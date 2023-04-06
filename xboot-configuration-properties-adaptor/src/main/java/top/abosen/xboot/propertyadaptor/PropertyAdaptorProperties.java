package top.abosen.xboot.propertyadaptor;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

/**
 * @author qiubaisen
 * @since 2022/6/21
 */
@Data
@ConfigurationProperties(prefix = PropertyAdaptorProperties.PREFIX)
public class PropertyAdaptorProperties {
    public static final String PREFIX = "property-adaptor";
    public static final String ENABLED = PREFIX + ".enabled";
    private boolean enabled;
    private String nullString = "@null";
    /**
     * 按照签名结尾字符串进行限制,默认不限制
     * <p></p>
     */
    private Set<String> condition = new HashSet<>();
}
