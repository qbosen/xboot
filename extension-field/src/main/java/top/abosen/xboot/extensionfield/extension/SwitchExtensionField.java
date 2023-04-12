package top.abosen.xboot.extensionfield.extension;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.util.StrUtil;
import com.google.auto.service.AutoService;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import top.abosen.xboot.extensionfield.validator.Validatable;
import top.abosen.xboot.extensionfield.valueholder.MapValueHolder;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;

import java.util.Map;
import java.util.Optional;

/**
 * option不重复的field分支
 *
 * @author qiubaisen
 * @since 2023/2/27
 */
@Getter
@Setter
@AutoService(ExtensionField.class)
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "条件组合扩展, 只能从options中选择一个", example = "{\"@type\":\"switch\",\"key\":\"pay-type\",\"name\":\"支付方式\",\"options\":{\"支付宝\":{\"@type\":\"map\",\"key\":\"alipay\",\"name\":\"支付宝设置\",\"fields\":[{\"@type\":\"simple\",\"key\":\"phone\",\"name\":\"帐号\",\"schema\":{\"@type\":\"string\",\"required\":true,\"default_value\":null,\"min_length\":null,\"max_length\":null,\"regex\":\"^1[3-9]\\\\d{9}$\"},\"widget\":{\"@type\":\"input\",\"name\":\"输入支付宝帐号\",\"style\":null,\"multiple\":false}},{\"@type\":\"simple\",\"key\":\"password\",\"name\":\"密码\",\"schema\":{\"@type\":\"string\",\"required\":true,\"default_value\":null,\"min_length\":6,\"max_length\":20,\"regex\":null},\"widget\":{\"@type\":\"input\",\"name\":\"输入支付宝密码\",\"style\":\"password\",\"multiple\":false}}]},\"银行卡\":{\"@type\":\"map\",\"key\":\"credit\",\"name\":\"银行卡设置\",\"fields\":[{\"@type\":\"simple\",\"key\":\"no\",\"name\":\"卡号\",\"schema\":{\"@type\":\"string\",\"required\":true,\"default_value\":null,\"min_length\":null,\"max_length\":null,\"regex\":\"^([1-9]{1})(\\\\d{14}|\\\\d{18})$\"},\"widget\":{\"@type\":\"input\",\"name\":\"输入卡号\",\"style\":null,\"multiple\":false}},{\"@type\":\"simple\",\"key\":\"password\",\"name\":\"密码\",\"schema\":{\"@type\":\"string\",\"required\":true,\"default_value\":null,\"min_length\":6,\"max_length\":6,\"regex\":null},\"widget\":{\"@type\":\"input\",\"name\":\"输入密码\",\"style\":\"password\",\"multiple\":false}}]}},\"desc\":\"任选一种支付方式即可\"}")
public class SwitchExtensionField extends AbstractExtensionField {
    public final String type = "switch";

    Map<String, ExtensionField> options;

    @SuppressWarnings("unchecked")
    @Override
    public boolean checkValue(ValueHolder valueHolder) {
        Object nestedValue = valueHolder.get();
        if (!(nestedValue instanceof Map)) return false;
        Map<String, Object> castValue = (Map<String, Object>) nestedValue;

        String selectOption = castValue.keySet().stream().filter(options::containsKey).findFirst().orElse(null);
        if (selectOption == null) return false;
        if (!options.containsKey(selectOption)) return false;
        return options.get(selectOption).checkValue(MapValueHolder.of(castValue, selectOption));
    }

    @Override
    protected Optional<String> validMsg() {
        if (CollUtil.isEmpty(options)) return Optional.of("字段列表不能为空");
        if(options.keySet().stream().anyMatch(StrUtil::isBlank)) return Optional.of("选项不能为空");
        return options.values().stream().map(Validatable::validMessage)
                .filter(Optional::isPresent)
                .map(Optional::get).findFirst();
    }
}
