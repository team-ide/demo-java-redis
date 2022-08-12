package com.demo.redis.service;

/**
 * @author ZhuLiang
 */

public enum UseType {

    //
    useSpringLettucePipeLine("Spring Lettuce 管道"),

    //
    useSpringLettucePoolPipeLine("Spring Lettuce 连接池 管道"),

    //
    useSpringRedissonPipeLine("Spring Redisson 管道"),

    //
    useRedisson("Redisson 管道"),

    ;

    UseType(String text) {
        this.text = text;
    }

    public final String text;
}
