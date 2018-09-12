package ru.otus.l02;

import org.github.jamm.MemoryMeter;

import java.util.*;

public class MemoryMeasurer {

    private MemoryMeter meter;
    private LinkedHashMap<String, Long> results = new LinkedHashMap<>();
    private Random rand;

    public MemoryMeasurer() {
        meter = new MemoryMeter();
        rand = new Random();
    }

    public MeasureResult getMeasures() {
        measureBasics();
        measureArrays();
        measureCollections();
        measureMaps();
        measureArrayList();
        measureLinkedList();
        measureHashSet();
        return new MeasureResult(new LinkedHashMap<>(results));
    }

    private void measureBasics() {
        results.put("Empty string", meter.measureDeep(new String("")));
        results.put("String", meter.measureDeep(new String("String")));
        results.put("Object", meter.measureDeep(new Object()));
    }

    private void measureArrays() {
        results.put("Array int[0]", meter.measureDeep(new int[0]));
        results.put("Array long[0]", meter.measureDeep(new long[0]));
        results.put("Array long[10]", meter.measureDeep(new long[10]));
    }

    private void measureCollections() {
        results.put("ArrayList", meter.measureDeep(new ArrayList<>()));
        results.put("HashSet", meter.measureDeep(new HashSet<>()));
        results.put("LinkedList", meter.measureDeep(new LinkedList<>()));
        results.put("LinkedHashSet", meter.measureDeep(new LinkedHashSet<>()));
        results.put("TreeSet", meter.measureDeep(new TreeSet<>()));
    }

    private void measureMaps() {
        results.put("HashMap", meter.measureDeep(new HashMap<>()));
        results.put("LinkedHashMap", meter.measureDeep(new LinkedHashMap<>()));
        results.put("TreeMap", meter.measureDeep(new TreeMap<>()));
    }

    private void measureArrayList() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        results.put("ArrayList<Integer> (empty)", meter.measureDeep(list));

        for (int i = 0; i < 10; i++) {
            list.add(rand.nextInt());
            results.put("ArrayList<Integer> (" + (i+1) + ")", meter.measureDeep(list));
        }
    }

    private void measureLinkedList() {
        LinkedList<Integer> list = new LinkedList<Integer>();
        results.put("LinkedList<Integer> (empty)", meter.measureDeep(list));

        for (int i = 0; i < 10; i++) {
            list.add(rand.nextInt());
            results.put("LinkedList<Integer> (" + (i+1) + ")", meter.measureDeep(list));
        }
    }

    private void measureHashSet() {
        HashSet<Long> list = new HashSet<Long>();
        results.put("HashSet<Long> (empty)", meter.measureDeep(list));

        for (int i = 0; i < 10; i++) {
            list.add(rand.nextLong());
            results.put("HashSet<Long> (" + (i+1) + ")", meter.measureDeep(list));
        }
    }

}
