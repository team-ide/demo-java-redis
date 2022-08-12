package com.demo.redis;

import com.demo.redis.service.RedisBatch;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.redisson.api.RedissonClient;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author ZhuLiang
 */
@BenchmarkMode(Mode.All)
@OutputTimeUnit(TimeUnit.SECONDS)
//预热 预热次数，默认是每次运行1秒，运行10次
@Warmup(iterations = 2)
//配置执行次数，本例是一次运行5秒，总共运行3次。在性能对比时候，采用默认1秒即可，如果我们用jvisualvm做性能监控，我们可以指定一个较长时间运行
@Measurement(iterations = 3,time = 5,timeUnit = TimeUnit.SECONDS)
// 配置同时起多少个线程执行，默认值世 Runtime.getRuntime().availableProcessors()，本例启动1个线程同时执行
@Threads(1)
//代表启动多个单独的进程分别测试每个方法，我们这里指定为每个方法启动一个进程
@Fork(1) //fork 1 个线程
@State(Scope.Thread) //每个测试线程一个实例
public class ServerBenchmark {

    public static void main(String[] args) throws Exception {
        // 使用一个单独进程执行测试，执行5遍warmup，然后执行5遍测试
        Options opt = new OptionsBuilder()
                .include(ServerBenchmark.class.getSimpleName())
                .output("./jmh-map.log") // 输出测试结果的文件
                .build();
        new Runner(opt).run();
    }

    private ConfigurableApplicationContext context;

    private RedisBatch redisBatch;
    public RedissonClient redissonClient;
    public RedisTemplate<String, String> springLettuceTemplate;
    public RedisTemplate<String, String> springLettucePoolTemplate;
    public RedisTemplate<String, String> springRedissonTemplate;

    @Setup
    public void init() {

        context = SpringApplication.run(ServerApplication.class, new String[]{});
        redisBatch = context.getBean(RedisBatch.class);
        redissonClient = context.getBean("redissonClient", RedissonClient.class);
        springLettuceTemplate = context.getBean("springLettuceTemplate", RedisTemplate.class);
        springLettucePoolTemplate = context.getBean("springLettucePoolTemplate", RedisTemplate.class);
        springRedissonTemplate = context.getBean("springRedissonTemplate", RedisTemplate.class);
    }

    @TearDown
    public void down() {
        context.close();
    }

    @Benchmark
    public void springLettuceTemplateUserKeys1() {
        List<String> keys = redisBatch.getUserKeys(1);
        redisBatch.pipelinedBatchGet(keys, springLettuceTemplate);
    }

    @Benchmark
    public void springLettuceTemplateUserKeys2() {
        List<String> keys = redisBatch.getUserKeys(2);
        redisBatch.pipelinedBatchGet(keys, springLettuceTemplate);
    }

    @Benchmark
    public void springLettuceTemplateUserKeys5() {
        List<String> keys = redisBatch.getUserKeys(5);
        redisBatch.pipelinedBatchGet(keys, springLettuceTemplate);
    }

    @Benchmark
    public void springLettuceTemplateUserKeys10() {
        List<String> keys = redisBatch.getUserKeys(10);
        redisBatch.pipelinedBatchGet(keys, springLettuceTemplate);
    }

    @Benchmark
    public void springLettuceTemplateUserKeys100() {
        List<String> keys = redisBatch.getUserKeys(100);
        redisBatch.pipelinedBatchGet(keys, springLettuceTemplate);
    }

    @Benchmark
    public void springRedissonTemplateUserKeys1() {
        List<String> keys = redisBatch.getUserKeys(1);
        redisBatch.pipelinedBatchGet(keys, springRedissonTemplate);
    }

    @Benchmark
    public void springRedissonTemplateUserKeys2() {
        List<String> keys = redisBatch.getUserKeys(2);
        redisBatch.pipelinedBatchGet(keys, springRedissonTemplate);
    }

    @Benchmark
    public void springRedissonTemplateUserKeys5() {
        List<String> keys = redisBatch.getUserKeys(5);
        redisBatch.pipelinedBatchGet(keys, springRedissonTemplate);
    }

    @Benchmark
    public void springRedissonTemplateUserKeys10() {
        List<String> keys = redisBatch.getUserKeys(10);
        redisBatch.pipelinedBatchGet(keys, springRedissonTemplate);
    }

    @Benchmark
    public void springRedissonTemplateUserKeys100() {
        List<String> keys = redisBatch.getUserKeys(100);
        redisBatch.pipelinedBatchGet(keys, springRedissonTemplate);
    }

}
