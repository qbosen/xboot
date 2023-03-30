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
public class BroadcastMessageForwarderImpl implements BroadcastMessageForwarder {
    final BroadcastInstanceContext context;
    final Map<Class<? extends InstanceMessage>, BroadcastMessageListener<? extends InstanceMessage>> messageListeners;
    final ObjectMapper objectMapper;

    public BroadcastMessageForwarderImpl(BroadcastInstanceContext context, ObjectMapper objectMapper,
                                         List<BroadcastMessageListener<?>> messageListeners) {
        this.context = context;
        this.objectMapper = objectMapper;
        this.messageListeners = messageListeners.stream().collect(Collectors.toMap(BroadcastMessageListener::type, it -> it));
    }


    @SneakyThrows
    @Override
    public void forward(String message) {
        InstanceMessage instanceMessage = objectMapper.readValue(message, InstanceMessage.class);
        if (context.getInstanceId().equals(instanceMessage.getInstanceId())) {
            log.debug("[broadcast] 本机消息,跳过");
            return;
        }

        if (!messageListeners.containsKey(instanceMessage.getClass())) {
            log.warn("[broadcast] 没有对应的处理器, 跳过:{}", message);
            return;
        }

        //noinspection unchecked
        ((BroadcastMessageListener<InstanceMessage>) messageListeners.get(instanceMessage.getClass()))
                .onMessage(instanceMessage);
    }

}
