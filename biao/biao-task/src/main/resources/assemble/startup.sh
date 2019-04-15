#!/bin/sh
#该脚本可在服务器上的任意目录下执行,不会影响到日志的输出位置等
#-------------------------------------------------------------------------------------------------------------
if [ ! -n "$JAVA_HOME" ]; then
	export JAVA_HOME="/usr/java/jdk1.8.0_77"
fi

#-------------------------------------------------------------------------------------------------------------
#       系统运行参数
#-------------------------------------------------------------------------------------------------------------
DIR=$(cd "$(dirname "$0")"; pwd)
APP_HOME=${DIR}/..
CLASSPATH=${APP_HOME}/conf
APP_LOG=${APP_HOME}/logs
APP_CONFIG=${APP_HOME}/conf/global.yml
SPRING_CONFIG=${APP_HOME}/conf/application.yml
if [ -n "$2" ]; then
    SPRING_CONFIG="$2"
fi
APP_MAIN=com.biao.TaskApplication
PIDFILE=${DIR}/PID
JAVA_OPTS="$JAVA_OPTS -server -Xms2048m -Xmx2048m -Xmn1024m -XX:ParallelGCThreads=20 -XX:+UseConcMarkSweepGC -XX:MaxGCPauseMillis=850 -XX:+PrintGCDetails -Xloggc:$APP_LOG/gc.log -Dfile.encoding=UTF-8"
JAVA_OPTS="$JAVA_OPTS -DlogPath=$APP_LOG -Dlog.level=info"
JAVA_OPTS="$JAVA_OPTS -Dspring.config.location=file:${SPRING_CONFIG}"
#-------------------------------------------------------------------------------------------------------------
#   程序开始
#-------------------------------------------------------------------------------------------------------------
for appJar in `ls ${APP_HOME}/lib/*.jar`;
do
   CLASSPATH="$CLASSPATH":"$appJar"
done
RUN_PID=0
status() {
    if [ -f ${PIDFILE} ]; then
        PID=$(cat ${PIDFILE})
        JAVAPS=`${JAVA_HOME}/bin/jps -l | grep ${PID}`
         if [ -n "$JAVAPS" ]; then
            RUN_PID=`echo ${JAVAPS} | awk '{print $1}'`
            return 0
        else
            return 1
        fi
    else
        return 1
    fi
}
status2() {
         JAVAPS=`${JAVA_HOME}/bin/jps -v | grep ${APP_HOME}`
        if [ -n "$JAVAPS" ]; then
            RUN_PID=`echo ${JAVAPS} | awk '{print $1}'`
            return 0
        else
            return 1
        fi
}
start(){
     echo -e "Starting the $APP_MAIN ...\c"
        status
        RETVAL=$?
        if [ ${RETVAL} -eq 0 ]; then
            echo ""
            echo  "$APP_MAIN  The old service is running, the need to stop(PID:$RUN_PID)..."
            kill -9 ${RUN_PID}
            echo  "Stopped(PID:$RUN_PID)..."
        fi
         if [ ! -d "$APP_LOG" ]; then
            mkdir "$APP_LOG"
         fi
           nohup ${JAVA_HOME}/bin/java ${JAVA_OPTS} -classpath ${CLASSPATH} ${APP_MAIN} > ${APP_LOG}/nohup.log 2>&1 &
        RETVAL=$?
        COUNT=0
            while [ $COUNT -lt 1 ]; do
                echo -e  ".\c"
                sleep 1
                COUNT=`ps -f | grep java | grep "$APP_HOME" | awk '{print $2}' | wc -l`
                if [ $COUNT -gt 0 ]; then
                    break
                fi
            done
         sleep 5
        echo ""
        cat ${APP_LOG}/nohup.log | while read LINE
            do
                echo $LINE
            done
        if [ ${RETVAL} -eq 0 ]; then
            echo $! > ${PIDFILE}
            PID=$(cat ${PIDFILE})
            echo  "Starting Successful(PID:$PID)"
            exit 0
        else
            echo  "Starting Failure"
            rm -f ${PIDFILE}
            exit 1
        fi

}
stop(){
     echo -e "Stopping the $APP_MAIN ...\c"
        status
        RETVAL=$?
        if [ ${RETVAL} -eq 0 ]; then
            kill -12 ${RUN_PID} > /dev/null 2>&1
            RETVAL=$?
             COUNT=0
            while [ $COUNT -lt 1 ]; do
                echo -e ".\c"
                sleep 1
                COUNT=`ps -f | grep java | grep "$APP_HOME" | awk '{print $2}' | wc -l`
                if [ $COUNT -le 0 ]; then
                    break
                fi
            done
            if [ ${RETVAL} -eq 0 ]; then
                rm -f ${PIDFILE}
                echo "Successful(PID:$RUN_PID)"
            else
                echo "Failure(PID:$RUN_PID)"
            fi
         else
              echo "Didn't find the service"
        fi
}
case "$1" in
    start)
          start
        ;;
    stop)
         stop
        ;;
    status)
        status
        RETVAL=$?
        if [ ${RETVAL} -eq 0 ]; then
            PID=$(cat ${PIDFILE})
            echo "$APP_MAIN is running($PID)"
        else
            echo "$APP_MAIN is stopped"
        fi
        ;;
     restart)
        stop
        start
        ;;
    *)
        echo "使用提示(后面带参): $0 {start|stop|status|restart}"
        ;;
esac