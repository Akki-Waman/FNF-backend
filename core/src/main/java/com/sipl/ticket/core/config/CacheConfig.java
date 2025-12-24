package com.sipl.ticket.core.config;

import com.sipl.ticket.core.exception.custom.RedisSafeCacheErrorHandler;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheErrorHandler cacheErrorHandler() {
        return new RedisSafeCacheErrorHandler();
    }
}