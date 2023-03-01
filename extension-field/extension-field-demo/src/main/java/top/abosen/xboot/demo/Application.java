package top.abosen.xboot.demo;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import top.abosen.xboot.extensionfield.jackson.DynamicSubtypeModule;

import javax.annotation.PostConstruct;
import java.util.Iterator;

/**
 * @author qiubaisen
 * @date 2023/2/28
 */

@SpringBootApplication
@MapperScan(markerInterface = BaseMapper.class)
public class Application implements ApplicationContextAware {

    public static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Application.context = applicationContext;
    }

    @Bean
    Jackson2ObjectMapperBuilderCustomizer configObjectMapper() {
        return builder -> {
            // 设置序列化命名策略
            builder.propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
            // 注册 动态子类型模块
            builder.modules(new DynamicSubtypeModule());
            // 将配置好的 ObjectMapper 应用到 JacksonTypeHandler
            builder.postConfigurer(JacksonTypeHandler::setObjectMapper);
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
