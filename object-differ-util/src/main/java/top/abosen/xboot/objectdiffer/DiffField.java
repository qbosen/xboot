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
public @interface DiffField {

    /**
     * 字段被{@link Id}标注,则对比在上一级{@code parent}结束;
     * <p>
     * 如何描述 {@code parent} 对比的差异 由 {@link MessageType}决定
     * <p>
     * <font color="red">此注解使得 同级或下级 {@link DiffField}标记的字段 不再参与对比</font>
     */
    @Documented
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    public @interface Id {

    }

    @Documented
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @interface Format {
        Source source() default Source.IGNORE;

        /**
         * 当前对象的方法名,用于将当前对象变为字符串
         * <p>
         * 要求该方法无参
         *
         * @return 方法名
         */
        String methodSource() default "toString";

        /**
         * 注册到上下文的处理对象名称, 为{@link FormatProvider} 类型
         * <p>
         * 通常用于处理或查询复杂对象,比如通过id获取完整描述,可与其他ioc容器结合使用
         *
         * @return 注册的处理对象名
         */
        String instanceSource() default "";


        enum Source {
            METHOD, INSTANCE, IGNORE;

            public boolean handle() {
                return this != IGNORE;
            }
        }
    }

    String name() default "";

    boolean ignore() default false;

    Format format() default @Format();
}
