package top.abosen.xboot.broadcast;

/**
 * @author qiubaisen
 * @since 2023/3/29
 */
public interface BroadcastMessageMiddlewarePublisher {
    /**
     * 将消息发送到中间件
     *
     * @param message 广播消息 {"@type": "xxx", ...}
     */
    void publish(String message);
}
