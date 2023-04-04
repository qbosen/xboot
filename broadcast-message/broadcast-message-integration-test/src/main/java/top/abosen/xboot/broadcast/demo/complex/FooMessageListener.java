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
public class FooMessageListener implements BroadcastMessageListener<FooMessage> {
    final FooCacheService fooCacheService;
    final BroadcastMessagePublisher messagePublisher;

    @Override
    public Class<FooMessage> type() {
        return FooMessage.class;
    }

    @Override
    public void onMessage(FooMessage message) {
        log.info("receive message:{}", message);
        fooCacheService.doClearFooCache(message.getId());
        // force 可在广播过程中强制发送广播消息
        messagePublisher.forcePublish(new BarMessage(message.getId()));
    }
}
