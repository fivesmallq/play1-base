import argparse
import base64
import json
import subprocess
import sys
from io import BytesIO
from os import listdir
from os.path import isfile, join
from timeit import default_timer as timer

import matplotlib as mpl
import matplotlib.pyplot as plt
import pandas as pd
import pymongo
import seaborn as sns

from pymongo import MongoClient


class GCM:
    def __init__(self):
        self.mongo = MongoClient()
        self.db = self.mongo['gcm']

        self.strain = self.db['strain']

        self.strain.create_index([('genome_id', pymongo.ASCENDING), ('strainid', pymongo.ASCENDING)])

    def import_tsv(self, options: dict = None):
        strain_file = './strain.tsv'
        if options is not None:
            if 'input' in options:
                strain_file = options['input']
        print('reading strains from ', strain_file)
        df_strain = pd.read_csv(strain_file, sep='\t',
                                low_memory=False, encoding='utf-8')
        print('drop strains')
        self.strain.drop()
        print('insert strains into mongodb')
        self.strain.insert_many(json.loads(df_strain.T.to_json()).values())

    def gen_figures(self, filters: dict):
        """
        生成figure_names指定类型的PNG格式图片，返回这些图片{类型名:base64值} dict :rtype: dict {类型名:base64值} :param filters: {'red':'{
        species:Mus}','green':'{class:Mus}',figure_names: ['Scaffold_number','N50','N75','Largest_Scaffold',
        'Total_length','GC','COG']}
        """
        dfs = {}
        figures = {}
        figure_names: list = filters['figure_names']
        try:
            projection = eval(figure_names)
        except:
            return
        if projection is None :
            return
        if 'COG' in projection:
            projection.remove('COG')
            projection = projection + ['COG1', 'COG2', 'COG3', 'COG4']

        def exist_filter(color_filter:str):
            return color_filter in filters and filters[color_filter] is not None and len(filters[color_filter].strip()) > 0
        if exist_filter('red'):
            df_red = pd.DataFrame(self.strain.find(eval(filters['red']), projection=projection))
            if len(df_red) >0 :
                dfs['red'] = df_red
        if exist_filter('green'):
            df_green = pd.DataFrame(self.strain.find(eval(filters['green']), projection=projection))
            if len(df_green) > 0:

                dfs['green'] = df_green
        if exist_filter('blue'):
            df_blue = pd.DataFrame(self.strain.find(eval(filters['blue']), projection=projection))
            if len(df_blue) > 0:
                dfs['blue'] = df_blue
        if exist_filter('pink'):
            df_pink = pd.DataFrame(self.strain.find(eval(filters['pink']), projection=projection))
            if len(df_pink) > 0:
                dfs['pink'] = df_pink
        if exist_filter('yellow'):
            df_yellow = pd.DataFrame(self.strain.find(eval(filters['yellow']), projection=projection))
            if len(df_yellow)>0:
                dfs['yellow'] = df_yellow

        # todo: change figsize
        mpl.rcParams["figure.figsize"] = [6.4, 4.8]
        for col in eval(figure_names):
            # dfCol = pd.concat([df[col] for df in dfs.values],axis=1)
            # ax = dfCol.plot.kde()

            ax = None
            if 'COG' == col:
                _, axes = plt.subplots(1, len(dfs.keys()), sharey=True)
                idx = 0
                for color, df in dfs.items():
                    df_cog = df[['COG1', 'COG2', 'COG3', 'COG4']]
                    ax = df_cog.plot(kind='bar', stacked=True, ax=axes[idx], title=color)
                    idx = idx + 1
            else:

                for color, df in dfs.items():
                    ax = sns.kdeplot(df[col], color=color)
                if ax is not None:
                    ax.set_ylabel('density')
            if ax is not None:
                byte_image = BytesIO()
                ax.figure.savefig(byte_image, format='png')
                # todo : debug purpose
                # ax.figure.savefig(col+'.png')
                ax.cla()
                plt.clf()
                # if axes is not None and len(axes)>0:
                #     ax.clear()
                figures[col] = "data:image/png;base64,"+str(base64.encodebytes(byte_image.getvalue()), 'utf-8')
        json.dump(figures, sys.stdout)
        f = open('figures.json', 'w')
        json.dump(figures, f)
        f.close()


    def gen_bacmap(self, options: dict = None):
        """
        使用cgview批量生成input目录下所有fasta文件的circle 图，并将circle图所在路径保存到MongoDB中
        :param options:
        """
        root = '/data/input/'
        cgview_jar_path = './cgview.jar'
        if options is not None:
            if 'input' in options:
                root = options['input']
            if 'cgview_jar' in options:
                cgview_jar_path = options['cgview_jar']
        files = [join(root, f) for f in listdir(root) if (isfile(join(root, f)) and f.endswith('.fna'))]

        for idx, fasta in enumerate(files):
            print('{}/{}'.format(idx, len(files)))
            start = timer()
            genome_id = fasta.rpartition('/')[-1].rpartition('_')[0]
            print('running: perl cgview_xml_builder.pl')
            xml_builder_result = subprocess.run(
                ["perl", f"cgview_xml_builder.pl -sequence {fasta}  -output xml/{genome_id}.xml"], capture_output=True)
            if xml_builder_result.returncode == 0:
                print('running; java -jar cgview.jar')

                gen_map_result = subprocess.run(["java",
                                                 f"-jar -Xmx1500m {cgview_jar_path} -i xml/{genome_id}.xml  -s ./bacmap/{genome_id} -e F"],
                                                capture_output=True)
                if gen_map_result.returncode == 0:
                    self.strain.update_one({'genome_id': genome_id},
                                           {'$set': {'bacmap_path': f'/public/bacmap/{genome_id}/index.html'}})
                else:
                    print(gen_map_result.stderr)
            else:
                print(xml_builder_result.stderr)
            end = timer()
            print(end - start)


if __name__ == '__main__':

    # print(sys.argv)
    gcm = GCM()
    parser = argparse.ArgumentParser(description='gcw helper')
    subparsers = parser.add_subparsers()
    gen_figures = subparsers.add_parser('gen_figures')
    gen_figures.add_argument('--red')
    gen_figures.add_argument('--blue')
    gen_figures.add_argument('--green')
    gen_figures.add_argument('--yellow')
    gen_figures.add_argument('--pink')
    gen_figures.add_argument('--figure_names')
    gen_figures.set_defaults(func=gcm.gen_figures)

    gen_bacmap = subparsers.add_parser('gen_bacmap')
    gen_bacmap.add_argument('--input', help='fasta file folder,default /data/input')
    gen_bacmap.add_argument('--cgview_jar', help='cgview jar path')
    gen_bacmap.set_defaults(func=gcm.gen_bacmap)

    import_tsv = subparsers.add_parser('import_tsv')
    import_tsv.add_argument('--input', help='fasta file folder,default ./strain.tsv')
    import_tsv.set_defaults(func=gcm.import_tsv)
    args = parser.parse_args()
    args.func(vars(args))
