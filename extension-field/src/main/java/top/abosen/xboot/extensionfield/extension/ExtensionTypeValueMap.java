package top.abosen.xboot.extensionfield.extension;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author qiubaisen
 * @date 2023/2/27
 */
public class ExtensionTypeValueMap implements ExtensionTypeValue {
    final Map<String, Object> map;

    @JsonAnySetter
    public void setter(String key, Object value) {
        map.put(key, value);
    }

    @Override
    @JsonAnyGetter
    public Map<String, Object> toMap() {
        return map;
    }

    public ExtensionTypeValueMap() {
        this.map = new LinkedHashMap<>();
    }

    public ExtensionTypeValueMap(Map<String, Object> map) {
        this.map = map;
    }

    @Override
    public boolean valid(ExtensionType type) {
        return type.valid(toMap());
    }


}
