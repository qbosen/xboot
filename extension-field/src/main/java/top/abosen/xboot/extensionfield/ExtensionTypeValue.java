package top.abosen.xboot.extensionfield;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author qiubaisen
 * @date 2023/2/22
 */
public interface ExtensionTypeValue {

    Map<String, Object> toMap();

    boolean valid(ExtensionType type);

}

@Data
@NoArgsConstructor
class ExtensionTypeValueMap implements ExtensionTypeValue {
    Map<String, Object> map = new LinkedHashMap<>();

    public ExtensionTypeValueMap(Map<String, Object> map) {
        this.map = map;
    }

    @JsonAnySetter
    public void setter(String key, Object value) {
        map.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getter() {
        return map;
    }


    @Override
    public Map<String, Object> toMap() {
        return map;
    }

    @Override
    public boolean valid(ExtensionType type) {
        return type.getFields().stream().allMatch(field -> field.checkValue(
                !map.containsKey(field.getKey()) ? null : new ValueHolder() {
                    @Override
                    public Object get() {
                        return map.get(field.getKey());
                    }

                    @Override
                    public void set(Object value) {
                        map.put(field.getKey(), value);
                    }
                })
        );

    }


}

@Value(staticConstructor = "of")
class EntryValueHolder implements ValueHolder {
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

