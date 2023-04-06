package top.abosen.xboot.extensionfield.extension;

import cn.hutool.core.lang.Opt;
import com.google.auto.service.AutoService;
import lombok.*;
import lombok.experimental.ExtensionMethod;
import lombok.experimental.SuperBuilder;
import top.abosen.xboot.extensionfield.schema.Schema;
import top.abosen.xboot.extensionfield.util.Opt8;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;
import top.abosen.xboot.extensionfield.widget.Widget;

import java.util.Optional;

/**
 * @author qiubaisen
 * @since 2023/2/27
 */
@Getter
@Setter
@AutoService(ExtensionField.class)
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@ExtensionMethod(Opt8.class)
public class SimpleExtensionField extends AbstractExtensionField {
    public final String type = "simple";
    Schema schema;
    Widget widget;

    @Override
    public boolean checkValue(ValueHolder valueHolder) {
        return schema.checkValue(valueHolder) && widget.checkValue(valueHolder);
    }

    @Override
    protected Optional<String> validMsg() {
        if (schema == null || widget == null) return Optional.of("值和组件不能为空");
        return schema.validMessage().or(() -> widget.validMessage());
    }
}
