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
import java.util.stream.Collectors;

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
@Schema(description = "选项组件; 可单选/多选; 值受绑定的schema限制; 有顺序")
public class OptionWidget2 extends AbstractWidget {
    public final String type = "option2";
    @Schema(description = "是否多选; 当schema是list时生效", example = "[true, false]")
    private boolean multiple;

    @Schema(description = "选项,key作唯一键,name为展示,value为保存时的值,该值在保存时参与schema校验", example = "[{\"key\": \"star_1\", \"name\": \"一星\", \"value\": 1},{\"key\": \"star_2\", \"name\": \"二星\", \"value\": 2}]")
    private List<OptionEntry> options;

    @Override
    public boolean checkValue(ValueHolder valueHolder) {
        if (valueHolder == null) return true;
        if (options == null) return false;
        Map<String, Object> options = this.options.stream().collect(Collectors.toMap(OptionEntry::getKey, OptionEntry::getValue));

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
        if (options.stream().map(OptionEntry::getKey).distinct().count() != options.size()) {
            return Optional.of("选项key不能重复");
        }
        if (options.stream().map(OptionEntry::getValue).distinct().count() != options.size()) {
            return Optional.of("选项值不能重复");
        }
        return Optional.empty();
    }

    @Data
    public static class OptionEntry{
        @Schema(description = "选项key;不可重复", example = "[\"start_1\", \"star_2\"]")
        String key;
        @Schema(description = "选项字面", example = "[\"一星\", \"两星\"]")
        String name;
        @Schema(description = "选项值,不可重复;类型与对应schema一致", example = "[1, 2]")
        Object value;
    }
}
