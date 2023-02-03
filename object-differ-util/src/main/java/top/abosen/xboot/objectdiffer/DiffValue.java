package top.abosen.xboot.objectdiffer;

import java.lang.annotation.*;

/**
 * @author qiubaisen
 * @date 2023/1/16
 */
@Documented
@Target({})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DiffValue {

    /**
     * {@link SourceType#IGNORE} 时尝试深入对比该对象, 否则根据根据source返回当前对象对比情况
     *
     * @return 值来源
     */
    SourceType source();


    /**
     * 当前对象的方法名,用于将当前对象变为字符串
     * <p>
     * 要求该方法无参
     *
     * @return 方法名
     */
    String methodName() default "toString";

    /**
     * 注册到上下文的处理对象名称, 为{@link ValueProvider} 类型
     * <p>
     * 通常用于处理或查询复杂对象,比如通过id获取完整描述,可与其他ioc容器结合使用
     *
     * @return 注册的处理对象名
     */
    String providerName() default "";


}