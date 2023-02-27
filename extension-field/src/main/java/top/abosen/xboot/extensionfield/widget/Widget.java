package top.abosen.xboot.extensionfield.widget;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import top.abosen.xboot.extensionfield.validator.Validatable;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;
import top.abosen.xboot.extensionfield.valueholder.ValueHolderChecker;

/**
 * @author qiubaisen
 * @date 2023/2/21
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public interface Widget  extends ValueHolderChecker, Validatable {
    @JsonIgnore
    String getType();

    @Override
    default boolean checkValue(ValueHolder valueHolder) {
        return true;
    }
}

