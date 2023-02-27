package top.abosen.xboot.extensionfield.widget;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.auto.service.AutoService;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;

/**
 * @author qiubaisen
 * @date 2023/2/27
 */

@Data
@NoArgsConstructor
public class DefaultBizWidgetExtension implements BizWidgetExtension{
    String key;

    @Override
    public boolean checkValue(ValueHolder valueHolder) {
        return true;
    }

    public DefaultBizWidgetExtension(String key) {
        this.key = key;
    }
}
