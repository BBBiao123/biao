#!/bin/sh
APP_CONFIG="$1"

cd $(dirname $0)
source ./startup.sh start ${APP_CONFIG}
