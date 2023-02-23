package top.abosen.xboot.extensionfield.schema;

import cn.hutool.core.util.NumberUtil;
import com.google.auto.service.AutoService;
import lombok.*;
import top.abosen.xboot.extensionfield.ValueHolder;
import top.abosen.xboot.extensionfield.jackson.JsonSubType;
import top.abosen.xboot.extensionfield.validator.NumberValidator;

import java.util.Objects;

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
@JsonSubType("double")

public class DoubleSchema extends AbstractSchema {
    Double min;
    Double max;

    @Override
    protected boolean checkSchema(ValueHolder valueHolder) {
        return new NumberValidator<>(min, max).valid(valueHolder.get());
    }

    @Override
    public void resolveValue(ValueHolder holder) {
        Objects.requireNonNull(holder);
        Object value = holder.get();
        Objects.requireNonNull(value);

        if(value instanceof Number){
            holder.set(((Number) value).doubleValue());
        }else{
            holder.set(NumberUtil.parseDouble(String.valueOf(value)));
        }
    }
}
