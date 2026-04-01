package com.sipl.ticket.redisCache.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/redis")
@CrossOrigin("*")
@Api(tags = "Redis")
@Slf4j
public class RedisCacheController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * ✅ Redis Health Check API
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> isRedisUp() {
        log.info("[RedisHealth] Redis health check API called");

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());

        try {
            Boolean pingResult = redisTemplate.execute(
                    (RedisCallback<Boolean>) connection -> {
                        connection.ping();
                        return Boolean.TRUE;
                    }
            );

            if (Boolean.TRUE.equals(pingResult)) {
                log.info("[RedisHealth] Redis is UP");
                response.put("redisUp", true);
                response.put("status", "UP");
                return ResponseEntity.ok(response);
            }

        } catch (Exception ex) {
            log.error("[RedisHealth] Redis health check FAILED", ex);
        }

        response.put("redisUp", false);
        response.put("status", "DOWN");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    /**
     * ⚠️ Delete cache by project prefix
     */
    @DeleteMapping("/cache/deleteProject/{projectPrefix}")
    public String deleteProjectCache(@PathVariable String projectPrefix) {

        log.warn("[RedisCache] deleteProjectCache called for prefix: {}", projectPrefix);
        log.warn("[RedisCache] Using KEYS command (not recommended for large datasets)");

        Set<String> keys = redisTemplate.keys(projectPrefix + "*");

        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            log.info("[RedisCache] Deleted {} keys for projectPrefix={}", keys.size(), projectPrefix);
            return "✅ Deleted all keys for project: " + projectPrefix
                    + " (" + keys.size() + " keys)";
        }

        log.info("[RedisCache] No keys found for projectPrefix={}", projectPrefix);
        return "⚠️ No keys found for project: " + projectPrefix;
    }

    /**
     * ⚠️ Delete cache by cacheName
     */
    @DeleteMapping("/cache/deleteByCacheName/{cacheName}")
    public String deleteByCacheName(@PathVariable String cacheName) {

        log.warn("[RedisCache] deleteByCacheName called for cacheName: {}", cacheName);
        log.warn("[RedisCache] Using KEYS command (not recommended for production)");

        Set<String> keys = redisTemplate.keys("*");

        if (keys != null && !keys.isEmpty()) {

            Set<String> matchedKeys = keys.stream()
                    .filter(k -> k.startsWith(cacheName + "::"))
                    .collect(Collectors.toSet());

            if (!matchedKeys.isEmpty()) {
                redisTemplate.delete(matchedKeys);
                log.info("[RedisCache] Deleted {} keys for cacheName={}", matchedKeys.size(), cacheName);
                return "✅ Deleted all keys for cacheName: " + cacheName
                        + " (" + matchedKeys.size() + " keys)";
            }

            log.info("[RedisCache] No matching keys for cacheName={}", cacheName);
            return "⚠️ No keys found for cacheName: " + cacheName;
        }

        log.info("[RedisCache] Redis is empty");
        return "⚠️ Redis is empty";
    }

    /**
     * ⚠️ Get all Redis keys
     */
    @GetMapping("/cache/keys")
    public Set<String> getAllKeys() {

        log.warn("[RedisCache] getAllKeys API called (KEYS *)");
        return redisTemplate.keys("*");
    }
}
