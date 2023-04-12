package top.abosen.xboot.extensionfield.extension;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import com.google.auto.service.AutoService;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "列表扩展字段; 当前字段是target扩展字段的集合;" ,example = "{\"@type\":\"list\",\"key\":\"hobby-movie\",\"name\":\"爱好电影\",\"minSize\":null,\"maxSize\":10,\"target\":{\"@type\":\"map\",\"key\":\"nested-hobby\",\"name\":null,\"fields\":[{\"@type\":\"simple\",\"key\":\"name\",\"name\":\"名称\",\"schema\":{\"@type\":\"string\",\"required\":true,\"defaultValue\":null,\"minLength\":1,\"maxLength\":20,\"regex\":null},\"widget\":{\"@type\":\"input\",\"name\":\"电影名\",\"style\":null,\"multiple\":false}},{\"@type\":\"simple\",\"key\":\"score\",\"name\":\"评分\",\"schema\":{\"@type\":\"integer\",\"required\":true,\"defaultValue\":3,\"min\":0,\"max\":5},\"widget\":{\"@type\":\"option\",\"name\":\"五星评分\",\"style\":\"star\",\"multiple\":false,\"options\":{\"一星\":1,\"二星\":2,\"三星\":3,\"四星\":4,\"五星\":5}}}]},\"desc\":\"爱好电影列表,包含名字和评分,最多10个\"}")
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
