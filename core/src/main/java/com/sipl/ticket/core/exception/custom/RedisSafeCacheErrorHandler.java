package com.sipl.ticket.core.exception.custom;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisSafeCacheErrorHandler implements CacheErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(RedisSafeCacheErrorHandler.class);

    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        logger.warn("Redis GET failed for key {}: {}", key, exception.getMessage());
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        logger.warn("Redis PUT failed for key {}: {}", key, exception.getMessage());
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        logger.warn("Redis EVICT failed for key {}: {}", key, exception.getMessage());
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        logger.warn("Redis CLEAR failed: {}", exception.getMessage());
    }
}
