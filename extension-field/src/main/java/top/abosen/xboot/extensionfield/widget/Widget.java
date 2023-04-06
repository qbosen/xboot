package top.abosen.xboot.extensionfield.widget;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import top.abosen.xboot.extensionfield.validator.Validatable;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;
import top.abosen.xboot.extensionfield.valueholder.ValueHolderChecker;

/**
 * @author qiubaisen
 * @since 2023/2/21
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@Schema(oneOf = {InputWidget.class, OptionWidget.class, SelectWidget.class})
public interface Widget extends ValueHolderChecker, Validatable {
    @JsonIgnore
    String getType();

    String getName();

    @Override
    default boolean checkValue(ValueHolder valueHolder) {
        return true;
    }
}

