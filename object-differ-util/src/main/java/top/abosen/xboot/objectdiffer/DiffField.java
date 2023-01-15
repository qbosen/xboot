package top.abosen.xboot.objectdiffer;

import java.lang.annotation.*;

/**
 * @author qiubaisen
 * @date 2023/1/13
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@interface DiffField {

    String name() default "";

    boolean ignore() default false;

    /**
     * 当字段值被修改时, 用于描述该字段的解析类
     *
     * @return 具体解析类
     */
    Class<? extends DiffFieldParser> function() default DiffFieldParser.NONE.class;

}
