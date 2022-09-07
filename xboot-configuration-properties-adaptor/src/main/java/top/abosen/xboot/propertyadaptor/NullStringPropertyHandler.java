package top.abosen.xboot.propertyadaptor;

import lombok.extern.apachecommons.CommonsLog;

import java.util.Objects;

/**
 * @author qiubaisen
 * @date 2022/9/6
 */
@CommonsLog
public class NullStringPropertyHandler implements PropertyHandler {

    /**
     * 无get,set方法, get,set方法参数个数不合法 不处理;
     * 基础类型,数组类型,注解类型,接口类型 不处理;
     * String类型处理; java包下不处理; 自定义组合类型 处理
     *
     * @param context 上下文
     * @return 是否进一步处理
     */
    @Override
    public boolean shouldHandle(IHandlerContext context) {
        Class<?> propertyType = context.getCurrentDescriptor().getPropertyType();
        if (propertyType.isPrimitive() || propertyType.isArray() || propertyType.isAnnotation() || propertyType.isEnum() || propertyType.isInterface()) {
            return false;
        }
        if (propertyType.isAssignableFrom(String.class)) {
            return true;
        }
        return !propertyType.getPackage().getName().startsWith("java.");
    }

    @Override
    public boolean handle(IHandlerContext context) {
        Object value = PropertyHandler.get(context.getCurrentDescriptor(), context.getCurrentParent());
        if (value == null) {
            return false;
        }

        if (String.class.equals(context.getCurrentDescriptor().getPropertyType()) &&
                Objects.equals(value, context.getProperties().getNullString()) &&
                context.matchCondition()
        ) {
            log.info(String.format("Set nullable config property [%s]", context.currentPath()));
            PropertyHandler.set(context.getCurrentDescriptor(), context.getCurrentParent(), null);
            return true;
        }
        return false;
    }
}
