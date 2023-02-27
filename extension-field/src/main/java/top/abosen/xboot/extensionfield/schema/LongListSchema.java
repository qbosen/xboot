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

public class LongListSchema extends AbstractListSchema{

    Long min;
    Long max;

    public static final String TYPE = "long-list";

    public LongListSchema() {
        super(TYPE);
    }

    @Override
    protected Schema contentSchema() {
        LongSchema schema = new LongSchema();
        schema.setMin(min);
        schema.setMax(max);
        return null;
    }
}
