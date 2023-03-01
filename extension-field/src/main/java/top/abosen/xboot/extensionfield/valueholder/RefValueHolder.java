package top.abosen.xboot.extensionfield.valueholder;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author qiubaisen
 * @date 2023/2/28
 */


public class RefValueHolder implements ValueHolder {

    final AtomicReference<Object> ref;

    private RefValueHolder(Object target) {
        this.ref = new AtomicReference<>(target);
    }

    public static ValueHolder of(Object target) {
        return new RefValueHolder(target);
    }


    @Override
    public Object get() {
        return ref.get();
    }

    @Override
    public void set(Object value) {
        ref.set(value);
    }
}
