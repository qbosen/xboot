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
@JsonSubType("integer")
public class IntegerSchema extends AbstractSchema {
    Integer min;
    Integer max;

    @Override
    protected boolean checkSchema(ValueHolder valueHolder) {
        return new NumberValidator<>(min, max).valid(valueHolder.get());
    }

    @Override
    public void resolveValue(ValueHolder holder) {
        Object value = holder.get();

        if(value instanceof Number){
            holder.set(((Number) value).intValue());
        }else{
            holder.set(NumberUtil.parseInt(String.valueOf(value)));
        }
    }
}
