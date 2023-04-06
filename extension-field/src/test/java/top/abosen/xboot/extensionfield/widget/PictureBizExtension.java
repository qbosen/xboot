package top.abosen.xboot.extensionfield.widget;

import com.google.auto.service.AutoService;
import lombok.Getter;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;

/**
 * @author qiubaisen
 * @since 2023/2/27
 */
@Getter
@AutoService(BizWidgetExtension.class)
public class PictureBizExtension implements BizWidgetExtension {
    final String key = "picture";

    @Override
    public boolean checkValue(ValueHolder valueHolder) {
        if (valueHolder != null && valueHolder.get() != null && valueHolder.get() instanceof Long) {
            Long originPictureId = (Long) valueHolder.get();
            valueHolder.set(1000_000 + originPictureId);
            return true;
        }
        return false;
    }
}
