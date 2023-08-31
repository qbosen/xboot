package top.abosen.xboot.extensionfield.widget;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import top.abosen.xboot.extensionfield.validator.Validatable;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;
import top.abosen.xboot.extensionfield.valueholder.ValueHolderChecker;
import top.abosen.xboot.extensionfield.valueholder.ValueHolderUpdater;

/**
 * @author qiubaisen
 * @since 2023/2/21
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type", include = JsonTypeInfo.As.EXTERNAL_PROPERTY)
@Schema(
        description = "值定义,由@type决定: input, option, select",
        discriminatorProperty = "@type",
        discriminatorMapping = {
                @DiscriminatorMapping(value = "input", schema = InputWidget.class),
                @DiscriminatorMapping(value = "option", schema = OptionWidget.class),
                @DiscriminatorMapping(value = "select", schema = SelectWidget.class),
        },
        oneOf = {InputWidget.class, OptionWidget.class, SelectWidget.class})
public interface Widget extends ValueHolderChecker, ValueHolderUpdater, Validatable {
    @JsonIgnore
    String getType();

    String getName();

    @Override
    default boolean checkValue(ValueHolder valueHolder) {
        return true;
    }

    @Override
    default void updateValue(ValueHolder valueHolder) {
    }
}

