package top.abosen.xboot.extensionfield.schema;

import com.google.auto.service.AutoService;
import lombok.*;

/**
 * @author qiubaisen
 * @date 2023/2/23
 */

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AutoService(Schema.class)

public class IntegerListSchema extends AbstractListSchema {
    public static final String TYPE = "integer-list";
    Integer min;
    Integer max;

    public IntegerListSchema() {
        super(TYPE);
    }

    @Override
    protected Schema contentSchema() {
        IntegerSchema schema = new IntegerSchema();
        schema.setMin(min);
        schema.setMax(max);
        return schema;
    }
}
