package top.abosen.xboot.configutil;

import com.ecwid.consul.v1.ConsulClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.consul.ConsulAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author qiubaisen
 * @date 2021/11/12
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "spring.cloud.consul.config.enabled", matchIfMissing = true)
@EnableConfigurationProperties(ConsulConfigRegistryProperties.class)
@Import(ConsulAutoConfiguration.class)
public class ConsulConfigBootstrapConfiguration {
    @Bean
    public ConsulConfigPropertyLocator servicePropertyLocator(ConsulClient consul, ConsulConfigRegistryProperties properties) {
        return new ConsulConfigPropertyLocator(consul, properties);
    }
}
