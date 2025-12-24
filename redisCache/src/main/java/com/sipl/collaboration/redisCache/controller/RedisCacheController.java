package com.sipl.collaboration.redisCache.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("api/v1/redis")
@CrossOrigin("*")
@Api(tags = "Redis")
public class RedisCacheController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @DeleteMapping("/cache/deleteProject/{projectPrefix}")
    public String deleteProjectCache(@PathVariable String projectPrefix) {
        Set<String> keys = redisTemplate.keys(projectPrefix + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            return "✅ Deleted all keys for project: " + projectPrefix
                    + " (" + keys.size() + " keys)";
        }
        return "⚠️ No keys found for project: " + projectPrefix;
    }

    @DeleteMapping("/cache/deleteByCacheName/{cacheName}")
    public String deleteByCacheName(@PathVariable String cacheName) {
        Set<String> keys = redisTemplate.keys("*");
        if (keys != null && !keys.isEmpty()) {
            // filter only keys that start with cacheName + "::"
            Set<String> matchedKeys = keys.stream()
                    .filter(k -> k.startsWith(cacheName + "::"))
                    .collect(java.util.stream.Collectors.toSet());

            if (!matchedKeys.isEmpty()) {
                redisTemplate.delete(matchedKeys);
                return "✅ Deleted all keys for cacheName: " + cacheName
                        + " (" + matchedKeys.size() + " keys)";
            } else {
                return "⚠️ No keys found for cacheName: " + cacheName;
            }
        }
        return "⚠️ Redis is empty";
    }

    @GetMapping("/cache/keys")
    public Set<String> getAllKeys() {
        return redisTemplate.keys("*");  // * matches all keys
    }
}
