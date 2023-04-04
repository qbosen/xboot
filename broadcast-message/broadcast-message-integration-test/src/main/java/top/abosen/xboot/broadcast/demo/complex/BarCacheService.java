package top.abosen.xboot.broadcast.demo.complex;

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
public class BarCacheService {
    final BroadcastMessagePublisher publisher;

    public void clearBarCache(long fooId) {
        publishBarClearMessage(fooId);
        doClearBarCache(fooId);
    }

    private void publishBarClearMessage(long fooId) {
        publisher.publish(new BarMessage(fooId));
    }

    public void doClearBarCache(long fooId) {
        log.info("do clear bar cache by fooId: {}", fooId);
    }
}
