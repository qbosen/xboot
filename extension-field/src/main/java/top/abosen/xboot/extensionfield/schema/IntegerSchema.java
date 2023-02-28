package top.abosen.xboot.extensionfield.schema;

import cn.hutool.core.util.NumberUtil;
import com.google.auto.service.AutoService;
import lombok.*;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;
import top.abosen.xboot.extensionfield.validator.NumberValidator;

/**
 * @author qiubaisen
 * @date 2023/2/23
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AutoService(Schema.class)

public class IntegerSchema extends AbstractSchema {
    public static final String TYPE = "integer";
    Integer min;
    Integer max;

    public IntegerSchema() {
        super(TYPE);
    }

    @Override
    protected boolean checkSchema(ValueHolder valueHolder) {
        return new NumberValidator<>(min, max).valid(valueHolder.get());
    }

    @Override
    public void resolveValue(ValueHolder holder) {
        if(holder == null || holder.get() == null) return;
        Object value = holder.get();

        if(value instanceof Number){
            holder.set(((Number) value).intValue());
        }else{
            holder.set(NumberUtil.parseInt(String.valueOf(value)));
        }
    }
}
