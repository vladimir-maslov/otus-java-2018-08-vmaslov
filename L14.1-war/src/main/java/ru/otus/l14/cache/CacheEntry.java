package ru.otus.l14.cache;

import java.lang.ref.SoftReference;

public class CacheEntry<V> {
    private final SoftReference<V> value;
    private final long creationTime;
    private long lastAccessTime;

    public CacheEntry(V value){
        this.value = new SoftReference<V>(value);
        this.creationTime = getCurrentTime();
        this.lastAccessTime = getCurrentTime();
    }

    public V getValue(){
        return value.get();
    }

    public long getCurrentTime(){
        return System.currentTimeMillis();
    }

    public long getCreationTime(){
        return creationTime;
    }

    public long getLastAccessTime(){
        return lastAccessTime;
    }

    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    void setAccessed(){
        lastAccessTime = getCurrentTime();
    }
}