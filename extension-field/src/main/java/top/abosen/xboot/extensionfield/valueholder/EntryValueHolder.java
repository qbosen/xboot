package top.abosen.xboot.extensionfield.valueholder;

import lombok.Value;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;

import java.util.Map;

/**
 * @author qiubaisen
 * @date 2023/2/27
 */
@Value(staticConstructor = "of")
public class EntryValueHolder implements ValueHolder {
    Map.Entry<String, Object> entry;

    @Override
    public Object get() {
        return entry.getValue();
    }

    @Override
    public void set(Object value) {
        entry.setValue(value);
    }
}
