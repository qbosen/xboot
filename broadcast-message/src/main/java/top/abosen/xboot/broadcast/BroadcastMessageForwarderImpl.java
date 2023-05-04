package top.abosen.xboot.broadcast;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author qiubaisen
 * @since 2023/3/13
 */

@SuppressWarnings("unchecked")
@Slf4j
public class BroadcastMessageForwarderImpl implements BroadcastMessageForwarder {
    final BroadcastInstanceContext context;
    final ObjectMapper objectMapper;
    final List<BroadcastMessageListener<InstanceMessage>> messageListeners;

    public BroadcastMessageForwarderImpl(
            BroadcastInstanceContext context, ObjectMapper objectMapper,
            List<BroadcastMessageListener<? extends InstanceMessage>> messageListeners) {
        this.context = context;
        this.objectMapper = objectMapper;
        this.messageListeners = messageListeners.stream().map(it -> ((BroadcastMessageListener<InstanceMessage>) it)).collect(Collectors.toList());

        log.info("注册广播消息处理器:{}", messageListeners.stream()
                .map(it -> it.type().getSimpleName() + ":" + it.getClass().getSimpleName()).collect(Collectors.joining(",")));

    }

    @SneakyThrows
    @Override
    public void forward(String message) {
        InstanceMessage instanceMessage = objectMapper.readValue(message, InstanceMessage.class);
        boolean fromOtherInstance = !context.getInstanceId().equals(instanceMessage.getInstanceId());

        log.debug("[broadcast] 处理广播消息:{}", message);

        messageListeners.stream()
                .filter(it -> Objects.equals(it.type(), instanceMessage.getClass()))
                .filter(it -> fromOtherInstance || it.handleLocalInstanceMessage())
                .forEach(it -> context.broadcast(
                        () -> it.onMessage(instanceMessage)
                ));
    }

}
