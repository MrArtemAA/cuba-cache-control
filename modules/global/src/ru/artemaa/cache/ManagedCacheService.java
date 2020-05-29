package ru.artemaa.cache;

public interface ManagedCacheService {
    long refresh(String cacheName);
    CacheInfo cacheInfo(String cacheName);

    class CacheNotFoundException extends RuntimeException {
        public CacheNotFoundException(String cacheName) {
            super("Managed cache not found by name: " + cacheName);
        }
    }
}
