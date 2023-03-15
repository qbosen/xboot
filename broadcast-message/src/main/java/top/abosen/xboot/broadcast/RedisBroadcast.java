package top.abosen.xboot.broadcast;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

/**
 * @author qiubaisen
 * @since 2023/3/13
 */

@Slf4j
@RequiredArgsConstructor
public class RedisBroadcast implements MessageListener {
    public static final String SPLITTER = "\u0000\u0001";
    final RedisTemplate<String, String> redisTemplate;
    final ChannelTopic topic;
    final BroadcastInstanceContext context;

    //todo polymorphism message
    public void broadcast(String message) {
        if (context.isBroadcasting()) {
            return;
        }
        String instanceMessage = context.getInstanceId() + SPLITTER + message;
        if (log.isDebugEnabled()) {
            log.debug(String.format("[redis broadcast] 向[%s]推送消息[%s]", topic.getTopic(), instanceMessage));
        }
        redisTemplate.convertAndSend(topic.getTopic(), instanceMessage);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("[redis broadcast]收到[%s]的消息[%s]", new String(message.getChannel()), message));
        }
        String[] instancedMsg = message.toString().split(SPLITTER, 2);
        if (context.getInstanceId().equals(instancedMsg[0])) {
            if (log.isDebugEnabled()) {
                log.debug("[redis broadcast] 本机消息,跳过");
            }
            return;
        }

        context.broadcast(
                () -> handleMessage(instancedMsg[1])
        );


    }

    //todo
    private void handleMessage(String message) {

    }
}
