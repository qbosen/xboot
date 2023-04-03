package top.abosen.xboot.broadcast;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qiubaisen
 * @since 2023/3/29
 */

@RequiredArgsConstructor
@Slf4j
public class BroadcastMessagePublisherImpl implements BroadcastMessagePublisher {
    final BroadcastInstanceContext context;
    final ObjectMapper objectMapper;
    final BroadcastMessageMiddlewarePublisher realPublisher;


    @SneakyThrows
    @Override
    public void publish(InstanceMessage message) {
        message.setInstanceId(context.getInstanceId());
        if (context.isBroadcasting()) {
            log.debug("广播中,跳过消息:{}", message);
            return;
        }
        forcePublish(message);
    }

    @SneakyThrows
    @Override
    public void forcePublish(InstanceMessage message) {
        message.setInstanceId(context.getInstanceId());
        String messageStr = objectMapper.writeValueAsString(message);
        log.debug("广播消息:{}", messageStr);
        realPublisher.publish(messageStr);
    }


}
