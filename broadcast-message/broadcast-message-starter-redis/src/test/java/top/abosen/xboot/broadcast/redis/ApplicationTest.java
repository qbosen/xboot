package top.abosen.xboot.broadcast.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;

/**
 * @author qiubaisen
 * @since 2023/3/29
 */

@SpringBootTest(classes = {SprintBootTestConfiguration.class/*note: 先于autoconfiguration */, BroadcastRedisAutoConfiguration.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = "xboot.broadcast.redis.topic=foo")
public class ApplicationTest {

    @Autowired
    BroadcastRedisProperties properties;

    @Test
    void should_configure_instanceId_as_uuid_by_default() {
        assertThat(properties.getTopic()).isEqualTo("foo");
        assertThat(properties.getInstanceId()).isNotBlank();
        assertThatNoException().isThrownBy(() -> {
            UUID uuid = UUID.fromString(properties.getInstanceId());
            assertThat(uuid).isNotNull();
        });

    }


}
