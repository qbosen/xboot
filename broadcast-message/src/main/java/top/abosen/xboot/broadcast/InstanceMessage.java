package top.abosen.xboot.broadcast;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qiubaisen
 * @since 2023/3/14
 */


@BroadcastMessage({})
@Getter
@Setter
public abstract class InstanceMessage {
    String type;
    String instanceId;
}
