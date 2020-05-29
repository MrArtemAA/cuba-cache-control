package ru.artemaa.cuba.cachecontrol.service;

import org.springframework.stereotype.Service;
import ru.artemaa.cache.CacheInfo;
import ru.artemaa.cache.ManagedCache;
import ru.artemaa.cache.ManagedCacheService;

import java.util.*;

@Service(CacheControlService.NAME)
public class CacheControlServiceBean implements CacheControlService {
    protected final Map<String, ManagedCache> cacheMap;

    public CacheControlServiceBean(List<ManagedCache> caches) {
        cacheMap = new HashMap<>();
        caches.forEach(managedCache -> cacheMap.put(managedCache.name(), managedCache));
    }

    @Override
    public Set<String> cacheNames() {
        return new HashSet<>(cacheMap.keySet());
    }

    @Override
    public long refresh(String cacheName) {
        ManagedCache cache = getManagedCacheOrThrowException(cacheName);
        return cache.refresh();
    }

    @Override
    public CacheInfo cacheInfo(String cacheName) {
        ManagedCache cache = getManagedCacheOrThrowException(cacheName);
        return new CacheInfo(cache.totalCachedItems(), cache.lastSync());
    }

    private ManagedCache getManagedCacheOrThrowException(String cacheName) {
        Objects.requireNonNull(cacheName);
        ManagedCache cache = cacheMap.get(cacheName);
        if (cache == null) {
            throw new CacheNotFoundException(cacheName);
        }
        return cache;
    }
}