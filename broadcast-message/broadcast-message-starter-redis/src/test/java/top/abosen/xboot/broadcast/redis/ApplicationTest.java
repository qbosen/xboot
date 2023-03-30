package top.abosen.xboot.broadcast.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;

/**
 * @author qiubaisen
 * @since 2023/3/29
 */

@SpringBootTest(classes = {ApplicationTest.MockConfig.class/*note: 先于autoconfiguration */, BroadcastRedisAutoConfiguration.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = "xboot.broadcast.redis.topic=foo")
public class ApplicationTest {
    @Configuration(proxyBeanMethods = false)
    public static class MockConfig {
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

    @Autowired
    BroadcastRedisProperties properties;

    @Test
    void should() {
        assertThat(properties.getTopic()).isEqualTo("foo");
        assertThat(properties.getInstanceId()).isNotBlank();
        assertThatNoException().isThrownBy(() -> {
            UUID uuid = UUID.fromString(properties.getInstanceId());
            assertThat(uuid).isNotNull();
        });

    }


}
