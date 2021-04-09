package top.abosen.dddboot.spring.configration;

import org.springframework.context.annotation.Import;
import top.abosen.dddboot.spring.repository.mybatisplus.MybatisPlusConfiguration;

import java.lang.annotation.*;

/**
 * @author qiubaisen
 * @date 2021/4/12
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(MybatisPlusConfiguration.class)
public @interface EnableMybatisPlusConfiguration {
}
