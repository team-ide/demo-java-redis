package com.demo.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * @author ZhuLiang
 */
@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    /**
     * 声明一个Bean 这样其他地方可以通过@Autowired获取到生成的 RedisTemplate 对象
     *
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        // 配置连接工厂
        template.setConnectionFactory(factory);
        //设置键值默认序列化方式
        //RedisTemplate有自己的默认序列化的方式，不过使用默认方式，会在redis客户端查看的时候出现乱码，不便与使用，我们这里用falstjson库
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        template.setDefaultSerializer(stringSerializer);

        return template;
    }
}
