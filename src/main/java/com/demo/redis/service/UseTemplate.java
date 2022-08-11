package com.demo.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author ZhuLiang
 */
@Service
public class UseTemplate {

    private final RedisTemplate<String, String> redisTemplate;

    public UseTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @PostConstruct
    public void init() {
        redisTemplate.opsForValue().get("_");
    }

    public void batchGet(List<String> keys) {
        for (String key : keys) {
            redisTemplate.opsForValue().get(key);
        }
    }
}
