package top.abosen.xboot.demo;

import com.google.auto.service.AutoService;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;
import top.abosen.xboot.extensionfield.widget.BizWidgetExtension;

/**
 * @author qiubaisen
 * @date 2023/3/1
 */

@AutoService(BizWidgetExtension.class)
@Getter
@Slf4j
@EqualsAndHashCode
@ToString
public class VideoBizExtension implements BizWidgetExtension {
    final String key = "video";

    @Override
    public boolean checkValue(ValueHolder valueHolder) {
        if (valueHolder != null && valueHolder.get() != null && valueHolder.get() instanceof Long) {
            Long video = (Long) valueHolder.get();
            log.info("使用视频id:{}, 可以放入InvokeContext等线程上下文中集中处理业务", video);
            return true;
        }
        return false;
    }
}
