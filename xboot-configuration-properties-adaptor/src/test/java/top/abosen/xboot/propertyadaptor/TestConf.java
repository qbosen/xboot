package top.abosen.xboot.propertyadaptor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author qiubaisen
 * @date 2022/9/6
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties
public class TestConf {
    String str;
    int integer;
    TestConf nest;

    public TestConf(String str, TestConf nest) {
        this.str = str;
        this.nest = nest;
    }

}
