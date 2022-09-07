package top.abosen.xboot.propertyadaptor;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.beans.PropertyDescriptor;
import java.util.List;

/**
 * @author qiubaisen
 * @date 2022/6/21
 * @see
 */
@CommonsLog
public class ConfigurationPropertyBeanPostProcessor implements BeanPostProcessor {

    private final PropertyAdaptorProperties properties;
    private final List<PropertyHandler> handlerList;

    public ConfigurationPropertyBeanPostProcessor(PropertyAdaptorProperties properties, List<PropertyHandler> handlerList) {
        this.properties = properties;
        this.handlerList = handlerList;
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        if (properties.isEnabled() && targetClass.isAnnotationPresent(ConfigurationProperties.class)) {
            recursiveDeal(new HandlerContext(bean, targetClass.getName(), properties));
        }
        return bean;
    }

    private void recursiveDeal(HandlerContext context) {
        if (context.getCurrentParent() == null) return;
        for (PropertyDescriptor descriptor : BeanUtils.getPropertyDescriptors(context.getCurrentParent().getClass())) {
            if (!PropertyHandler.hasGetterSetter(descriptor)) {
                continue;
            }
            for (PropertyHandler propertyHandler : handlerList) {
                context.enterProperty(descriptor);
                try {
                    if (propertyHandler.shouldHandle(context)) {
                        if (context.isHandled()) {
                            // 跳出handlers, 继续下一个属性
                            break;
                        }
                        boolean handled = propertyHandler.handle(context);
                        if (handled) {
                            context.markHandled();
                        } else {
                            Object oldParent = context.enterValue();
                            try {
                                recursiveDeal(context);
                            } finally {
                                context.leaveValue(oldParent);
                            }
                        }

                    }
                } finally {
                    context.leaveProperty();
                }
            }

        }
    }


}
