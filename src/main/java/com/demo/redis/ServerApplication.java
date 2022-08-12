package com.demo.redis;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;


/**
 * @author ZhuLiang
 */
@SpringBootApplication(exclude = {
        RedisReactiveAutoConfiguration.class,
        RedisAutoConfiguration.class,
        RedisRepositoriesAutoConfiguration.class,
})
public class ServerApplication {

    public static void main(String[] args) throws Exception {
        // 使用一个单独进程执行测试，执行5遍warmup，然后执行5遍测试
//        Options opt = new OptionsBuilder()
//                .include(ServerBenchmark.class.getSimpleName())
//                // 输出测试结果的文件
//                .output("./jmh-map.log")
//                .build();
//        new Runner(opt).run();

        ConfigurableApplicationContext context = SpringApplication.run(ServerApplication.class, args);
        context.stop();
        System.exit(0);
    }

}
