package top.abosen.xboot.broadcast.demo.simple;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.abosen.xboot.broadcast.BroadcastMessageListener;

/**
 * @author qiubaisen
 * @since 2023/4/3
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SimpleMessageListener implements BroadcastMessageListener<SimpleMessage> {
    final SimpleCacheService simpleCacheService;

    @Override
    public Class<SimpleMessage> type() {
        return SimpleMessage.class;
    }

    @Override
    public void onMessage(SimpleMessage message) {
        log.info("receive message:{}", message);
        simpleCacheService.clearUserCache(message.getId());
    }
}
