package top.abosen.xboot.broadcast;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

/**
 * @author qiubaisen
 * @since 2023/3/14
 */
class InstanceMessageTest {
    @Getter
    @Setter
    @BroadcastMessage({})
    static class NewUser extends InstanceMessage{
        Long userId;
    }



}