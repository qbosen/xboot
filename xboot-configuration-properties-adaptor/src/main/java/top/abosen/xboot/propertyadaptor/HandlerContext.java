package top.abosen.xboot.propertyadaptor;

import lombok.Getter;

import java.beans.PropertyDescriptor;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

/**
 * @author qiubaisen
 * @date 2022/9/6
 */
@Getter
public class HandlerContext implements IHandlerContext {
    final Object bean;
    final String beanName;
    final Deque<String> path;
    final Set<String> handled;
    final PropertyAdaptorProperties properties;

    PropertyDescriptor currentDescriptor;
    Object currentParent;

    public HandlerContext(Object bean, String beanName, PropertyAdaptorProperties properties) {
        this.bean = bean;
        this.beanName = beanName;
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
        return beanName + "#" + String.join(".", path);
    }


}
