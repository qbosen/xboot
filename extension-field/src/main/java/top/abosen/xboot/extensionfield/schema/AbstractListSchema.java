package top.abosen.xboot.extensionfield.schema;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.abosen.xboot.extensionfield.validator.LengthValidator;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;

import java.util.List;

/**
 * @author qiubaisen
 * @date 2023/2/23
 */

@Getter
@Setter
@ToString
@EqualsAndHashCode
public abstract class AbstractListSchema implements ListSchema {
    private final String type;
    private boolean required = false;
    Integer minSize;
    Integer maxSize;

    protected AbstractListSchema(String type) {
        this.type = type;
    }

    @Override
    public final boolean checkValue(ValueHolder valueHolder) {
        // key not exists
        if (valueHolder == null) return !required;

        resolveValue(valueHolder);

        if (required && valueHolder.get() == null) return false;

        Schema contentSchema = contentSchema();
        List<ValueHolder> valueHolders = listValue(valueHolder);

        if (!LengthValidator.collection(minSize, maxSize).valid(valueHolders)) {
            return false;
        }

        return valueHolders.stream().allMatch(contentSchema::checkValue);
    }

    @Override
    public void resolveValue(ValueHolder holder) {
        Schema contentSchema = contentSchema();
        listValue(holder).forEach(contentSchema::resolveValue);
    }

    protected abstract Schema contentSchema();
}
