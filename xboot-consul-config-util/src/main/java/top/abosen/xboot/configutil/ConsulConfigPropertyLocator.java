package top.abosen.xboot.configutil;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.health.HealthServicesRequest;
import com.ecwid.consul.v1.health.model.HealthService;
import com.ecwid.consul.v1.kv.model.GetValue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.cloud.consul.config.ConsulConfigProperties;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @author qiubaisen
 * @since 2021/11/12
 */
public class ConsulConfigPropertyLocator implements PropertySourceLocator {
    private static final Log log = LogFactory.getLog(ConsulConfigPropertyLocator.class);

    private final ConsulClient consul;
    private final ConsulConfigProperties configProperties;
    private final ConsulConfigRegistryProperties properties;

    public ConsulConfigPropertyLocator(ConsulClient consul, ConsulConfigProperties configProperties, ConsulConfigRegistryProperties properties) {
        this.consul = consul;
        this.configProperties = configProperties;
        this.properties = properties;
        this.properties.normalization();
    }

    //cant batch query but parallel
    @Override
    public PropertySource<?> locate(Environment environment) {
        if (environment instanceof ConfigurableEnvironment) {
            ConfigurableEnvironment env = (ConfigurableEnvironment) environment;
            HealthServicesRequest request = HealthServicesRequest.newBuilder()
                    .setPassing(properties.isQueryPassing())
                    .setToken(configProperties.getAclToken())
                    .build();
            Map<String, Object> sourceMap = new HashMap<>();
            MapPropertySource propertySource = new MapPropertySource("consulConfigUtil", sourceMap);

            BiConsumer<String, Object> setSourceValue = sourceMap::put;
            Function<String, Boolean> checkKeyDuplicate = alias -> sourceMap.containsKey(alias) || env.containsProperty(alias);

            // 键值
            Map<String, GetValue> kvValues = properties.getKv().keySet().stream().parallel()
                    .collect(Collectors.toMap(it -> it, key -> consul.getKVValue(key, configProperties.getAclToken()).getValue()));
            Function<String, Optional<String>> valueGetter = key -> Optional.ofNullable(kvValues.get(key)).map(it -> it.getDecodedValue(StandardCharsets.UTF_8));

            properties.getKv().forEach((key, kvc) -> kv(key, kvc, setSourceValue, checkKeyDuplicate, valueGetter));

            // 服务
            Map<String, List<HealthService>> serviceValues = properties.getService().keySet().stream().parallel()
                    .collect(Collectors.toMap(it -> it, key -> consul.getHealthServices(key, request).getValue()));
            Function<String, Optional<List<HealthService.Service>>> servicesGetter = key -> Optional.ofNullable(serviceValues.get(key))
                    .filter(it -> !it.isEmpty()).map(it -> it.stream().map(HealthService::getService).collect(Collectors.toList()));

            properties.getService().forEach((key, svc) -> svc(key, svc, setSourceValue, checkKeyDuplicate, servicesGetter));
            return propertySource;
        }
        return null;
    }

    protected static void svc(
            String key, ConsulConfigRegistryProperties.Service svc,
            BiConsumer<String, Object> setSourceValue,
            Function<String, Boolean> checkKeyDuplicate,
            Function<String, Optional<List<HealthService.Service>>> servicesGetter) {
        List<HealthService.Service> services = servicesGetter.apply(key)
                .orElseThrow(() -> new ConsulConfigException("consul config util can't found service: " + key));
        String alias = svc.getAlias();
        switch (svc.getAction()) {
            case JOIN_ALL:
                if (checkKeyDuplicate.apply(alias)) {
                    log.warn("duplicate config alias: " + alias + ", skip config this service");
                } else {
                    String value = services.stream().map(it -> it.getAddress() + ":" + it.getPort())
                            .collect(Collectors.joining(svc.getJoinWith()));
                    setSourceValue.accept(alias, value);
                }
                break;
            case RANDOM_HOST_PORT:
                String hostKey = alias + ".host";
                String portKey = alias + ".port";
                if (checkKeyDuplicate.apply(hostKey)) {
                    log.warn("duplicate config alias: " + hostKey + ", skip config this service");
                } else if (checkKeyDuplicate.apply(portKey)) {
                    log.warn("duplicate config alias: " + portKey + ", skip config this service");
                } else {
                    HealthService.Service picked = selectRandom(services);
                    setSourceValue.accept(hostKey, picked.getAddress());
                    setSourceValue.accept(portKey, picked.getPort());
                }
                break;
        }
    }

    protected static void kv(
            String key, ConsulConfigRegistryProperties.KV kvc,
            BiConsumer<String, Object> setSourceValue,
            Function<String, Boolean> checkKeyDuplicate,
            Function<String, Optional<String>> valueGetter) {
        String alias = kvc.getAlias();
        if (checkKeyDuplicate.apply(alias)) {
            log.warn("duplicate config alias: " + alias + ", skip config this key");
            return;
        }

        Optional<String> valueOpt = valueGetter.apply(key);
        if (!valueOpt.isPresent() && kvc.isMustExist()) {
            throw new ConsulConfigException("consul config util can't found key: " + key);
        }

        String value = valueOpt.orElse(null);
        if (kvc.isBlankToNull() && value != null && value.length() == 0) {
            value = null;
        }
        if (value == null) {
            value = kvc.getNullToValue();
        }
        setSourceValue.accept(alias, value);
    }


    private static  <T> T selectRandom(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }
}