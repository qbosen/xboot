package top.abosen.xboot.broadcast.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import top.abosen.xboot.broadcast.BroadcastMessageMiddlewarePublisher;

/**
 * @author qiubaisen
 * @since 2023/3/29
 */


@RequiredArgsConstructor
public class BroadcastMessageRedisActor implements BroadcastMessageMiddlewarePublisher {
    final StringRedisTemplate redisTemplate;
    final String topic;

    @Override
    public void publish(String message) {
        redisTemplate.convertAndSend(topic, message);
    }


}
