package top.abosen.xboot.broadcast;

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
}
