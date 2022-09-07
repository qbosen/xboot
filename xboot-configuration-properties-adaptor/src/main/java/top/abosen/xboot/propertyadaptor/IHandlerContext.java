package top.abosen.xboot.propertyadaptor;

import java.beans.PropertyDescriptor;

/**
 * @author qiubaisen
 * @date 2022/9/6
 */
public interface IHandlerContext {
    PropertyAdaptorProperties getProperties();

    PropertyDescriptor getCurrentDescriptor();

    Object getCurrentParent();

    String currentPath();

    boolean matchCondition();
}
