package top.abosen.xboot.broadcast;

/**
 *
 * @author qiubaisen
 * @since 2023/3/29
 */
public interface BroadcastMessageForwarder {

    /**
     * 接收来自中间件的消息, 转换为具体消息类型并转发给 {@link BroadcastMessageListener}
     *
     * @param message 接收消息
     */
    void forward(String message);
}
