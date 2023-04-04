package top.abosen.xboot.broadcast.demo.complex;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.abosen.xboot.broadcast.BroadcastMessageListener;
import top.abosen.xboot.broadcast.BroadcastMessagePublisher;

/**
 * @author qiubaisen
 * @since 2023/4/3
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BarMessageListener implements BroadcastMessageListener<BarMessage> {
    final BarCacheService barCacheService;

    @Override
    public Class<BarMessage> type() {
        return BarMessage.class;
    }

    @Override
    public void onMessage(BarMessage message) {
        log.info("receive message:{}", message);
        barCacheService.doClearBarCache(message.getFooId());
    }
}
