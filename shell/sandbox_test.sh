#!/usr/bin/env bash

pid=`ps aux | grep 'java com.angelwhu'|awk '{print $2}'|sed -n '2p'`
echo ${pid}
cd /Users/didi/Documents/github/jvm-sandbox/sandbox/bin
bash ./sandbox.sh -p ${pid}
bash ./sandbox.sh -p ${pid} -d "system-block/blockSystemExec"
bash ./sandbox.sh -p ${pid} -d "system-block/blockSystemExit"

