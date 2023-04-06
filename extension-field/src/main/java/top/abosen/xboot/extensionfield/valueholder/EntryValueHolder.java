package top.abosen.xboot.extensionfield.valueholder;

import java.util.Map;

/**
 * @author qiubaisen
 * @since 2023/2/27
 */
public final class EntryValueHolder implements ValueHolder {
    private final Map.Entry<String, Object> entry;

    private EntryValueHolder(Map.Entry<String, Object> entry) {
        this.entry = entry;
    }

    public static ValueHolder of(Map.Entry<String, Object> entry) {
        return new EntryValueHolder(entry);
    }

    @Override
    public Object get() {
        return entry.getValue();
    }

    @Override
    public void set(Object value) {
        entry.setValue(value);
    }

}
