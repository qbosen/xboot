package top.abosen.xboot.broadcast.demo.complex;

import lombok.Value;
import top.abosen.xboot.broadcast.BroadcastMessage;
import top.abosen.xboot.broadcast.InstanceMessage;

/**
 * @author qiubaisen
 * @since 2023/4/3
 */
@BroadcastMessage("foo")
@Value
public class FooMessage extends InstanceMessage {
    long id;
}
