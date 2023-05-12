package top.abosen.xboot.statemachine;

/**
 * Condition
 *
 * @author qiubaisen
 * @since 2023/5/12
 */
public interface Condition<C> {

    /**
     * @param context context object
     * @return whether the context satisfied current condition
     */
    boolean isSatisfied(C context);

    default String name() {
        return this.getClass().getSimpleName();
    }
}