package top.abosen.xboot.extensionfield.extension;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import top.abosen.xboot.extensionfield.validator.Validatable;
import top.abosen.xboot.extensionfield.valueholder.ValueHolderChecker;
import top.abosen.xboot.extensionfield.valueholder.ValueHolderUpdater;

import java.util.Map;

/**
 * @author qiubaisen
 * @since 2023/2/23
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, defaultImpl = SimpleExtensionField.class, property = "@type",
        include = JsonTypeInfo.As.EXTERNAL_PROPERTY)
@Schema(description = "扩展字段; 由@type决定具体类型: simple, map, list, switch" + "\n" +
        "所有额外传递的kv会被当作extension传递给后端,供业务使用",
        discriminatorProperty = "@type",
        discriminatorMapping = {
                @DiscriminatorMapping(value = "simple", schema = SimpleExtensionField.class),
                @DiscriminatorMapping(value = "map", schema = MapExtensionField.class),
                @DiscriminatorMapping(value = "list", schema = ListExtensionField.class),
                @DiscriminatorMapping(value = "switch", schema = SwitchExtensionField.class),
        },
        oneOf = {SimpleExtensionField.class, MapExtensionField.class, ListExtensionField.class, SwitchExtensionField.class}
)
public interface ExtensionField extends ValueHolderChecker, ValueHolderUpdater, Validatable {
    @JsonIgnore
    String getType();

    String getKey();

    String getName();

    Map<String, Object> extension();

}
