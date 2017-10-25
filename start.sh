#!/bin/sh
set -e
if [ -n "$DT_AGENT_PATH" ];then
    java -ea -agentpath:$DT_AGENT_PATH -Djava.security.egd=file:/dev/./urandom -jar build/libs/image-processing-service-1.0.jar
else
    java -ea -Djava.security.egd=file:/dev/./urandom -jar build/libs/image-processing-service-1.0.jar
fi