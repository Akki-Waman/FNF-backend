package com.sipl.ticket.redisCache.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/cache/deleteProject/{projectPrefix}")
    public String deleteProjectCache(@PathVariable String projectPrefix) {
        log.info("Request received to delete cache for projectPrefix: {}", projectPrefix);

        try {
            Set<String> keys = redisTemplate.keys(projectPrefix + "*");

            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
                log.info("Deleted {} keys for projectPrefix: {}", keys.size(), projectPrefix);
                return "✅ Deleted all keys for project: " + projectPrefix
                        + " (" + keys.size() + " keys)";
            }

            log.warn("No keys found for projectPrefix: {}", projectPrefix);
            return "⚠️ No keys found for project: " + projectPrefix;

        } catch (Exception e) {
            log.error("Error while deleting cache for projectPrefix: {}", projectPrefix, e);
            return "❌ Error while deleting project cache";
        }
    }

    @DeleteMapping("/cache/deleteByCacheName/{cacheName}")
    public String deleteByCacheName(@PathVariable String cacheName) {
        log.info("Request received to delete cache for cacheName: {}", cacheName);

        try {
            Set<String> keys = redisTemplate.keys("*");

            if (keys == null || keys.isEmpty()) {
                log.warn("Redis is empty");
                return "⚠️ Redis is empty";
            }

            Set<String> matchedKeys = keys.stream()
                    .filter(k -> k.startsWith(cacheName + "::"))
                    .collect(Collectors.toSet());

            if (!matchedKeys.isEmpty()) {
                redisTemplate.delete(matchedKeys);
                log.info("Deleted {} keys for cacheName: {}", matchedKeys.size(), cacheName);
                return "✅ Deleted all keys for cacheName: " + cacheName
                        + " (" + matchedKeys.size() + " keys)";
            }

            log.warn("No keys found for cacheName: {}", cacheName);
            return "⚠️ No keys found for cacheName: " + cacheName;

        } catch (Exception e) {
            log.error("Error while deleting cache for cacheName: {}", cacheName, e);
            return "❌ Error while deleting cache";
        }
    }

    @GetMapping("/cache/keys")
    public Set<String> getAllKeys() {
        log.info("Request received to fetch all Redis keys");

        Set<String> keys = redisTemplate.keys("*");
        log.info("keys: " + keys);
        if (keys == null || keys.isEmpty()) {
            log.warn("No keys found in Redis");
        } else {
            log.info("Total Redis keys found: {}", keys.size());
        }

        return keys;
    }  // * matches all keys

}
