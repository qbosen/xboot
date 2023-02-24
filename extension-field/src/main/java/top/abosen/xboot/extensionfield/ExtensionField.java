package top.abosen.xboot.extensionfield;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.auto.service.AutoService;
import lombok.Getter;
import top.abosen.xboot.extensionfield.jackson.JsonSubType;
import top.abosen.xboot.extensionfield.schema.Schema;
import top.abosen.xboot.extensionfield.widget.Widget;

import java.util.List;
import java.util.Map;

/**
 * @author qiubaisen
 * @date 2023/2/23
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
interface ExtensionField extends ValueHolderChecker{
    String getKey();

    @Getter
    @AutoService(ExtensionField.class)
    @JsonSubType("simple")
    class SimpleExtensionField implements ExtensionField {
        String key;
        Schema schema;
        Widget widget;

        @Override
        public boolean checkValue(ValueHolder valueHolder) {
            return schema.checkValue(valueHolder) && widget.checkValue(valueHolder);
        }
    }

    @Getter
    @AutoService(ExtensionField.class)
    @JsonSubType("nested")
    class NestedExtensionField implements ExtensionField {
        String key;
        List<ExtensionField> fields;

        @Override
        public boolean checkValue(ValueHolder valueHolder) {
            Object nestedValue = valueHolder.get();
            if (!(nestedValue instanceof Map)) return false;
            return new ExtensionTypeValueMap((Map<String, Object>) nestedValue)
                    .valid(new ExtensionType() {
                        @Override
                        public List<ExtensionField> getFields() {
                            return fields;
                        }
                    });
        }
    }
}
