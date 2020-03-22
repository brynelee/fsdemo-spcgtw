#!/bin/bash

echo "********************************************************"
echo "Starting Spring Cloud Gateway Service ..."
echo "********************************************************"
java -Ddebug=$DEBUG_MODE \
     -Dserver.port=$SERVERPORT \
    -jar /usr/local/spcgtw/@project.build.finalName@.jar
