package top.abosen.xboot.extensionfield.schema;

import com.google.auto.service.AutoService;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author qiubaisen
 * @date 2023/2/23
 */

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AutoService(Schema.class)
@SuperBuilder
@NoArgsConstructor
public class StringListSchema extends AbstractListSchema {
    public final String type = "string-list";
    Integer minLength;
    Integer maxLength;
    String regex;


    @Override
    protected Schema contentSchema() {
        StringSchema schema = new StringSchema();
        schema.setMinLength(minLength);
        schema.setMaxLength(maxLength);
        schema.setRegex(regex);
        return schema;
    }
}
