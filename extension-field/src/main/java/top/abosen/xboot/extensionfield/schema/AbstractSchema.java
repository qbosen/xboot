package top.abosen.xboot.extensionfield.schema;

import lombok.*;
import top.abosen.xboot.extensionfield.ValueHolder;

/**
 * @author qiubaisen
 * @date 2023/2/23
 */

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public abstract class AbstractSchema implements Schema {
    private boolean required = false;
    private Object defaultValue = null;

    @Override
    public final boolean checkValue(ValueHolder valueHolder) {
        // key not exists
        if(valueHolder == null) return !required;

        if (valueHolder.get() == null) {
            valueHolder.set(defaultValue);
        }else{
            resolveValue(valueHolder);
        }
        if (required && valueHolder.get() == null) return false;

        return checkSchema(valueHolder);
    }

    protected abstract boolean checkSchema(ValueHolder valueHolder);
}
