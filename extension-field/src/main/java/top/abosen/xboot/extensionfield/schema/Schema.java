package top.abosen.xboot.extensionfield.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import top.abosen.xboot.extensionfield.validator.Validatable;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;
import top.abosen.xboot.extensionfield.valueholder.ValueHolderChecker;

/**
 * @author qiubaisen
 * @since 2023/2/21
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type", include = JsonTypeInfo.As.EXTERNAL_PROPERTY)
@io.swagger.v3.oas.annotations.media.Schema(
        description = "值定义,由@type决定: integer, double, long, string, integer-list, double-list, long-list, string-list",
        discriminatorProperty = "@type",
        discriminatorMapping = {
                @DiscriminatorMapping(value = "integer", schema = IntegerSchema.class),
                @DiscriminatorMapping(value = "double", schema = DoubleSchema.class),
                @DiscriminatorMapping(value = "long", schema = LongSchema.class),
                @DiscriminatorMapping(value = "string", schema = StringSchema.class),
                @DiscriminatorMapping(value = "integer-list", schema = IntegerListSchema.class),
                @DiscriminatorMapping(value = "double-list", schema = DoubleListSchema.class),
                @DiscriminatorMapping(value = "long-list", schema = LongListSchema.class),
                @DiscriminatorMapping(value = "string-list", schema = StringListSchema.class),
        },
        oneOf = {
                IntegerSchema.class, DoubleSchema.class, LongSchema.class, StringSchema.class,
                IntegerListSchema.class, DoubleListSchema.class, LongListSchema.class, StringListSchema.class,
        })
public interface Schema extends ValueHolderChecker, Validatable {

    @JsonIgnore
    String getType();

    /**
     * 根据自身schema的值定义,可以修改/转换目标值
     *
     * @param holder 值
     */
    default void resolveValue(ValueHolder holder) {
    }

}

