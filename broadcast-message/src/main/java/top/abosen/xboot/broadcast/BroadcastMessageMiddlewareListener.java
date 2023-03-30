package top.abosen.xboot.broadcast;

/**
 * @author qiubaisen
 * @since 2023/3/29
 */
public interface BroadcastMessageMiddlewareListener {

    /**
     * 将来自中间件的消息 转发给 {@link BroadcastMessageForwarder}
     *
     * @param message 接收消息 {"@type": "xxx", ...}
     */
    void receive(String message);
}
