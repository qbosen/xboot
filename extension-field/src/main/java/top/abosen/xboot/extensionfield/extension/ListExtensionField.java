package top.abosen.xboot.extensionfield.extension;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import com.google.auto.service.AutoService;
import lombok.*;
import lombok.experimental.ExtensionMethod;
import lombok.experimental.SuperBuilder;
import top.abosen.xboot.extensionfield.util.Opt8;
import top.abosen.xboot.extensionfield.util.Utils;
import top.abosen.xboot.extensionfield.validator.LengthValidator;
import top.abosen.xboot.extensionfield.validator.Validatable;
import top.abosen.xboot.extensionfield.validator.ValueValidator;
import top.abosen.xboot.extensionfield.valueholder.MapValueHolder;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 对同一个field的可重复集合
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
@ExtensionMethod(Opt8.class)
public class ListExtensionField extends AbstractExtensionField {
    public final String type = "list";
    Integer minSize;
    Integer maxSize;
    ExtensionField target;

    @Override
    public boolean checkValue(ValueHolder valueHolder) {
        Object nestedValue = valueHolder.get();
        if (!(nestedValue instanceof List)) return false;
        List<?> listValue = (List<?>) nestedValue;
        if(listValue.stream().anyMatch(Objects::isNull)) return false;
        if(!buildValidator().valid(listValue)) return false;

        return Utils.listValue(valueHolder).stream().allMatch(holder -> target.checkValue(holder));
    }

    protected ValueValidator buildValidator() {
        return LengthValidator.collection(minSize, maxSize);
    }

    @Override
    protected Optional<String> validMsg() {
        if(Objects.isNull(target)) return Optional.of("目标字段不能为空");
        return target.validMessage().or(() ->
                buildValidator().validMessage());
    }
}
