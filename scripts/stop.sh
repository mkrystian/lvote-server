#!/usr/bin/env bash

LVOTE_PID=`ps -ef | grep '[j]ava -jar.*lvote.*.war' | awk '{ print $2 }'`

if [ -z "$LVOTE_PID" ]; then
    echo "Lvote server not started, could not be stopped"

else
        echo "Stopping Lvote server PID: $LVOTE_PID ..."
        echo "kill -9 $LVOTE_PID"
        kill -9 $LVOTE_PID
        echo "Lvote server stopped"

fi
