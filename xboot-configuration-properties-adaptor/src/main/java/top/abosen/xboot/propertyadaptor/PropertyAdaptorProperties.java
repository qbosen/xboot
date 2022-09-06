package top.abosen.xboot.propertyadaptor;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author qiubaisen
 * @date 2022/6/21
 */
@Data
@ConfigurationProperties(prefix = PropertyAdaptorProperties.PREFIX)
public class PropertyAdaptorProperties {
    public static final String PREFIX = "property-adaptor";
    public static final String ENABLED = PREFIX + ".enabled";
    private boolean enabled;
    private String nullString = "@null";
}
