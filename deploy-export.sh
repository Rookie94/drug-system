#!/bin/sh

# 禁用历史扩展
# set +H

# ./ry.sh start 启动 stop 停止 restart 重启 status 状态
AppName=ruoyi-admin.jar

# 1. Jasypt 加密密钥
JASYPT_ENCRYPTOR_PASSWORD='t8Zr#kP2!mV@wQ5$xH9&nL4*eS7)uF1(+'

# 获取当前脚本所在目录
APP_HOME=$(cd "$(dirname "$0")"; pwd)
CONFIG_DIR=$APP_HOME/config
CERT_DIR=$APP_HOME/cert
LOG_DIR=$APP_HOME/logs
LOG_PATH=$LOG_DIR/$AppName.log

# 2. JVM 参数：把密钥作为系统属性传入
JVM_OPTS="-Dname=$AppName \
          -Duser.timezone=Asia/Shanghai \
          -Xms4096m -Xmx8192m \
          -XX:MetaspaceSize=1024m \
          -XX:MaxMetaspaceSize=4096m \
          -XX:+HeapDumpOnOutOfMemoryError \
          -XX:+PrintGCDateStamps \
          -XX:+PrintGCDetails \
          -XX:NewRatio=1 \
          -XX:SurvivorRatio=30 \
          -XX:+UseParallelGC \
          -XX:+UseParallelOldGC \
          -Djasypt.encryptor.password=$JASYPT_ENCRYPTOR_PASSWORD \
          -Dspring.config.location=$CONFIG_DIR/application.yml,$CONFIG_DIR/application-druid.yml \
          -Dserver.ssl.key-store=$CERT_DIR/yiti.shunxuan.net.cn.jks \
          -Dserver.ssl.key-store-password=4c1c4h4x \
          -Dserver.ssl.key-alias=yiti.shunxuan.net.cn"

# --- 以下逻辑无需改动 ---------------------------------
if [ "$1" = "" ]; then
    echo -e "\033[0;31m 未输入操作名 \033[0m  \033[0;34m {start|stop|restart|status} \033[0m"
    exit 1
fi

if [ "$AppName" = "" ]; then
    echo -e "\033[0;31m 未输入应用名 \033[0m"
    exit 1
fi

# 创建必要的目录
function create_directories() {
    if [ ! -d "$CONFIG_DIR" ]; then
        echo "创建配置目录: $CONFIG_DIR"
        mkdir -p "$CONFIG_DIR"
    fi
    
    if [ ! -d "$CERT_DIR" ]; then
        echo "创建证书目录: $CERT_DIR"
        mkdir -p "$CERT_DIR"
    fi
    
    if [ ! -d "$LOG_DIR" ]; then
        echo "创建日志目录: $LOG_DIR"
        mkdir -p "$LOG_DIR"
    fi
}

function start(){
    PID=`ps -ef |grep java|grep $AppName|grep -v grep|awk '{print $2}'`
    if [ x"$PID" != x"" ]; then
        echo "$AppName is running..."
    else
        echo "检查必要目录和文件..."
        create_directories
        
        # 检查配置文件是否存在
        if [ ! -f "$CONFIG_DIR/application.yml" ]; then
            echo "警告: 配置文件 $CONFIG_DIR/application.yml 不存在"
        fi
        
        if [ ! -f "$CONFIG_DIR/application-druid.yml" ]; then
            echo "警告: 配置文件 $CONFIG_DIR/application-druid.yml 不存在"
        fi
        
        # 检查证书文件是否存在
        if [ ! -f "$CERT_DIR/yiti.shunxuan.net.cn.jks" ]; then
            echo "警告: 证书文件 $CERT_DIR/yiti.shunxuan.net.cn.jks 不存在"
        fi
        
        echo "启动应用..."
        echo "工作目录: $APP_HOME"
        echo "配置目录: $CONFIG_DIR"
        echo "证书目录: $CERT_DIR"
        echo "完整启动命令:"
        echo "java $JVM_OPTS -jar $AppName"
        
        # 切换到应用目录
        cd "$APP_HOME"
        
        # 启动应用
        nohup java $JVM_OPTS -jar $AppName > $LOG_DIR/debug_startup.log 2>&1 &
        START_PID=$!
        
        # 等待并检查进程状态
        sleep 10
        if ps -p $START_PID > /dev/null; then
            echo "应用启动成功! PID: $START_PID"
            echo "应用日志: $LOG_PATH"
            echo "启动日志: $LOG_DIR/debug_startup.log"
        else
            echo "应用启动失败! 请检查启动日志:"
            cat $LOG_DIR/debug_startup.log
        fi
    fi
}

function stop(){
    echo "停止 $AppName"
    PID=""
    query(){
        PID=`ps -ef |grep java|grep $AppName|grep -v grep|awk '{print $2}'`
    }
    query
    if [ x"$PID" != x"" ]; then
        kill -TERM $PID
        echo "$AppName (pid:$PID) 正在退出..."
        while [ x"$PID" != x"" ]
        do
            sleep 1
            query
        done
        echo "$AppName 已停止."
    else
        echo "$AppName 未运行."
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
        echo "$AppName 正在运行..."
    else
        echo "$AppName 未运行..."
    fi
}

case $1 in
    start)   start;;
    stop)    stop;;
    restart) restart;;
    status)  status;;
    *)       echo "用法: $0 {start|stop|restart|status}";;
esac