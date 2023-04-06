package top.abosen.xboot.configutil;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.Map;

import static top.abosen.xboot.configutil.ConsulConfigRegistryProperties.PREFIX;

/**
 * @author qiubaisen
 * @since 2021/11/12
 */
@ConfigurationProperties(PREFIX)
@Data
public class ConsulConfigRegistryProperties {
    public static final String PREFIX = "spring.cloud.consul-util";

    private boolean queryPassing = true;

    private Map<String, KV> kv = Collections.emptyMap();
    private Map<String, Service> service = Collections.emptyMap();

    /**
     * 归一化配置，对于未配置别名的，均按key名称处理
     */
    public void normalization() {
        for (Map.Entry<String, KV> entry : kv.entrySet()) {
            if (entry.getValue() == null) {
                entry.setValue(new KV());
            }
            if (ALIAS_SAME_TO_KEY.equals(entry.getValue().getAlias())) {
                entry.getValue().setAlias(entry.getKey());
            }
        }
        for (Map.Entry<String, Service> entry : service.entrySet()) {
            if (entry.getValue() == null) {
                entry.setValue(new Service());
            }
            if (ALIAS_SAME_TO_KEY.equals(entry.getValue().getAlias())) {
                entry.getValue().setAlias(entry.getKey());
            }
        }
    }

    public static final String ALIAS_SAME_TO_KEY = "@@ALIAS_SAME_TO_KEY@@";

    @Data
    @NoArgsConstructor
    public static class KV {
        private String alias = ALIAS_SAME_TO_KEY;
        /**
         * kv 配置中存在key时，value为空串，有时候业务上表示为未配置，此时转换为null
         */
        private boolean blankToNull = true;
        /**
         * blankToNull 之后,如果值为null,可以进行映射
         */
        private String nullToValue = null;
        /**
         * true: 如果不存在对应的key, 会抛出异常
         */
        private boolean mustExist = false;

        /**
         * 如果什么都不配置，或者无法解析为对象，使用默认配置
         */
        public KV(String string) {

        }
    }

    @Data
    @NoArgsConstructor
    public static class Service {
        public enum Action {
            /**
             * 默认选项. 随机选一个服务, 追加 [alias].host/[alias].port 配置
             */
            RANDOM_HOST_PORT,
            /**
             * 追加[alias]配置: 按 host1:port1()host2:port2()host3:port3 拼接, 拼接参数为 {@link Service#joinWith}
             */
            JOIN_ALL,
        }

        private String alias = ALIAS_SAME_TO_KEY;
        private Action action = Action.RANDOM_HOST_PORT;
        /**
         * <p>
         * elasticsearch, redis 按 `,` 拼接
         * </p>
         * <p>
         * rocketmq 按 `;` 拼接
         * </p>
         */
        private String joinWith = ",";

        /**
         * 如果什么都不配置，或者无法解析为对象，使用默认配置
         */
        public Service(String string) {

        }
    }
}
