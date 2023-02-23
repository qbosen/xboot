package top.abosen.xboot.extensionfield.schema;

import com.google.auto.service.AutoService;
import lombok.*;
import top.abosen.xboot.extensionfield.ValueHolder;
import top.abosen.xboot.extensionfield.jackson.JsonSubType;

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
@JsonSubType("long-list")

public class LongListSchema extends LongSchema implements ListSchema {
    @Override
    protected boolean checkSchema(ValueHolder valueHolder) {
        return listValue(valueHolder).stream().allMatch(super::checkSchema);
    }

    @Override
    public void resolveValue(ValueHolder holder) {
        listValue(holder).forEach(super::resolveValue);
    }
}
