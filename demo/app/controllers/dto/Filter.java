package controllers.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;

import static utils.Logs.error;

public class Filter {
    String red;
    String green;
    String blue;
    String yellow;
    String pink;
    String figure_names;
    @JSONField(name="figure_names")
    public String getFigure_names() {
        return figure_names;
    }

    public void setFigure_names(String figure_names) {
        this.figure_names = figure_names;
    }

    public String getRed() {
        return red;
    }

    public void setRed(String red) {
        this.red = red;
    }

    public String getGreen() {
        return green;
    }

    public void setGreen(String green) {
        this.green = green;
    }

    public String getBlue() {
        return blue;
    }

    public void setBlue(String blue) {
        this.blue = blue;
    }

    public String getYellow() {
        return yellow;
    }

    public void setYellow(String yellow) {
        this.yellow = yellow;
    }
    @JSONField(name="pink")
    public String getMongoPink(){
        return toMongoFilter(pink);
    }
    @JSONField(name="red")
    public String getMongoRed(){
        return toMongoFilter(red);
    }
    @JSONField(name="yellow")
    public String getMongoYellow(){
        return toMongoFilter(yellow);
    }
    @JSONField(name="blue")
    public String getMongoBlue(){
        return toMongoFilter(blue);
    }
    @JSONField(name="green")
    public String getMongoGreen(){
        return toMongoFilter(green);
    }
    public String getPink() {
        return pink;
    }
    public String toParamJson(){
        return JSON.toJSONString(this, SerializerFeature.BrowserCompatible);
    }
    public void setPink(String pink) {
        this.pink = pink;
    }
    public String toArgs(){
        StringBuilder  args=new StringBuilder();

        if(!Strings.isNullOrEmpty(getMongoBlue())){
            args.append(" --blue ").append('"').append(getMongoBlue()).append('"');
        }
        if(!Strings.isNullOrEmpty(getMongoRed())){
            args.append(" --red ").append('"').append(getMongoRed()).append('"');
        }
        if(!Strings.isNullOrEmpty(getMongoGreen())){
            args.append(" --green ").append('"').append(getMongoGreen()).append('"');
        }
        if(!Strings.isNullOrEmpty(getMongoYellow())){
            args.append(" --yellow ").append('"').append(getMongoYellow()).append('"');
        }
        if(!Strings.isNullOrEmpty(getMongoPink())){
            args.append(" --pink ").append('"').append(getMongoPink()).append('"');
        }
        if(!Strings.isNullOrEmpty(getFigure_names())){
            args.append(" --figure_names ").append('"').append(getFigure_names().replace("\"","\\\"")).append('"');
        }
        return args.toString();
    }
    /**
     * 将 where条件转化为mongo的条件，目前支持==,!=, 单个and或者or
     * @param filter a==b & c!=d
     * @return [{a:'b'}]
     */
    private static String toMongoFilter(String filter) {
        String mongoFilter;
        if(Strings.isNullOrEmpty(filter)){
            return null;
        }
        String[] conditions = filter.split("[&|]");
        String[] mongoFilters = new String[conditions.length];
        for (int i = 0; i < conditions.length; i++) {
            String condition = conditions[i];
            String left,op,right;
            String[] split = condition.split("==|!=");
            if (split.length == 2) {
                left = split[0].trim();
                right = split[1].trim();
            }else {
                error("expression error:"+filter);
                return null;
            }
            // bash脚本中，遇到$符号会特殊处理，因此需要对$服务转义
            if(condition.indexOf("==")>0) {
                op = "\\$eq";

            }else if(condition.indexOf("!=")>0){
                op = "\\$ne";
            }else{
                error("expression error:"+filter);
                return null;
            }
            mongoFilters[i] = String.format("{'%s':{'%s':'%s'}}", left, op, right);

        }

        if(filter.indexOf('&')>0){
            mongoFilter = String.format("{ '\\$and': [ %s ] }", Joiner.on(',').join(mongoFilters));

        }else if(filter.indexOf('|')>0){
            mongoFilter =String.format("{ '\\$or': [ %s ] }",Joiner.on(',').join(mongoFilters));
        }else {
            mongoFilter = mongoFilters[0];
        }

        return mongoFilter;
    }

    public static void main(String[] args) {
        Filter f = new Filter();
        f.red="Class == Bacilli";
        f.blue="Species == Salmonella enterica";
        f.figure_names="['N50','N75']";
        System.out.println(f.toArgs());
    }
}
