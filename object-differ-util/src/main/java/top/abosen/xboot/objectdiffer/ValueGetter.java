package top.abosen.xboot.objectdiffer;

import de.danielbechler.util.Exceptions;

import java.util.Objects;

/**
 * @author qiubaisen
 * @date 2023/1/18
 */
interface ValueGetter {

    default boolean isEquals(Object working, Object base) {
        try {
            Object workingValue = getValue(working);
            Object baseValue = getValue(base);
            return Objects.equals(workingValue, baseValue);
        } catch (Exception e) {
            throw Exceptions.escalate(e);
        }
    }

    Object getValue(Object target) throws Exception;

    static ValueGetter filterNull(ValueGetter valueGetter) {
        return source -> source == null ? null : valueGetter.getValue(source);
    }
}
