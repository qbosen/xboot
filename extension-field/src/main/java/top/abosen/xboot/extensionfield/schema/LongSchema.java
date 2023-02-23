package top.abosen.xboot.extensionfield.schema;

import cn.hutool.core.util.NumberUtil;
import com.google.auto.service.AutoService;
import lombok.*;
import top.abosen.xboot.extensionfield.ValueHolder;
import top.abosen.xboot.extensionfield.jackson.JsonSubType;
import top.abosen.xboot.extensionfield.validator.CombinedValidator;
import top.abosen.xboot.extensionfield.validator.NumberValidator;
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
@JsonSubType("long")

public class LongSchema extends AbstractSchema {
    Long min;
    Long max;

    @Override
    protected boolean checkSchema(ValueHolder valueHolder) {
        return new NumberValidator<>(min, max).valid(valueHolder.get());
    }

    @Override
    public void resolveValue(ValueHolder holder) {
        Object value = holder.get();

        if(value instanceof Number){
            holder.set(((Number) value).longValue());
        }else{
            holder.set(NumberUtil.parseLong(String.valueOf(value)));
        }
    }
}