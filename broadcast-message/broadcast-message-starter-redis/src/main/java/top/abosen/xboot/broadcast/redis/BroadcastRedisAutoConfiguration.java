package top.abosen.xboot.broadcast.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import top.abosen.xboot.broadcast.*;

import java.util.List;

/**
 * 外部需要有 {@link ObjectMapper} {@link StringRedisTemplate} 的相关bean
 *
 * @author qiubaisen
 * @since 2023/3/29
 */

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(BroadcastRedisProperties.class)
@ConditionalOnProperty(value = BroadcastRedisProperties.TOPIC)
@ConditionalOnBean(value = {ObjectMapper.class, RedisConnectionFactory.class})
public class BroadcastRedisAutoConfiguration {

    public static final String REDIS_BROADCAST_MESSAGE_LISTENER_CONTAINER = "REDIS_BROADCAST_MESSAGE_LISTENER_CONTAINER";

    @Bean
    @ConditionalOnMissingBean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public BroadcastMessageMiddlewarePublisher redisPublisher(
            BroadcastRedisProperties properties,
            StringRedisTemplate redisTemplate) {
        return (message -> redisTemplate.convertAndSend(properties.getTopic(), message));
    }


    /**
     * {@link MessageListener } {@link BroadcastMessageMiddlewareListener}
     */
    @Bean
    @ConditionalOnMissingBean
    public BroadcastMessageRedisListener redisReceiver(BroadcastMessageForwarder receiver) {
        return new BroadcastMessageRedisListener(receiver);
    }

    @Bean
    @ConditionalOnMissingBean
    public BroadcastInstanceContext broadcastInstanceContext(BroadcastRedisProperties properties) {
        return new BroadcastInstanceContext(properties.getInstanceId());
    }

    @Bean
    @ConditionalOnMissingBean
    public BroadcastMessageForwarder messageForwarder(
            BroadcastInstanceContext context, ObjectMapper objectMapper,
            List<BroadcastMessageListener<InstanceMessage>> listeners) {
        return new BroadcastMessageForwarderImpl(context, objectMapper, listeners);
    }

    @Bean
    @ConditionalOnMissingBean
    public BroadcastMessagePublisher messagePublisher(
            BroadcastInstanceContext context, ObjectMapper objectMapper,
            BroadcastMessageMiddlewarePublisher redisPublisher) {
        return new BroadcastMessagePublisherImpl(context, objectMapper, redisPublisher);
    }

    @Bean(name = REDIS_BROADCAST_MESSAGE_LISTENER_CONTAINER)
    @ConditionalOnMissingBean(name = REDIS_BROADCAST_MESSAGE_LISTENER_CONTAINER)
    public RedisMessageListenerContainer container(
            RedisConnectionFactory connectionFactory,
            BroadcastMessageRedisListener redisListener,
            BroadcastRedisProperties properties) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(redisListener, new ChannelTopic(properties.getTopic()));
        return container;
    }
}
