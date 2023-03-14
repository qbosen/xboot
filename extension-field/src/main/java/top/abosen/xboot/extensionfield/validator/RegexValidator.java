package top.abosen.xboot.extensionfield.validator;

import cn.hutool.core.util.StrUtil;
import lombok.Value;

import java.util.regex.Pattern;

/**
 * @author qiubaisen
 * @date 2023/2/23
 */
@Value
public
class RegexValidator implements ValueValidator {
    String regex;

    @Override
    public boolean valid(Object value) {
        if (StrUtil.isEmptyIfStr(value) || StrUtil.isBlank(regex)) return true;
        return Pattern.matches(regex, String.valueOf(value));
    }
}
