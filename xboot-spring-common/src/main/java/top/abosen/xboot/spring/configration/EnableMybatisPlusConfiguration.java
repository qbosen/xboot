package top.abosen.xboot.spring.configration;

import org.springframework.context.annotation.Import;
import top.abosen.xboot.spring.repository.mybatisplus.MybatisPlusConfiguration;

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
