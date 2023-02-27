package top.abosen.xboot.extensionfield.extension;

import cn.hutool.core.lang.Opt;
import com.google.auto.service.AutoService;
import lombok.Getter;
import top.abosen.xboot.extensionfield.jackson.JsonSubType;
import top.abosen.xboot.extensionfield.schema.Schema;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;
import top.abosen.xboot.extensionfield.widget.OptionWidget;
import top.abosen.xboot.extensionfield.widget.Widget;

import java.util.Optional;

/**
 * @author qiubaisen
 * @date 2023/2/27
 */
@Getter
@AutoService(ExtensionField.class)
@JsonSubType("simple")
public class SimpleExtensionField extends AbstractExtensionField {
    Schema schema;
    Widget widget;

    @Override
    public boolean checkValue(ValueHolder valueHolder) {
        return schema.checkValue(valueHolder) && widget.checkValue(valueHolder);
    }

    @Override
    public Optional<String> validMessage() {
        if(schema == null || widget == null) return Optional.of("值 和 组件 定义不能为空");
        return Opt.of(1).flattedMap(it -> schema.validMessage())
                .or(() -> Opt.of(1).flattedMap(it -> widget.validMessage()))
                .toOptional();
    }
}
