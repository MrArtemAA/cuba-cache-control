package ru.artemaa.cache;

import java.time.LocalDateTime;

public interface ManagedCache {
    String name();
    long refresh();
    LocalDateTime lastSync();
    long totalCachedItems();
}
