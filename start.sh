#!/bin/sh
# ./ry.sh start 启动 stop 停止 restart 重启 status 状态
AppName=ruoyi-admin.jar

# 1. Jasypt 加密密钥（写死版本）
JASYPT_ENCRYPTOR_PASSWORD="t8Zr#kP2!mV@wQ5$xH9&nL4*eS7)uF1(+"   # <-- 改这里即可换密钥

# 2. JVM 参数：把密钥作为系统属性传入
JVM_OPTS="-Dname=$AppName \
          -Duser.timezone=Asia/Shanghai \
          -Xms512m -Xmx1024m \
          -XX:MetaspaceSize=128m \
          -XX:MaxMetaspaceSize=512m \
          -XX:+HeapDumpOnOutOfMemoryError \
          -XX:+PrintGCDateStamps \
          -XX:+PrintGCDetails \
          -XX:NewRatio=1 \
          -XX:SurvivorRatio=30 \
          -XX:+UseParallelGC \
          -XX:+UseParallelOldGC \
          -Djasypt.encryptor.password=$JASYPT_ENCRYPTOR_PASSWORD"

APP_HOME=`pwd`
LOG_PATH=$APP_HOME/logs/$AppName.log

# --- 以下逻辑无需改动 ---------------------------------
if [ "$1" = "" ]; then
    echo -e "\033[0;31m 未输入操作名 \033[0m  \033[0;34m {start|stop|restart|status} \033[0m"
    exit 1
fi

if [ "$AppName" = "" ]; then
    echo -e "\033[0;31m 未输入应用名 \033[0m"
    exit 1
fi

function start(){
    PID=`ps -ef |grep java|grep $AppName|grep -v grep|awk '{print $2}'`
    if [ x"$PID" != x"" ]; then
        echo "$AppName is running..."
    else
        nohup java $JVM_OPTS -jar $AppName > /dev/null 2>&1 &
        echo "Start $AppName success..."
    fi
}

function stop(){
    echo "Stop $AppName"
    PID=""
    query(){
        PID=`ps -ef |grep java|grep $AppName|grep -v grep|awk '{print $2}'`
    }
    query
    if [ x"$PID" != x"" ]; then
        kill -TERM $PID
        echo "$AppName (pid:$PID) exiting..."
        while [ x"$PID" != x"" ]
        do
            sleep 1
            query
        done
        echo "$AppName exited."
    else
        echo "$AppName already stopped."
    fi
}

function restart(){
    stop
    sleep 2
    start
}

function status(){
    PID=`ps -ef |grep java|grep $AppName|grep -v grep|wc -l`
    if [ $PID != 0 ];then
        echo "$AppName is running..."
    else
        echo "$AppName is not running..."
    fi
}

case $1 in
    start)   start;;
    stop)    stop;;
    restart) restart;;
    status)  status;;
    *)       echo "Usage: $0 {start|stop|restart|status}";;
esac