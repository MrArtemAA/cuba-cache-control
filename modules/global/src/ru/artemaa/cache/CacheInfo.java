package ru.artemaa.cache;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CacheInfo implements Serializable {
    protected final long totalCachedItems;
    protected final LocalDateTime lastSync;

    public CacheInfo(long totalCachedItems, LocalDateTime lastSync) {
        this.totalCachedItems = totalCachedItems;
        this.lastSync = lastSync;
    }

    public long getTotalCachedItems() {
        return totalCachedItems;
    }

    public LocalDateTime getLastSync() {
        return lastSync;
    }
}
