package ru.otus.l05;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BenchmarkResult {
    private Map<String, Map.Entry<Integer, Double>> result = new HashMap<>();

    public BenchmarkResult(Map<String, Map.Entry<Integer, Double>> result){
        this.result = result;
    }

    public void print() {
        System.out.println("[GC Results]");
        result.forEach((k, v) -> {
            System.out.println(k + " — "
                    + "Runs: " + v.getKey() + ", "
                    + "Duration: " + v.getValue() + " min.");
        });
    }
}
