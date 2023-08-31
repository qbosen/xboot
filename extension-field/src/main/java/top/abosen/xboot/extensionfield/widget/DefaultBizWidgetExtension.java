package top.abosen.xboot.extensionfield.widget;

import lombok.Data;
import lombok.NoArgsConstructor;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;

/**
 * @author qiubaisen
 * @since 2023/2/27
 */

@Data
@NoArgsConstructor
public class DefaultBizWidgetExtension implements BizWidgetExtension {
    String key;

    public DefaultBizWidgetExtension(String key) {
        this.key = key;
    }

    @Override
    public boolean checkValue(ValueHolder valueHolder) {
        return true;
    }

    @Override
    public void updateValue(ValueHolder valueHolder) {

    }
}
