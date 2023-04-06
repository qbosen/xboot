package top.abosen.xboot.broadcast;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qiubaisen
 * @since 2023/3/14
 */


@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, property = "@type", include = JsonTypeInfo.As.EXTERNAL_PROPERTY)
@JsonTypeIdResolver(BroadcastMessageTypeIdResolver.class)
public abstract class InstanceMessage {
    protected String instanceId;
}