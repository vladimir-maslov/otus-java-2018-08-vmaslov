package ru.otus.l06;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // new Main().eternalCacheExample();
        new Main().lifeCacheExample();
    }

    private void eternalCacheExample() {
        int size = 8;
        CacheEngine<Integer, CacheEntry<String>> cache = new CacheEngineImpl<>(size, 0, 0, true);

        for (int i = 0; i < 10; i++) {
            cache.put(i, new CacheEntry<>("String: " + i));
        }

        for (int i = 0; i < 10; i++) {
            CacheEntry<String> element = cache.get(i);
            System.out.println("String for " + i + ": " + (element != null ? element.getValue() : "null"));
        }

        System.out.println("Cache hits: " + cache.getHitCount());
        System.out.println("Cache misses: " + cache.getMissCount());

        cache.dispose();
    }

    private void lifeCacheExample() throws InterruptedException {
        int size = 10;
        CacheEngine<Integer, CacheEntry<String>> cache = new CacheEngineImpl<>(size, 500, 0, false);

        for (int i = 0; i < size; i++) {
            cache.put(i, new CacheEntry<>("String: " + i));
        }

        for (int i = 0; i < size; i++) {
            CacheEntry<String> element = cache.get(i);
            System.out.println("String for " + i + ": " + (element != null ? element.getValue() : "null"));
        }

        System.out.println("Cache hits: " + cache.getHitCount());
        System.out.println("Cache misses: " + cache.getMissCount());

        Thread.sleep(700);

        for (int i = 0; i < size; i++) {
            CacheEntry<String> element = cache.get(i);
            System.out.println("String for " + i + ": " + (element != null ? element.getValue() : "null"));
        }

        System.out.println("Cache hits: " + cache.getHitCount());
        System.out.println("Cache misses: " + cache.getMissCount());

        cache.dispose();
    }
}
