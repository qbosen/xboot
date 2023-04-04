package top.abosen.xboot.broadcast.redis;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

/**
 * @author qiubaisen
 * @since 2023/3/29
 */

@Validated
@ConfigurationProperties(prefix = BroadcastRedisProperties.PREFIX)
@Data
public class BroadcastRedisProperties {
    public static final String PREFIX = "xboot.broadcast.redis";
    public static final String TOPIC = "xboot.broadcast.redis.topic";

    @NotBlank
    private String topic;


    @Value("${spring.cloud.consul.discovery.instance-id:${random.uuid}}")
    private String instanceId;
}
