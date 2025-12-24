package com.sipl.ticket.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RedisCacheConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Value("${spring.redis.auth.enabled}")
    private boolean redisAuthEnabled;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        log.info("Creating JedisConnectionFactory instance for redis cache");
        RedisStandaloneConfiguration redisConfiguration =
                new RedisStandaloneConfiguration(redisHost, redisPort);

        if (redisAuthEnabled) {
            redisConfiguration.setPassword(redisPassword);
        }

        log.info("Created JedisConnectionFactory instance");
        return new JedisConnectionFactory(redisConfiguration);
    }

    /**
     * RedisTemplate for token (key = String, value = String)
     */
    @Bean
    public RedisTemplate<String, String> redisTemplateForToken(JedisConnectionFactory jedisConnectionFactory) {
        log.info("Creating RedisTemplate<String, String> for token cache");
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());
        log.info("Created RedisTemplate<String, String>");
        return template;
    }

    /**
     * RedisTemplate for general objects (JSON serialization)
     */
    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        log.info("Creating RedisTemplate<String, Object> for JSON serialization");
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory);

        // Key serializer
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // JSON serializer for values
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);

        log.info("Created RedisTemplate<String, Object> with JSON serialization");
        return template;
    }
}
