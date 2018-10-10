package ru.otus.l05;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.util.*;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;

public class Benchmark implements BenchmarkMBean {

    public final static double MINUTES_IN_MILLISECOND = 1.66667e-5;

    private volatile int size = 0;
    private ArrayList<Integer> testList = new ArrayList<>();

    private Map<String, Entry<Integer, Double>> results = new HashMap<>();

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public void installGCMonitoring() {
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            System.out.println(gcbean.getName());

            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());

                    long duration = info.getGcInfo().getDuration();
                    String gctype = info.getGcAction();
                    String gcName = info.getGcName();

                    Entry<Integer, Double> result = results.get(gcName);

                    int totalGCcount = (result != null) ? result.getKey() : 0;
                    totalGCcount++;

                    double totalGCDuration = (result != null) ? result.getValue() : 0;
                    totalGCDuration += duration * MINUTES_IN_MILLISECOND;

                    results.put(gcName, new SimpleEntry<Integer, Double>(totalGCcount, totalGCDuration));

                    System.out.println(gctype + ": - "
                            + info.getGcInfo().getId() + ", "
                            + info.getGcName()
                            + " (from " + info.getGcCause() + ") " + duration + " milliseconds");

                }
            };

            emitter.addNotificationListener(listener, null, null);
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void run() throws InterruptedException {
        Random rand = new Random();
        while (true) {
            for (int i = 0; i < 14; i++) {
                testList.add(rand.nextInt());
            }
            testList.remove(testList.size() - 1);
            Thread.sleep(0, 1);
        }
    }

    public BenchmarkResult getResult(){
        return new BenchmarkResult(results);
    }
}
