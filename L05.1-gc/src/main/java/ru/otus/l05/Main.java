package ru.otus.l05;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.MBeanServer;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

/**
 * VM options:
 *
 * -agentlib:jdwp=transport=dt_socket,address=14000,server=y,suspend=n
 * -Xms64m -Xmx64m -XX:MaxMetaspaceSize=24m
 * -XX:+UseSerialGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSInitiatingOccupancyOnly
 * -XX:CMSInitiatingOccupancyFraction=70 -XX:+ScavengeBeforeFullGC -XX:+CMSScavengeBeforeRemark
 * -verbose:gc -Xlog:gc*:file=./logs/gc_pid_%p.log
 * -Dcom.sun.management.jmxremote.port=15000 -Dcom.sun.management.jmxremote.authenticate=false
 * -Dcom.sun.management.jmxremote.ssl=false
 * -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./dumps/
 */

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("PID: " + ManagementFactory.getRuntimeMXBean().getPid());

        int size = 5 * 1000 * 1000;

        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("com.otus:type=Benchmark");
        Benchmark mbean = new Benchmark();
        mBeanServer.registerMBean(mbean, name);
        mbean.setSize(size);

        try {
            mbean.installGCMonitoring();
            mbean.run();
        } catch (OutOfMemoryError e){
            System.out.println("[Application has crashed with OOM]");
            e.printStackTrace();
        } finally {
            BenchmarkResult bResult = mbean.getResult();
            bResult.print();
        }



    }

}
