#!/bin/bash

echo "********************************************************"
echo "Starting Spring Cloud Gateway Service ..."
echo "********************************************************"
java -Ddebug=$DEBUG_MODE \
     -Dserver.port=$SERVERPORT \
     -jar /usr/src/app/fsdemo-spcgtw-0.0.1-SNAPSHOT.jar
