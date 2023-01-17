package top.abosen.xboot.objectdiffer;

import java.lang.annotation.*;

/**
 * 可作用于属性 和 类型, 属性先于类型
 *
 * @author qiubaisen
 * @date 2023/1/13
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Diff {

    /**
     * 空则使用字段 或 类型名
     *
     * @return 名称
     */
    String displayName() default "";

    /**
     * @return 是否忽略该字段/类型的对比
     */
    boolean ignore() default false;

    /**
     * @return 值处理的元信息
     */
    DiffValue format() default @DiffValue(source = SourceType.IGNORE);
}
