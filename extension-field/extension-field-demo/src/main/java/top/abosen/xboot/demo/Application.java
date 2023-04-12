package top.abosen.xboot.demo;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import top.abosen.xboot.extensionfield.jackson.DynamicSubtypeModule;
import top.abosen.xboot.extensionfield.mybatis.ExtensionFieldsTypeHandler;

/**
 * @author qiubaisen
 * @since 2023/2/28
 */

@SpringBootApplication
@MapperScan(markerInterface = BaseMapper.class)
public class Application implements ApplicationContextAware {

    public static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Application.context = applicationContext;
    }

    /**
     * 主要目的:
     * <p>
     * 1. 给 {@link com.fasterxml.jackson.databind.ObjectMapper} 注册 {@link DynamicSubtypeModule} 模块
     * <p>
     * 2. 将配置好的 objectMapper 应用到 {@link MappingJackson2HttpMessageConverter} 用于REST接口
     * <p>
     * 3. 将配置好的 objectMapper 应用到 {@link ExtensionFieldsTypeHandler} 用于 mybatis 转换存储
     */
    @Bean
    Jackson2ObjectMapperBuilderCustomizer configObjectMapper() {
        return builder -> {
            // 设置序列化命名策略
            builder.propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
            // 注册 动态子类型模块
            builder.modules(new DynamicSubtypeModule());
            // 将配置好module的 ObjectMapper 应用到 ExtensionFieldsTypeHandler
            builder.postConfigurer(ExtensionFieldsTypeHandler::setObjectMapper);
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
