package ru.artemaa.cuba.cachecontrol.service;

import ru.artemaa.cache.ManagedCacheService;

import java.util.Set;

public interface CacheControlService extends ManagedCacheService {
    String NAME = "cachectrl_CacheControlService";

    Set<String> cacheNames();
}