package top.abosen.xboot.broadcast.demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.abosen.xboot.broadcast.redis.BroadcastRedisProperties;

/**
 * @author qiubaisen
 * @since 2023/3/31
 */

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class BroadcastDemoApplication implements ApplicationRunner {
    final BroadcastRedisProperties properties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Run Instance [{}] on topic {}", properties.getInstanceId(), properties.getTopic());
    }

    public static void main(String[] args) {
        SpringApplication.run(BroadcastDemoApplication.class, args);
    }
}
