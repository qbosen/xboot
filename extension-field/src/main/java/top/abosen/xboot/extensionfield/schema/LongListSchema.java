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
public class LongListSchema extends AbstractListSchema {

    public final String type = "long-list";
    Long min;
    Long max;


    @Override
    protected Schema contentSchema() {
        LongSchema schema = new LongSchema();
        schema.setMin(min);
        schema.setMax(max);
        return schema;
    }
}
