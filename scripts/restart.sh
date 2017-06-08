#!/usr/bin/env bash

pushd `dirname $0`
SCRIPT_PATH=`pwd`
LVOTE_PID=`ps -ef | grep '[j]ava -jar.*lvote.*.war' | awk '{ print $2 }'`

if [ -z "$LVOTE_PID" ]; then
    echo "Lvote server not started, could not be stopped"

else
	echo "Killing Lvote server PID: $LVOTE_PID ..."
	echo "kill -9 $LVOTE_PID"
	kill -9 $LVOTE_PID
	echo "Killing Lvote server"
fi

echo "Recreating db schema..."
${SCRIPT_PATH}/recreate_db_schema.sh
echo "DB schema recreated"

echo "Starting lvote server..."
#echo "java -jar ${SCRIPT_PATH}/../target/lvote*.war >> ${SCRIPT_PATH}/../logs/server.log 2>> ${SCRIPT_PATH}/../logs/server.error.log &"
#java -jar ${SCRIPT_PATH}/../target/lvote*.war >> ${SCRIPT_PATH}/../logs/server.log 2>> ${SCRIPT_PATH}/../logs/server.error.log &
echo "java -jar /lvote/target/lvote*.war >> /lvote/logs/server.log 2>> /lvote/logs/server.error.log &"
java -jar /lvote/target/lvote*.war >> /lvote/logs/server.log 2>> /lvote/logs/server.error.log &



echo "Lvote server started"
