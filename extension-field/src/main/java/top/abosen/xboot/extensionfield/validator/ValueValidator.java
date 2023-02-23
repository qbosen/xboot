package top.abosen.xboot.extensionfield.validator;

import cn.hutool.core.util.StrUtil;
import lombok.Value;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * 值校验器
 * <p>
 * 除了显式的空值校验器,其他校验器都应该忽略空值,即校验通过
 */
public interface ValueValidator {
    boolean valid(Object value);

}
