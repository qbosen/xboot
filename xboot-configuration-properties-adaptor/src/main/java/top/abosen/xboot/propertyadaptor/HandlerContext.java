package top.abosen.xboot.propertyadaptor;

import lombok.Getter;

import java.beans.PropertyDescriptor;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

/**
 * @author qiubaisen
 * @since 2022/9/6
 */
@Getter
public class HandlerContext implements IHandlerContext {
    final Object bean;
    final String className;
    final Deque<String> path;
    final Set<String> handled;
    final PropertyAdaptorProperties properties;

    PropertyDescriptor currentDescriptor;
    Object currentParent;

    public HandlerContext(Object bean, String className, PropertyAdaptorProperties properties) {
        this.bean = bean;
        this.className = className;
        this.properties = properties;
        this.path = new ArrayDeque<>();
        this.handled = new HashSet<>();

        this.currentParent = bean;
        this.currentDescriptor = null;
    }


    public Object enterValue() {
        Object oldParent = this.currentParent;
        this.currentParent = PropertyHandler.get(currentDescriptor, currentParent);
        return oldParent;
    }

    public void leaveValue(Object oldParent) {
        this.currentParent = oldParent;
    }

    public void enterProperty(PropertyDescriptor descriptor) {
        this.currentDescriptor = descriptor;
        this.path.addLast(descriptor.getDisplayName());
    }

    public void leaveProperty() {
        this.currentDescriptor = null;
        if (path.isEmpty()) return;
        this.path.removeLast();
    }

    public boolean isHandled() {
        return handled.contains(currentPath());
    }

    public void markHandled() {
        handled.add(currentPath());
    }

    @Override
    public String currentPath() {
        return className + "#" + String.join(".", path);
    }


    @Override
    public boolean matchCondition() {
        if (properties.getCondition() == null || properties.getCondition().isEmpty()) {
            return true;
        }
        String path = currentPath();
        return properties.getCondition().stream().anyMatch(path::endsWith);
    }
}
