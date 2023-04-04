package top.abosen.xboot.broadcast.demo.simple;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.abosen.xboot.broadcast.BroadcastMessagePublisher;

/**
 * @author qiubaisen
 * @since 2023/4/3
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SimpleCacheService {
    final BroadcastMessagePublisher publisher;

    public void clearUserCache(long id) {
        broadcastMessage(id);
        doClearCache(id);
    }

    private void broadcastMessage(long id) {
        publisher.publish(new SimpleMessage(id));
    }

    private void doClearCache(long id) {
        log.info("do clear simple cache: {}", id);
    }
}
