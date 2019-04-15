#!/usr/bin/env bash
noJavaHome=false
if [ -z "$JAVA_HOME" ] ; then
    noJavaHome=true
fi

if [ ! -e "$JAVA_HOME/bin/java" ] ; then
    noJavaHome=true
fi

if $noJavaHome ; then
    echo
    echo "Error: JAVA_HOME environment variable is not set."
    echo
    exit 1
fi

APP='/home/deploy/plat/bbex-plat.jar'
ENV=$1
BASE_DIR=/home/deploy/
APP_DIR=$BASE_DIR/plat/
LOG_DIR=$BASE_DIR/plat/logs
PID_FILE=$APP_DIR/bbex-plat.pid

cd $APP_DIR
#设置java的CLASSPATH

#USER_LOG_DIR=$BASE_DIR/userLogs
#==============================================================================

#set JAVA_OPTS
#JAVA_OPTS="-server -Xmx2048m  -Xms2048g -Xmn1024m  -Xss512k -XX:PermSize=128m"
JAVA_OPTS="-server -Xmx4096m  -Xms4096m -Xmn1024m  -Xss512k"
JAVA_OPTS="$JAVA_OPTS -XX:+AggressiveOpts"
JAVA_OPTS="$JAVA_OPTS -XX:+UseBiasedLocking"
JAVA_OPTS="$JAVA_OPTS -XX:+UseFastAccessorMethods"
JAVA_OPTS="$JAVA_OPTS -XX:+DisableExplicitGC"
JAVA_OPTS="$JAVA_OPTS -XX:+UseParNewGC"
JAVA_OPTS="$JAVA_OPTS -XX:ParallelGCThreads=20"
JAVA_OPTS="$JAVA_OPTS -XX:+UseConcMarkSweepGC"
JAVA_OPTS="$JAVA_OPTS -XX:+HeapDumpOnOutOfMemoryError"
JAVA_OPTS="$JAVA_OPTS -XX:MaxGCPauseMillis=850"
#JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCDetail"
JAVA_OPTS="$JAVA_OPTS -XX:+CMSParallelRemarkEnabled"
#JAVA_OPTS="$JAVA_OPTS -XX:+UseCMSCompactAtFullCollection"
JAVA_OPTS="$JAVA_OPTS -XX:+UseCMSInitiatingOccupancyOnly"
JAVA_OPTS="$JAVA_OPTS -XX:CMSInitiatingOccupancyFraction=75"
JAVA_OPTS="$JAVA_OPTS -Xloggc:$LOG_DIR/gc.log"
JAVA_OPTS="$JAVA_OPTS -DlogPath=$LOG_DIR"


RETVAL=0
start()
{
    mkdir -p $LOG_DIR
    run_cmd="java $JAVA_OPTS -jar $APP "
    $run_cmd >> $LOG_DIR/stdout.log 2>&1 &
    echo $! > "$PID_FILE"

    if [ $? -gt 0 ]; then
                echo "Starting $APP ERROR"
    fi
    echo "Starting $APP OK"
}

#
stop()
{
        PID=$(cat $PID_FILE 2>/dev/null)
        kill -KILL $PID 2>/dev/null
        echo "Stopping $APP OK"
}

restart()
{
        stop
        start
}

case "$2" in
  start)
        start ;;
  stop)
        stop ;;
  restart|force-reload|reload)
        restart ;;
  status)
        status -p $PID_FILE $APP;;
  *)
    echo $"Usage: $0 {start|stop|status|restart|reload|force-reload}"
    exit 1
esac

RETVAL=$?
exit $RETVAL
