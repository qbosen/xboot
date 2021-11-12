package top.abosen.dddboot.configutil;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.Map;

import static top.abosen.dddboot.configutil.ConsulConfigRegistryProperties.PREFIX;

/**
 * @author qiubaisen
 * @date 2021/11/12
 */
@ConfigurationProperties(PREFIX)
@Data
public class ConsulConfigRegistryProperties {
    public static final String PREFIX = "spring.cloud.consul-util";

    private boolean queryPassing = true;

    private Map<String, KV> kv = Collections.emptyMap();
    private Map<String, String> service = Collections.emptyMap();

    /**
     * 归一化配置，对于未配置别名的，均按key名称处理
     */
    public void normalization() {
        for (Map.Entry<String, KV> entry : kv.entrySet()) {
            if (entry.getValue() == null) {
                entry.setValue(new KV());
            }
            if (KV.ALIAS_SAME_TO_KEY.equals(entry.getValue().getAlias())) {
                entry.getValue().setAlias(entry.getKey());
            }
        }
        for (Map.Entry<String, String> entry : service.entrySet()) {
            if (entry.getValue() == null || entry.getValue().length() == 0) {
                entry.setValue(entry.getKey());
            }
        }
    }

    @Data @NoArgsConstructor
    public static class KV {
        public static final String ALIAS_SAME_TO_KEY = "@@ALIAS_SAME_TO_KEY@@";
        private String alias = ALIAS_SAME_TO_KEY;
        /**
         * kv 配置中存在key时，value为空串，有时候业务上表示为未配置，此时转换为null
         */
        private boolean blankToNull = true;
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
}
