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
public class IntegerListSchema extends AbstractListSchema {
    public final String type = "integer-list";
    Integer min;
    Integer max;


    @Override
    protected Schema contentSchema() {
        IntegerSchema schema = new IntegerSchema();
        schema.setMin(min);
        schema.setMax(max);
        return schema;
    }
}
