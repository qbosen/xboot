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
public class FooCacheService {
    final BroadcastMessagePublisher publisher;

    public void clearFooCache(long id) {
        publishFooClearMessage(id);
        doClearFooCache(id);
    }

    private void publishFooClearMessage(long id) {
        publisher.publish(new FooMessage(id));
    }

    public void doClearFooCache(long id) {
        log.info("do clear foo cache: {}", id);
    }
}
