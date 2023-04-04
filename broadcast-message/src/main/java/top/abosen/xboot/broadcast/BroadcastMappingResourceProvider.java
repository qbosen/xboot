package top.abosen.xboot.broadcast;

import com.google.common.annotations.VisibleForTesting;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author qiubaisen
 * @since 2023/3/27
 */
@Slf4j
public class BroadcastMappingResourceProvider implements BroadcastMappingProvider {
    private static final String BROADCAST_PROPERTIES = "META-INF/broadcast.properties";

    @Getter(lazy = true)
    private final Properties broadcastMapping = loadBroadcastMapping();

    @VisibleForTesting
    protected static final Map<String, String> TEST_MAPPING_ONLY = new HashMap<>();

    private static Properties loadBroadcastMapping() {

        Properties props = new Properties();
        if (!TEST_MAPPING_ONLY.isEmpty()) {
            props.putAll(TEST_MAPPING_ONLY);
            log.info("load custom broadcast mapping ONLY: {}", TEST_MAPPING_ONLY);
            return props;
        }

        try {
            Enumeration<URL> broadcasts = Thread.currentThread().getContextClassLoader()
                    .getResources(BROADCAST_PROPERTIES);
            while (broadcasts.hasMoreElements()) {
                URL broadcast = broadcasts.nextElement();
                try (InputStream stream = broadcast.openStream()) {
                    props.load(stream);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("无法加载 broadcast.properties 配置", e);
        }
        return props;
    }
}
