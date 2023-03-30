package top.abosen.xboot.broadcast;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * @author qiubaisen
 * @since 2023/3/29
 */

@RequiredArgsConstructor
public class BroadcastMessagePublisherImpl implements BroadcastMessagePublisher{
    final BroadcastInstanceContext context;
    final ObjectMapper objectMapper;
    final BroadcastMessageMiddlewarePublisher realPublisher;


    @SneakyThrows
    @Override
    public void publish(InstanceMessage message) {
        if (context.isBroadcasting()) {
            return;
        }
        message.setInstanceId(context.getInstanceId());
        String messageStr = objectMapper.writeValueAsString(message);
        realPublisher.publish(messageStr);
    }
}
