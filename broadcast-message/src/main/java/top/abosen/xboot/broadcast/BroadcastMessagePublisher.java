package top.abosen.xboot.broadcast;

import lombok.SneakyThrows;

/**
 * @author qiubaisen
 * @since 2023/3/29
 */
public interface BroadcastMessagePublisher {
    /**
     * 广播消息
     *
     * @param message 广播消息 {"@type": "xxx", ...}
     */
    void publish(InstanceMessage message);

    void forcePublish(InstanceMessage message);
}
