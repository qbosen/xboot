package top.abosen.xboot.extensionfield.schema;

import com.google.auto.service.AutoService;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
public class DoubleListSchema extends AbstractListSchema {
    public final String type = "double-list";
    Double min;
    Double max;

    @Override
    protected Schema contentSchema() {
        DoubleSchema schema = new DoubleSchema();
        schema.setMin(min);
        schema.setMax(max);
        return schema;
    }
}
