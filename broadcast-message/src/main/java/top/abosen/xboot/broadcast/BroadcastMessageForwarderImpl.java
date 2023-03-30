package top.abosen.xboot.broadcast;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

/**
 * @author qiubaisen
 * @since 2023/3/13
 */

@Slf4j
@RequiredArgsConstructor
public class BroadcastMessageForwarderImpl implements BroadcastMessageForwarder {
    final BroadcastInstanceContext context;
    final ObjectMapper objectMapper;
    final List<BroadcastMessageListener<InstanceMessage>> messageListeners;


    @SneakyThrows
    @Override
    public void forward(String message) {
        InstanceMessage instanceMessage = objectMapper.readValue(message, InstanceMessage.class);
        if (context.getInstanceId().equals(instanceMessage.getInstanceId())) {
            log.debug("[broadcast] 本机消息,跳过");
            return;
        }

        messageListeners.stream()
                .filter(it -> Objects.equals(it.type(), instanceMessage.getClass()))
                .forEach(it -> context.broadcast(
                        () -> it.onMessage(instanceMessage)
                ));
    }

}
