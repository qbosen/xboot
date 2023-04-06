package top.abosen.xboot.extensionfield.schema;

import cn.hutool.core.util.NumberUtil;
import com.google.auto.service.AutoService;
import lombok.*;
import lombok.experimental.SuperBuilder;
import top.abosen.xboot.extensionfield.validator.NumberValidator;
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
public class LongSchema extends AbstractSchema<Long> {
    public final String type = "long";
    Long min;
    Long max;


    @Override
    protected boolean checkSchema(ValueHolder valueHolder) {
        return new NumberValidator<>(min, max).valid(valueHolder.get());
    }

    @Override
    public void resolveValue(ValueHolder holder) {
        if (holder == null || holder.get() == null) return;
        Object value = holder.get();

        if (value instanceof Number) {
            holder.set(((Number) value).longValue());
        } else {
            holder.set(NumberUtil.parseLong(String.valueOf(value)));
        }
    }
}