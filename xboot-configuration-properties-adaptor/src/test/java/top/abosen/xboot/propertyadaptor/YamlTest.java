package top.abosen.xboot.propertyadaptor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ByteArrayResource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author qiubaisen
 * @date 2022/9/6
 */
class YamlTest {
    @Test
    public void yaml_null_will_be_parsed_as_empty_string() throws Exception {
        YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
        ByteArrayResource resource = new ByteArrayResource("foo:\n  bar: null\n  cool: ~".getBytes());
        PropertySource<?> source = loader.load("resource", resource).get(0);
//        assertNull(source.getProperty("foo.cool"));
        assertEquals("", source.getProperty("foo.bar"));
        assertEquals("", source.getProperty("foo.cool"));
    }


    public static ConfigurationPropertyBeanPostProcessor postProcessor(boolean enabled, String nullString, PropertyHandler... handlers) {
        PropertyAdaptorProperties config = new PropertyAdaptorProperties();
        config.setEnabled(enabled);
        config.setNullString(nullString);
        return new ConfigurationPropertyBeanPostProcessor(config, Arrays.asList(handlers));
    }


}