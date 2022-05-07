package top.abosen.xboot.configutil;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.health.HealthServicesRequest;
import com.ecwid.consul.v1.health.model.HealthService;
import com.ecwid.consul.v1.kv.model.GetValue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;


/**
 * @author qiubaisen
 * @date 2021/11/12
 */
public class ConsulConfigPropertyLocator implements PropertySourceLocator {
    private static final Log log = LogFactory.getLog(ConsulConfigPropertyLocator.class);

    private final ConsulClient consul;
    private final ConsulConfigRegistryProperties properties;

    public ConsulConfigPropertyLocator(ConsulClient consul, ConsulConfigRegistryProperties properties) {
        this.consul = consul;
        this.properties = properties;
        this.properties.normalization();
    }

    @Override
    public PropertySource<?> locate(Environment environment) {
        if (environment instanceof ConfigurableEnvironment) {
            ConfigurableEnvironment env = (ConfigurableEnvironment) environment;
            HealthServicesRequest request = HealthServicesRequest.newBuilder().setPassing(properties.isQueryPassing()).build();
            Map<String, Object> sourceMap = new HashMap<>();
            MapPropertySource propertySource = new MapPropertySource("consulConfigUtil", sourceMap);

            properties.getKv().forEach((key, kvc) -> {
                GetValue getValue = consul.getKVValue(key).getValue();
                String alias = kvc.getAlias();
                String value = null;
                if (sourceMap.containsKey(alias) || env.containsProperty(alias)) {
                    log.warn("duplicate config alias: " + alias + ", skip config this key");
                } else if (getValue == null && kvc.isMustExist()) {
                    throw new ConsulConfigException("consul config util can't found key: " + key);
                } else {
                    if (getValue != null) {
                        value = getValue.getDecodedValue(StandardCharsets.UTF_8);
                    }
                    if (kvc.isBlankToNull() && value != null && value.length() == 0) {
                        value = null;
                    }
                }
                sourceMap.put(alias, value);
            });

            properties.getService().forEach((key, alias) -> {
                List<HealthService> services = consul.getHealthServices(key, request).getValue();
                if (services == null || services.isEmpty()) {
                    throw new ConsulConfigException("consul config util can't found service: " + key);
                } else {
                    if (sourceMap.containsKey(alias) || env.containsProperty(alias)) {
                        log.warn("duplicate config alias: " + alias + ", skip config this service");
                    } else {
                        HealthService.Service service = selectRandom(services).getService();
                        sourceMap.put(alias + ".host", service.getAddress());
                        sourceMap.put(alias + ".port", service.getPort());
                    }
                }
            });
            return propertySource;
        }
        return null;
    }

    private <T> T selectRandom(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }
}