#!/bin/sh

PATH=$PATH:/usr/local/bin
export PATH

/usr/local/bin/python3 /Users/zhaoxiaoyong/Documents/workspace/play1-base/demo/etl/gcm.py  gen_figures  --blue "{'Species':{'\$eq':'Salmonella enterica'}}" --red "{'Class':{'\$eq':'Bacilli'}}" --figure_names "['N50','N75']"
