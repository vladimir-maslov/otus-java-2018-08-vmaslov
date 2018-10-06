#!/usr/bin/env bash

REMOTE_DEBUG="-agentlib:jdwp=transport=dt_socket,address=14000,server=y,suspend=n"
MEMORY="-Xms64m -Xmx64m -XX:MaxMetaspaceSize=24m"
GC="-XX:+UseG1GC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+ScavengeBeforeFullGC -XX:+CMSScavengeBeforeRemark"
GC_LOG=" -verbose:gc -Xlog:gc*:file=./logs/gc_pid_%p.log"
JMX="-Dcom.sun.management.jmxremote.port=15000 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"
DUMP="-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./dumps/"

#-XX:OnOutOfMemoryError="kill -3 %p"


java $REMOTE_DEBUG $MEMORY $GC $GC_LOG $JMX $DUMP -jar target/L05.1-gc.jar > jvm.out
