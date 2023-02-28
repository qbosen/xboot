package top.abosen.xboot.extensionfield.valueholder;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

import java.util.List;
import java.util.Map;

/**
 * @author qiubaisen
 * @date 2023/2/27
 */
public final class ListValueHolder implements ValueHolder {
    private final List<Object> list;
    private final int index;

    private ListValueHolder(List<Object> list, int index) {
        this.list = list;
        this.index = index;
    }

    public static ValueHolder of(List<Object> list, int index) {
        return new ListValueHolder(list, index);
    }


    @Override
    public Object get() {
        return list.get(index);
    }

    @Override
    public void set(Object value) {
        list.set(index, value);
    }

}
