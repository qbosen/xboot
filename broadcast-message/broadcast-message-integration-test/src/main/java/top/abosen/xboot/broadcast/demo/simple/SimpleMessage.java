package top.abosen.xboot.broadcast.demo.simple;

import lombok.Value;
import top.abosen.xboot.broadcast.BroadcastMessage;
import top.abosen.xboot.broadcast.InstanceMessage;

/**
 * @author qiubaisen
 * @since 2023/4/3
 */
@BroadcastMessage("simple")
@Value
public class SimpleMessage extends InstanceMessage {
    long id;
}
