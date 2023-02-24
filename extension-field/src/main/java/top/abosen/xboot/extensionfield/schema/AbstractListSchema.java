package top.abosen.xboot.extensionfield.schema;

import cn.hutool.core.stream.StreamUtil;
import lombok.*;
import top.abosen.xboot.extensionfield.ValueHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author qiubaisen
 * @date 2023/2/23
 */

@Getter
@Setter
@ToString
@EqualsAndHashCode
public abstract class AbstractListSchema implements ListSchema {
    private final String type;
    private boolean required = false;

    protected AbstractListSchema(String type) {
        this.type = type;
    }

    @Override
    public final boolean checkValue(ValueHolder valueHolder) {
        // key not exists
        if (valueHolder == null) return !required;

        resolveValue(valueHolder);

        if (required && valueHolder.get() == null) return false;

        Schema contentSchema = contentSchema();
        return listValue(valueHolder).stream().allMatch(contentSchema::checkValue);
    }

    @Override
    public void resolveValue(ValueHolder holder) {
        Schema contentSchema = contentSchema();
        listValue(holder).forEach(contentSchema::resolveValue);
    }

    protected abstract Schema contentSchema();
}
