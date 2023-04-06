package top.abosen.xboot.extensionfield.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import top.abosen.xboot.extensionfield.validator.Validatable;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;
import top.abosen.xboot.extensionfield.valueholder.ValueHolderChecker;

/**
 * @author qiubaisen
 * @since 2023/2/21
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@io.swagger.v3.oas.annotations.media.Schema(oneOf = {
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

