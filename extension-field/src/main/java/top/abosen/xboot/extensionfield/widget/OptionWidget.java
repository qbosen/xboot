package top.abosen.xboot.extensionfield.widget;

import cn.hutool.core.collection.CollUtil;
import com.google.auto.service.AutoService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author qiubaisen
 * @since 2023/2/27
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AutoService(Widget.class)
@SuperBuilder
@NoArgsConstructor
@Schema(description = "选项组件; 可单选/多选; 值受绑定的schema限制")
public class OptionWidget extends AbstractWidget {
    public final String type = "option";
    @Schema(description = "是否多行; 当schema是list时生效", example = "[true, false]")
    private boolean multiple;

    @Schema(description = "选项,key作为展示,value为保存时的值,该值在保存时参与schema校验", example = "{\"选项1\":1,\"选项2\":2}")
    private Map<String, Object> options;

    @Override
    public boolean checkValue(ValueHolder valueHolder) {
        if (valueHolder == null) return true;

        if (options == null) return false;
        Object realValue = valueHolder.get();
        if (multiple && realValue instanceof List) {
            return ((List<?>) realValue).stream().allMatch(options::containsValue);
        }
        return options.containsValue(realValue);
    }

    @Override
    public Optional<String> validMessage() {
        if (CollUtil.isEmpty(options)) {
            return Optional.of("选项不能为空");
        }
        if (options.values().stream().distinct().count() != options.size()) {
            return Optional.of("选项值不能重复");
        }
        return Optional.empty();
    }
}
