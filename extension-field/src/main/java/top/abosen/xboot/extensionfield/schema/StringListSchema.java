package top.abosen.xboot.extensionfield.schema;

import com.google.auto.service.AutoService;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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


public class StringListSchema extends AbstractListSchema {
    public static final String TYPE = "string-list";
    Integer minLength;
    Integer maxLength;
    String regex;

    public StringListSchema() {
        super(TYPE);
    }

    @Override
    protected Schema contentSchema() {
        StringSchema schema = new StringSchema();
        schema.setMinLength(minLength);
        schema.setMaxLength(maxLength);
        schema.setRegex(regex);
        return schema;
    }
}
