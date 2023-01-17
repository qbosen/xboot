package top.abosen.xboot.objectdiffer;

import java.lang.annotation.*;

/**
 * 如果标注在方法(可私有 非继承)上, 该无参方法的返回值进行 {@code Objects.equals}比较
 * <p>
 * 如果标注在字段上, 多个字段值{@code equals}时 视为对象{@code equals}
 *
 * @author qiubaisen
 * @date 2023/1/17
 */

@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DiffEquals {
}
