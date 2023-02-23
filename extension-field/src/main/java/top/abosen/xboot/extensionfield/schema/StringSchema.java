package top.abosen.xboot.extensionfield.schema;

import com.google.auto.service.AutoService;
import lombok.*;
import top.abosen.xboot.extensionfield.ValueHolder;
import top.abosen.xboot.extensionfield.jackson.JsonSubType;
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
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AutoService(Schema.class)
@JsonSubType("string")
public class StringSchema extends AbstractSchema {
    Integer minLength;
    Integer maxLength;
    String regex;

    @Override
    protected boolean checkSchema(ValueHolder valueHolder) {
        return buildValidator().valid(valueHolder.get());
    }

    @Override
    public void resolveValue(ValueHolder holder) {
        Object value = holder.get();
        holder.set(String.valueOf(value));
    }

    private ValueValidator buildValidator() {
        return new CombinedValidator(
                new LengthValidator(minLength, maxLength, it -> String.valueOf(it).length()),
                new RegexValidator(regex)
        );
    }
}
