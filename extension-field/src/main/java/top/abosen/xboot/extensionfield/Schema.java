package top.abosen.xboot.extensionfield;

import cn.hutool.core.util.StrUtil;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author qiubaisen
 * @date 2023/2/21
 */
public interface Schema {

}
interface SchemaValidator{
    boolean valid(Object value);
}

interface SchemaNonnullValidator extends SchemaValidator{
    @Override
    default boolean valid(Object value) {
        return Objects.nonNull(value);
    }
}

interface RegexValidator extends SchemaValidator{
    String getRegex();

    @Override
    default boolean valid(Object value) {
        String regex = getRegex();
        if(StrUtil.isBlank(regex)) return true;
        return Pattern.matches(regex, String.valueOf(value));
    }
}