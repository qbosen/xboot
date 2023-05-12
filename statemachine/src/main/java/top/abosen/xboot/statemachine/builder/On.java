package top.abosen.xboot.statemachine.builder;


import top.abosen.xboot.statemachine.Condition;

/**
 * On
 *
 * @author qiubaisen
 * @since 2023/5/12
 */
public interface On<S, E, C> extends When<S, E, C> {
    /**
     * Add condition for the transition
     *
     * @param condition transition condition
     * @return When clause builder
     */
    When<S, E, C> when(Condition<C> condition);
}
