package top.abosen.xboot.extensionfield.extension;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Delegate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qiubaisen
 * @since 2023/4/12
 */

@Getter
@EqualsAndHashCode
public class ExtensionFields implements List<ExtensionField>, ExtensionType {
    @Delegate
    @JsonValue
    final List<ExtensionField> fields;

    public ExtensionFields() {
        this.fields = new ArrayList<>();
    }

    @JsonCreator
    public ExtensionFields(List<ExtensionField> fields) {
        this.fields = fields;
    }
}
