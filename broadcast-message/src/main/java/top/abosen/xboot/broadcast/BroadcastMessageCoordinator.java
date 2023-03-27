package top.abosen.xboot.broadcast;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author qiubaisen
 * @since 2023/3/13
 */

@Slf4j
public class BroadcastMessageCoordinator {
    final BroadcastInstanceContext context;
    final BroadcastMessageActor messagePublisher;
    final Map<Class<? extends InstanceMessage>, BroadcastMessageCallback<? extends InstanceMessage>> messageReceivers;
    final ObjectMapper objectMapper;

    public BroadcastMessageCoordinator(BroadcastInstanceContext context, BroadcastMessageActor messagePublisher,
                                       List<BroadcastMessageCallback<?>> messageReceivers, ObjectMapper objectMapper) {
        this.context = context;
        this.messagePublisher = messagePublisher;
        this.messageReceivers = messageReceivers.stream().collect(Collectors.toMap(BroadcastMessageCallback::type, it -> it));
        this.objectMapper = objectMapper;
    }


    @SneakyThrows
    public void publish(InstanceMessage message) {
        if (context.isBroadcasting()) {
            return;
        }
        message.setInstanceId(context.getInstanceId());
        String messageStr = objectMapper.writeValueAsString(message);
        messagePublisher.publish(messageStr);
    }

    @SneakyThrows
    public void receive(String message) {
        InstanceMessage instanceMessage = objectMapper.readValue(message, InstanceMessage.class);
        if (context.getInstanceId().equals(instanceMessage.getInstanceId())) {
            log.debug("[broadcast] 本机消息,跳过");
            return;
        }

        if (!messageReceivers.containsKey(instanceMessage.getClass())) {
            log.warn("[broadcast] 没有对应的处理器, 跳过:{}", message);
            return;
        }

        //noinspection unchecked
        ((BroadcastMessageCallback<InstanceMessage>) messageReceivers.get(instanceMessage.getClass()))
                .onMessage(instanceMessage);
    }

}
