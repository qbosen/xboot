package top.abosen.xboot.extensionfield.extension;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import top.abosen.xboot.extensionfield.validator.Validatable;
import top.abosen.xboot.extensionfield.valueholder.ValueHolderChecker;

import java.util.Map;

/**
 * @author qiubaisen
 * @since 2023/2/23
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, defaultImpl = SimpleExtensionField.class)
@Schema(
        discriminatorProperty = "@type",
        discriminatorMapping = {
                @DiscriminatorMapping(value = "simple", schema = SimpleExtensionField.class),
                @DiscriminatorMapping(value = "map", schema = MapExtensionField.class),
                @DiscriminatorMapping(value = "list", schema = ListExtensionField.class),
                @DiscriminatorMapping(value = "switch", schema = SwitchExtensionField.class),
        },
        oneOf = {SimpleExtensionField.class, MapExtensionField.class, ListExtensionField.class, SwitchExtensionField.class})
public interface ExtensionField extends ValueHolderChecker, Validatable {
    @JsonIgnore
    String getType();
    String getKey();

    String getName();

    Map<String, Object> extension();
}
