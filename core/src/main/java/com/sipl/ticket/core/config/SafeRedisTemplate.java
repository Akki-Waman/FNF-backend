package com.sipl.ticket.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Slf4j
public class SafeRedisTemplate {

    private final RedisTemplate<String, Object> redisTemplate;

    public SafeRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Object get(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.warn("Redis down, skipping cache get for key: {}", key, e);
            return null;
        }
    }

    public void put(String key, Object value, long ttlSeconds) {
        try {
            redisTemplate.opsForValue()
                    .set(key, value, Duration.ofSeconds(ttlSeconds));
        } catch (Exception e) {
            log.warn("Redis down, skipping cache put for key: {}", key, e);
        }
    }

    public void delete(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.warn("Redis down, skipping cache delete for key: {}", key, e);
        }
    }
}
