package top.abosen.xboot.extensionfield.jackson;

import com.fasterxml.jackson.annotation.JacksonAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author qiubaisen
 * @date 2023/2/22
 */

@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface JsonSubType {
    /**
     * 通过该名称确定具体的类型,空不处理
     *
     * @return 子类型名称
     */
    String[] value() default {};
}
