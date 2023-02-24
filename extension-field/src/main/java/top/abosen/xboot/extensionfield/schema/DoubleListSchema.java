package top.abosen.xboot.extensionfield.schema;

import com.google.auto.service.AutoService;
import lombok.*;
import top.abosen.xboot.extensionfield.jackson.JsonSubType;

/**
 * @author qiubaisen
 * @date 2023/2/23
 */

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AutoService(Schema.class)

public class DoubleListSchema extends AbstractListSchema {
    public static final String TYPE = "double-list";
    Double min;
    Double max;

    public DoubleListSchema() {
        super(TYPE);
    }

    @Override
    protected Schema contentSchema() {
        DoubleSchema schema = new DoubleSchema();
        schema.setMin(min);
        schema.setMax(max);
        return schema;
    }
}
