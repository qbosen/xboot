package top.abosen.xboot.extensionfield.widget;

import cn.hutool.core.collection.CollUtil;
import com.google.auto.service.AutoService;
import lombok.*;
import lombok.experimental.SuperBuilder;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;

import java.util.Map;
import java.util.Optional;

/**
 * @author qiubaisen
 * @date 2023/2/27
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AutoService(Widget.class)
@SuperBuilder
@NoArgsConstructor
public class OptionWidget extends AbstractWidget {
    public final String type = "option";
    private boolean multiple;
    private String style;

    /**
     * 选项,key作为展示,value为保存时的值,该值在保存时参与schema校验
     */
    private Map<String, Object> options;

    @Override
    public boolean checkValue(ValueHolder valueHolder) {
        return options != null && options.containsValue(valueHolder.get());
    }

    @Override
    public Optional<String> validMessage() {
        if (CollUtil.isEmpty(options)) {
            return Optional.of("选项不能为空");
        }
        return Optional.empty();
    }
}
