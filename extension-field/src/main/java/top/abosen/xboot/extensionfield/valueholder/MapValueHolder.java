package top.abosen.xboot.extensionfield.valueholder;

import java.util.Map;

/**
 * @author qiubaisen
 * @since 2023/2/28
 */
public class MapValueHolder implements ValueHolder {
    final Map<String, Object> map;
    final String key;

    private MapValueHolder(Map<String, Object> map, String key) {
        this.map = map;
        this.key = key;
    }

    public static ValueHolder of(Map<String, Object> map, String key) {
        return new MapValueHolder(map, key);
    }

    @Override
    public Object get() {
        return map.get(key);
    }

    @Override
    public void set(Object value) {
        map.put(key, value);
    }
}
