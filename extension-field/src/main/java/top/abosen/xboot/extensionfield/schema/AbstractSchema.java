package top.abosen.xboot.extensionfield.schema;

import lombok.*;
import lombok.experimental.SuperBuilder;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;

/**
 * @author qiubaisen
 * @since 2023/2/23
 */

@Getter
@Setter
@ToString
@EqualsAndHashCode
@SuperBuilder
@NoArgsConstructor
public abstract class AbstractSchema<T> implements Schema {
    @Builder.Default
    private boolean required = false;
    @Builder.Default
    private T defaultValue = null;

    protected boolean shouldUseDefault(ValueHolder valueHolder){
        return valueHolder.get() == null;
    }

    @Override
    public final boolean checkValue(ValueHolder valueHolder) {
        // key not exists
        if (valueHolder == null) return !required;

        if (shouldUseDefault (valueHolder)) {
            valueHolder.set(defaultValue);
        } else {
            resolveValue(valueHolder);
        }
        if (required && valueHolder.get() == null) return false;

        return checkSchema(valueHolder);
    }

    protected abstract boolean checkSchema(ValueHolder valueHolder);
}
