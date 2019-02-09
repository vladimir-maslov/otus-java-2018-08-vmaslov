package ru.otus.l15.cache;

public interface CacheEngine<K, V> {
    void put(K key, V val);

    V get (K key);

    int getHitCount();

    int getMissCount();

    int size();

    void dispose();
}
