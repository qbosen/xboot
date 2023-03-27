package top.abosen.xboot.broadcast;

/**
 * 消息接收后,如果是来自其他实例的消息,则转发到对应的callback处理
 *
 * @param <T> 消息的实际类型
 */

public interface BroadcastMessageCallback<T extends InstanceMessage> {
    /**
     * 消息回调可处理的{@code @type}
     *
     * @return 消息type
     */
    Class<T> type();

    /**
     * 广播消息有特殊的反序列化处理: {@code @type}, 这里是反序列化后的具体消息, 目标只有处理消息接收逻辑
     *
     * @param message 广播消息
     */
    void onMessage(T message);
}
