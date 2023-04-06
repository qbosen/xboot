package top.abosen.xboot.propertyadaptor;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author qiubaisen
 * @since 2022/6/21
 */

@AutoConfiguration
@EnableConfigurationProperties(PropertyAdaptorProperties.class)
@AutoConfigureBefore(PropertyPlaceholderAutoConfiguration.class)
@ConditionalOnProperty(value = PropertyAdaptorProperties.ENABLED, matchIfMissing = true)
public class PropertyAdaptorAutoConfiguration {

    @Bean
    public static ConfigurationPropertyBeanPostProcessor passwordBeanPostProcessor(
            PropertyAdaptorProperties properties,
            List<PropertyHandler> handlers) {
        return new ConfigurationPropertyBeanPostProcessor(properties, handlers);
    }

    @Bean
    public PropertyHandler nullStringHandler(){
        return new NullStringPropertyHandler();
    }
}
