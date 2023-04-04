package top.abosen.xboot.broadcast.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import top.abosen.xboot.broadcast.BroadcastMessageMiddlewareListener;
import top.abosen.xboot.broadcast.BroadcastMessageForwarder;

/**
 * @author qiubaisen
 * @since 2023/3/29
 */

@RequiredArgsConstructor
public class BroadcastMessageRedisListener implements MessageListener, BroadcastMessageMiddlewareListener {
    final BroadcastMessageForwarder messageReceiver;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        this.receive(message.toString());
    }

    @Override
    public void receive(String message) {
        messageReceiver.forward(message);
    }
}
