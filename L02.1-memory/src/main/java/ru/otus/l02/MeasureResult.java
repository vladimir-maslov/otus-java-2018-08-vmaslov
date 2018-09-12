package ru.otus.l02;

import java.util.LinkedHashMap;

public class MeasureResult {

    private LinkedHashMap<String, Long> results;

    public MeasureResult(LinkedHashMap<String, Long> results){
        this.results = results;
    }

    public void printResult() {
        results.forEach((k, v) -> {
            System.out.println(k + " — " + v + " bytes");
        });
    }

}
