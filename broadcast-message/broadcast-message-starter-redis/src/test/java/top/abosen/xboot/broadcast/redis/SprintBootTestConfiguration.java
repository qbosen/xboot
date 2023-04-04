package top.abosen.xboot.broadcast.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * @author qiubaisen
 * @since 2023/3/31
 */
@Configuration(proxyBeanMethods = false)
public class SprintBootTestConfiguration {
    @Bean
    RedisConnectionFactory connectionFactory() {
        return Mockito.mock(RedisConnectionFactory.class);
    }

    @Bean
    StringRedisTemplate stringRedisTemplate() {
        return Mockito.mock(StringRedisTemplate.class);
    }

    @Bean(name = BroadcastRedisAutoConfiguration.REDIS_BROADCAST_MESSAGE_LISTENER_CONTAINER)
    RedisMessageListenerContainer listenerContainer() {
        return Mockito.mock(RedisMessageListenerContainer.class);
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
