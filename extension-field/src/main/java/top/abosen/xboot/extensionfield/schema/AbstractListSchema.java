package top.abosen.xboot.extensionfield.schema;

import lombok.*;
import lombok.experimental.SuperBuilder;
import top.abosen.xboot.extensionfield.util.Utils;
import top.abosen.xboot.extensionfield.validator.LengthValidator;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;

import java.util.List;

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
public abstract class AbstractListSchema implements ListSchema {
    @Builder.Default
    private boolean required = false;
    Integer minSize;
    Integer maxSize;

    @Override
    public final boolean checkValue(ValueHolder valueHolder) {
        // key not exists
        if (valueHolder == null) return !required;

        resolveValue(valueHolder);

        if (required && valueHolder.get() == null) return false;

        Schema contentSchema = contentSchema();
        List<ValueHolder> valueHolders = Utils.listValue(valueHolder);

        if (!LengthValidator.collection(minSize, maxSize).valid(valueHolders)) {
            return false;
        }

        return valueHolders.stream().allMatch(contentSchema::checkValue);
    }

    @Override
    public void resolveValue(ValueHolder holder) {
        Schema contentSchema = contentSchema();
        Utils.listValue(holder).forEach(contentSchema::resolveValue);
    }

    protected abstract Schema contentSchema();
}
