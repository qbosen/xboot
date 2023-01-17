package top.abosen.xboot.objectdiffer;

import java.lang.annotation.*;

/**
 * 用于在 collection/array 中标记同一个对象
 * @author qiubaisen
 * @date 2023/1/17
 */

@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DiffIdentity {
}
