package ru.otus.l05;

public interface BenchmarkMBean {
    int getSize();

    void setSize(int size);

    void installGCMonitoring();
}
