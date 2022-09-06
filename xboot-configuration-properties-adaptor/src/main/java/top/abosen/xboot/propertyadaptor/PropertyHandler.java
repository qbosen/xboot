package top.abosen.xboot.propertyadaptor;

import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;

/**
 * @author qiubaisen
 * @date 2022/9/6
 */
@SuppressWarnings("UnnecessaryModifier")
interface PropertyHandler {
    /**
     * 是否进一步处理, 排除掉一定不满足的情况
     */
    boolean shouldHandle(IHandlerContext context);

    /**
     * @return 是否处理完成,不再接受其他处理器处理
     */
    boolean handle(IHandlerContext context);

    public static boolean hasGetterSetter(PropertyDescriptor descriptor) {
        return descriptor != null &&
                descriptor.getReadMethod() != null && descriptor.getReadMethod().getParameterCount() == 0 &&
                descriptor.getWriteMethod() != null && descriptor.getWriteMethod().getParameterCount() == 1;
    }

    public static Object get(PropertyDescriptor descriptor, Object object) {
        return ReflectionUtils.invokeMethod(descriptor.getReadMethod(), object);
    }

    public static void set(PropertyDescriptor descriptor, Object object, @Nullable Object value) {
        //noinspection RedundantArrayCreation
        ReflectionUtils.invokeMethod(descriptor.getWriteMethod(), object, new Object[]{value});
    }

}
