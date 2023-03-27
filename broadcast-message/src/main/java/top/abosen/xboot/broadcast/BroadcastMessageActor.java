package top.abosen.xboot.broadcast;

/**
 * 消息中间件实际处理者
 */
public interface BroadcastMessageActor {
    /**
     * 将消息发送到中间件
     *
     * @param message 广播消息 {"@type": "xxx", ...}
     */
    void publish(String message);

    /**
     * 将来自中间件的消息 转发给 {@link BroadcastMessageCoordinator}
     *
     * @param message 接收消息 {"@type": "xxx", ...}
     */
    void receive(String message);
}
