package top.abosen.xboot.propertyadaptor;

import lombok.experimental.Delegate;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 未启用; 通过代理转换器达到修改属性值的效果,影响较大
 * @author qiubaisen
 * @date 2022/6/21
 */
@Deprecated
public class ConversionHack implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment currentEnv = event.getEnvironment();
        currentEnv.setConversionService(new CustConversionService(currentEnv.getConversionService()));
    }


    public static class CustConversionService implements ConfigurableConversionService {
        @Delegate(excludes = Convert.class)
        public final ConfigurableConversionService delegate;

        public interface Convert {
            <T> T convert(Object source, Class<T> targetType);
        }

        public CustConversionService(ConfigurableConversionService delegate) {
            this.delegate = delegate;
        }

        @Override
        public <T> T convert(Object source, Class<T> targetType) {
            T result = delegate.convert(source, targetType);
            if (targetType.equals(String.class) && "@null".equals(result)) {
                return null;
            }
            return result;
        }
    }


}
