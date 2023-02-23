package top.abosen.xboot.extensionfield.schema;

import cn.hutool.core.stream.StreamUtil;
import com.google.common.collect.Lists;
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

public interface ListSchema extends Schema {

    default List<ValueHolder> listValue(ValueHolder holder) {
        if (holder == null || holder.get() == null) return Collections.emptyList();

        final List<Object> data;
        Object value = holder.get();
        if (value.getClass().isArray()) {
            data = Arrays.stream((Object[]) value).collect(Collectors.toList());
        } else if (value instanceof Iterable) {
            data = StreamUtil.of(((Iterable<?>) value)).collect(Collectors.toList());
        }else{
            data = new ArrayList<>();
        }
        holder.set(data);

        return IntStream.range(0, data.size()).mapToObj(i -> new ValueHolder() {
            @Override
            public Object get() {
                return data.get(i);
            }

            @Override
            public void set(Object value) {
                data.set(i, value);
            }
        }).collect(Collectors.toList());
    }

}
