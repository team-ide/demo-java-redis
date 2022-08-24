package com.demo.redis.service;

import com.teamide.metric.Metric;
import com.teamide.metric.MetricData;
import jodd.util.StringUtil;
import org.redisson.api.RBatch;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ZhuLiang
 */
//@Service
public class RedisBatch {

    final static long START_USER_ID = 4611686027042922274L;

    final static int[] SIZE_LIST = new int[]{1, 2, 3, 4, 5, 10, 50, 100};

    final static UseType[] USE_TYPE_LIST = new UseType[]{
            UseType.useSpringLettucePipeLine,
            UseType.useSpringLettucePoolPipeLine,
            UseType.useSpringRedissonPipeLine,
            UseType.useRedisson,
    };


    @Resource(name = "executor")
    private ThreadPoolTaskExecutor executor;
    @Resource(name = "redissonClient")
    private RedissonClient redissonClient;
    @Resource(name = "springLettuceTemplate")
    private RedisTemplate<String, String> springLettuceTemplate;
    @Resource(name = "springLettucePoolTemplate")
    private RedisTemplate<String, String> springLettucePoolTemplate;
    @Resource(name = "springRedissonTemplate")
    private RedisTemplate<String, String> springRedissonTemplate;

    public void testGet(List<String> keys) {
        System.out.println("test redissonClient string value");
        List<String> res = batchGet(keys, redissonClient);
        System.out.println("test redissonClient string value " + res.size());
//        for (String v : res) {
//            System.out.println(v);
//        }

        System.out.println("test springLettuceTemplate string value");
        res = pipelinedBatchGet(keys, springLettuceTemplate);
        System.out.println("test springLettuceTemplate string value " + res.size());
//        for (String v : res) {
//            System.out.println(v);
//        }

        System.out.println("test springLettucePoolTemplate string value");
        res = pipelinedBatchGet(keys, springLettucePoolTemplate);
        System.out.println("test springLettucePoolTemplate string value " + res.size());
//        for (String v : res) {
//            System.out.println(v);
//        }

        System.out.println("test springRedissonTemplate string value");
        res = pipelinedBatchGet(keys, springRedissonTemplate);
        System.out.println("test springRedissonTemplate string value " + res.size());
//        for (String v : res) {
//            System.out.println(v);
//        }
    }

    public void testGetHash(List<String> keys) {
        System.out.println("test redissonClient hash value");
        List<String> res = batchHashGet(keys, redissonClient);
        System.out.println("test redissonClient hash value " + res.size());
//        for (String v : res) {
//            System.out.println(v);
//        }

        System.out.println("test springLettuceTemplate hash value");
        res = pipelinedBatchHashGet(keys, springLettuceTemplate);
        System.out.println("test springLettuceTemplate hash value " + res.size());
//        for (String v : res) {
//            System.out.println(v);
//        }

        System.out.println("test springLettucePoolTemplate hash value");
        res = pipelinedBatchHashGet(keys, springLettucePoolTemplate);
        System.out.println("test springLettucePoolTemplate hash value " + res.size());
//        for (String v : res) {
//            System.out.println(v);
//        }

        System.out.println("test springRedissonTemplate hash value");
        res = pipelinedBatchHashGet(keys, springRedissonTemplate);
        System.out.println("test springRedissonTemplate hash value " + res.size());
//        for (String v : res) {
//            System.out.println(v);
//        }
    }

    @PostConstruct
    public void work() throws Exception {

        List<String> keys = getUserKeys(2);
        testGet(keys);

        keys = getOnlineKeys(2);
        testGetHash(keys);


        int threadCount = 100;
        if (StringUtil.isNotEmpty(System.getProperty("thread"))) {
            threadCount = Integer.valueOf(System.getProperty("thread"));
        }
        long sec = 10L;
        if (StringUtil.isNotEmpty(System.getProperty("time"))) {
            sec = Long.valueOf(System.getProperty("time"));
        }


        for (UseType useType : USE_TYPE_LIST) {
            String name = useType.text;

            Metric metric = new Metric(name);

            keys = getUserKeys(2);
            long needStopTime = System.currentTimeMillis() + 1000L * 1;
            doByCount(needStopTime, threadCount, keys, false, useType, metric);

            keys = getOnlineKeys(2);
            needStopTime = System.currentTimeMillis() + 1000L * 1;
            doByCount(needStopTime, threadCount, keys, true, useType, metric);
        }

        for (int size : SIZE_LIST) {
            keys = getUserKeys(size);
            System.out.println("\n### " + "批量查询String值，[" + threadCount + "]个线程，查询[" + sec + "]秒Redis，每次查询[" + size + "]个Key\n");
            Metric.outHeader();

            for (UseType useType : USE_TYPE_LIST) {
                for (int i = 1; i <= 5; i++) {
                    String name = useType.text + "(" + i + ")";
                    Metric metric = new Metric(name);
                    metric.start();
                    long needStopTime = System.currentTimeMillis() + 1000L * sec;
                    doByCount(needStopTime, threadCount, keys, false, useType, metric);
                    metric.stop();
                    metric.out();
                }
            }
        }

        for (int size : SIZE_LIST) {
            keys = getOnlineKeys(size);
            System.out.println("\n### " + "批量查询Hash值，[" + threadCount + "]个线程，查询[" + sec + "]秒Redis，每次查询[" + size + "]个Key\n");
            Metric.outHeader();
            for (UseType useType : USE_TYPE_LIST) {
                for (int i = 1; i <= 5; i++) {
                    String name = useType.text + "(" + i + ")";
                    Metric metric = new Metric(name);
                    metric.start();
                    long needStopTime = System.currentTimeMillis() + 1000L * sec;
                    doByCount(needStopTime, threadCount, keys, true, useType, metric);
                    metric.stop();
                    metric.out();
                }
            }
        }
    }


    public void doByCount(long needStopTime, int threadCount, List<String> keys, boolean isHash, UseType useType, Metric metric) throws Exception {

        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for (int threadIndex = 0; threadIndex < threadCount; threadIndex++) {
            new Thread(() -> {
                while (true) {
                    if (System.currentTimeMillis() >= needStopTime) {
                        break;
                    }
                    MetricData metricData = metric.startTask();
                    batchGet(keys, isHash, useType);
                    metric.endTask(metricData);
                }
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
    }

    public void batchGet(List<String> keys, boolean isHash, UseType useType) {
        switch (useType) {
            case useSpringLettucePipeLine:
                if (isHash) {
                    pipelinedBatchHashGet(keys, springLettuceTemplate);
                } else {
                    pipelinedBatchGet(keys, springLettuceTemplate);
                }
                break;
            case useSpringLettucePoolPipeLine:
                if (isHash) {
                    pipelinedBatchHashGet(keys, springLettucePoolTemplate);
                } else {
                    pipelinedBatchGet(keys, springLettucePoolTemplate);
                }
                break;
            case useSpringRedissonPipeLine:
                if (isHash) {
                    pipelinedBatchHashGet(keys, springRedissonTemplate);
                } else {
                    pipelinedBatchGet(keys, springRedissonTemplate);
                }
                break;
            case useRedisson:
                if (isHash) {
                    batchHashGet(keys, redissonClient);
                } else {
                    batchGet(keys, redissonClient);
                }
                break;
            default:
                break;
        }
    }


    public List<String> getUserKeys(int size) {
        List<String> keys = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            long userId = START_USER_ID + i;
            String key = "userKey-" + userId + "-0";
            keys.add(key);
        }
        return keys;
    }

    public List<String> getOnlineKeys(int size) {
        List<String> keys = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            long userId = START_USER_ID + i;
            String key = "online:info:" + userId;
            keys.add(key);
        }
        return keys;
    }


    public List<String> pipelinedBatchHashGet(List<String> keys, RedisTemplate<String, String> redisTemplate) {
        List<Object> res = redisTemplate.executePipelined((RedisConnection connection) -> {
            for (String key : keys) {
                connection.hGetAll(key.getBytes());
            }
            return null;
        });


        List<String> list = new ArrayList<>();
        for (Object object : res) {
            Map<?, ?> map = (Map) object;
            for (Object v : map.values()) {
                list.add(v.toString());
            }
        }
        return list;
    }


    public List<String> pipelinedBatchGet(List<String> keys, RedisTemplate<String, String> redisTemplate) {
        List<Object> res = redisTemplate.executePipelined((RedisConnection connection) -> {
            for (String key : keys) {
                connection.get(key.getBytes());
            }
            return null;
        });

        List<String> list = new ArrayList<>();
        for (Object object : res) {
            list.add(object.toString());
        }
        return list;
    }


    public List<String> batchHashGet(List<String> keys, RedissonClient redissonClient) {
        RBatch rBatch = redissonClient.createBatch();
        for (String key : keys) {
            rBatch.getMap(key).readAllMapAsync();
        }
        List<?> res = rBatch.execute().getResponses();

        List<String> list = new ArrayList<>();
        for (Object object : res) {
            Map<?, ?> map = (Map) object;
            for (Object v : map.values()) {
                list.add(v.toString());
            }
        }
        return list;
    }


    public List<String> batchGet(List<String> keys, RedissonClient redissonClient) {

        RBatch rBatch = redissonClient.createBatch();
        for (String key : keys) {
            rBatch.getBucket(key).getAsync();
        }
        List<?> res = rBatch.execute().getResponses();

        List<String> list = new ArrayList<>();
        for (Object object : res) {
            list.add(object.toString());
        }
        return list;
    }
}
