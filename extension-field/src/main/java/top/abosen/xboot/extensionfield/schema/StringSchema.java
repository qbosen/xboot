package top.abosen.xboot.extensionfield.schema;

import cn.hutool.core.util.StrUtil;
import com.google.auto.service.AutoService;
import lombok.*;
import lombok.experimental.SuperBuilder;
import top.abosen.xboot.extensionfield.validator.CombinedValidator;
import top.abosen.xboot.extensionfield.validator.LengthValidator;
import top.abosen.xboot.extensionfield.validator.RegexValidator;
import top.abosen.xboot.extensionfield.validator.ValueValidator;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;

/**
 * @author qiubaisen
 * @since 2023/2/23
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AutoService(Schema.class)
@SuperBuilder
@NoArgsConstructor
public class StringSchema extends AbstractSchema<String> {
    public final String type = "string";
    Integer minLength;
    Integer maxLength;
    String regex;

    @Override
    protected boolean shouldUseDefault(ValueHolder valueHolder) {
        return StrUtil.isEmptyIfStr(valueHolder.get());
    }

    @Override
    protected boolean checkSchema(ValueHolder valueHolder) {
        return buildValidator().valid(valueHolder.get());
    }

    @Override
    public void resolveValue(ValueHolder holder) {
        if (holder == null || holder.get() == null) return;
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
