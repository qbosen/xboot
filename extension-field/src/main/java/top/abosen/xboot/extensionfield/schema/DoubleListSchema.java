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

public class DoubleListSchema extends AbstractListSchema {
    public static final String TYPE = "double-list";
    Double min;
    Double max;

    public DoubleListSchema() {
        super(TYPE);
    }
    public static DoubleListSchema of(Double min, Double max, Integer minSize, Integer maxSize, boolean required) {
        DoubleListSchema schema = new DoubleListSchema();
        schema.setMin(min);
        schema.setMax(max);
        schema.setMinSize(minSize);
        schema.setMaxSize(maxSize);
        schema.setRequired(required);
        return schema;
    }
    @Override
    protected Schema contentSchema() {
        DoubleSchema schema = new DoubleSchema();
        schema.setMin(min);
        schema.setMax(max);
        return schema;
    }
}
