package com.demo.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhuLiang
 */
@Service
public class RedisBatch {

    final static long START_USER_ID = 4611686027042922274L;

    final static int[] SIZE_LIST = new int[]{1, 2, 3, 4, 5, 10, 50, 100};

    final static UseType[] USE_TYPE_LIST = new UseType[]{UseType.useTemplate};

    enum UseType {
        //
        useTemplate("RedisTemplate"),

        ;

        UseType(String text) {
            this.text = text;
        }

        public final String text;
    }

    private final UseTemplate useTemplate;

    public RedisBatch(UseTemplate useTemplate) {
        this.useTemplate = useTemplate;
    }


    @PostConstruct
    public void work() {
        for (UseType useType : USE_TYPE_LIST) {
            doByUseType(100, useType);
        }
    }

    public void doByUseType(int count, UseType useType) {
        System.out.println("----------" + useType.text + "----------");
        for (int size : SIZE_LIST) {
            long start = System.currentTimeMillis();
            doByCount(count, size, useType);
            long end = System.currentTimeMillis();

            long useTime = end - start;

            StringBuffer buffer = new StringBuffer();
            buffer.append("查询次数：" + count)
                    .append("Keys数量：" + size)
                    .append("使用时长：" + useTime)
                    .append("平均时长：" + useTime / count);
            System.out.println(buffer);
        }
    }

    public void doByCount(int count, int size, UseType useType) {
        for (int i = 0; i < count; i++) {
            batchGet(size, useType);
        }
    }

    public void batchGet(int size, UseType useType) {
        List<String> keys = getKeys(size);
        switch (useType) {
            case useTemplate:
                useTemplate.batchGet(keys);
                break;
            default:
                break;
        }
    }


    public List<String> getKeys(int size) {
        List<String> keys = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            long userId = START_USER_ID + i;
            String key = "online:info:" + userId;
            keys.add(key);
        }
        return keys;

    }
}
