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
@io.swagger.v3.oas.annotations.media.Schema(description = "简单扩展字段; 由schema和widget组成", example = "{\"@type\":\"simple\",\"key\":\"age\",\"name\":\"年龄\",\"schema\":{\"@type\":\"integer\",\"required\":true,\"default_value\":20,\"min\":0,\"max\":150},\"widget\":{\"@type\":\"input\",\"name\":\"输入年龄\",\"style\":null,\"multiple\":false},\"desc\":\"一个用于填写年龄的简单扩展\"}")
public class SimpleExtensionField extends AbstractExtensionField {
    public final String type = "simple";
    Schema schema;
    Widget widget;

    @Override
    public boolean checkValue(ValueHolder valueHolder) {
        return schema.checkValue(valueHolder) && widget.checkValue(valueHolder);
    }

    @Override
    public void updateValue(ValueHolder valueHolder) {
        widget.updateValue(valueHolder);
    }

    @Override
    protected Optional<String> validMsg() {
        if (schema == null || widget == null) return Optional.of("值和组件不能为空");
        return schema.validMessage().or(() -> widget.validMessage());
    }
}
