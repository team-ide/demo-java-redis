package com.demo.redis.configuration;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author ZhuLiang
 */
@Configuration
public class LettucePoolTemplateConfig {

    @Value("${redis.host}")
    private String redisHost;
    @Value("${redis.port}")
    private int redisPort;
    @Value("${redis.password}")
    private String redisPassword;


    @Bean
    public GenericObjectPoolConfig genericObjectPoolConfig() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMinIdle(10);
        poolConfig.setMaxIdle(100);
        poolConfig.setMaxTotal(100);
        return poolConfig;
    }

    @Bean
    public LettuceClientConfiguration lettuceClientConfiguration(GenericObjectPoolConfig genericObjectPoolConfig) {
        return LettucePoolingClientConfiguration.builder()
                .poolConfig(genericObjectPoolConfig)
                .build();
    }

    @Bean
    public RedisStandaloneConfiguration redisSentinelConfiguration() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisHost, redisPort);
        redisStandaloneConfiguration.setPassword(redisPassword);
        return redisStandaloneConfiguration;
    }


    @Bean(name = "poolLettuceConnectionFactory", destroyMethod = "destroy")
    public LettuceConnectionFactory poolLettuceConnectionFactory(RedisStandaloneConfiguration redisSentinelConfiguration, LettuceClientConfiguration lettuceClientConfiguration) {
        return new LettuceConnectionFactory(redisSentinelConfiguration, lettuceClientConfiguration);
    }

    @Bean(name = "springLettucePoolTemplate")
    public RedisTemplate<String, String> springLettucePoolTemplate(LettuceConnectionFactory poolLettuceConnectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        // ??????????????????
        template.setConnectionFactory(poolLettuceConnectionFactory);
        //?????????????????????????????????
        //RedisTemplate????????????????????????????????????????????????????????????????????????redis????????????????????????????????????????????????????????????????????????falstjson???
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        template.setDefaultSerializer(stringSerializer);

        return template;
    }

}
