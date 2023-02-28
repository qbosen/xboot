package top.abosen.xboot.extensionfield.schema;

import com.google.auto.service.AutoService;
import lombok.*;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;
import top.abosen.xboot.extensionfield.validator.CombinedValidator;
import top.abosen.xboot.extensionfield.validator.LengthValidator;
import top.abosen.xboot.extensionfield.validator.RegexValidator;
import top.abosen.xboot.extensionfield.validator.ValueValidator;

/**
 * @author qiubaisen
 * @date 2023/2/23
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AutoService(Schema.class)
public class StringSchema extends AbstractSchema {
    public static final String TYPE = "string";
    Integer minLength;
    Integer maxLength;
    String regex;

    public StringSchema() {
        super(TYPE);
    }

    @Override
    protected boolean checkSchema(ValueHolder valueHolder) {
        return buildValidator().valid(valueHolder.get());
    }

    @Override
    public void resolveValue(ValueHolder holder) {
        if(holder == null || holder.get() == null) return;
        Object value = holder.get();
        holder.set(String.valueOf(value));
    }

    private ValueValidator buildValidator() {
        return new CombinedValidator(
                LengthValidator.string(minLength, maxLength),
                new RegexValidator(regex)
        );
    }
}
