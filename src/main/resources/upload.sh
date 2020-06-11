##!/bin/bash
ip=192.168.2.2
#pwd=123456
cd ../../../target
#查询 进程端口号 并杀死进程
#ssh -l root $ip kill -9 `ps -ef |grep java  |grep aicoin.jar | awk '{print $2}'`;
#pid = $(ssh root@$ip `ps -ef |grep java  |grep aicoin.jar | awk '{print $2}'`);
#echo $pid
#ssh -l root $ip kill -9 $pid;
#ssh -l root $ip ps -ef |grep java  |grep aicoin.jar | awk '{print $2}' | xargs kill -9;
ssh -l root $ip "ps -ef | grep aicoin.jar | grep -v grep | awk '{print $2}' | xargs kill -9";
#ssh -l root $ip kill -9 $(ps -ef | grep aicoin.jar | grep -v grep | awk '{print $2}');
#复制jar包到xxx
#scp -r aicoin.jar root@$ip:/usr/local/project
#启动项目
#ssh -l root $ip "cd /usr/local/project;nohup /usr/local/jdk1.8.0/bin/java -jar aicoin.jar > log.log 2>&1 &";
#exit
