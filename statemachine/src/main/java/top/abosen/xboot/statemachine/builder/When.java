package top.abosen.xboot.statemachine.builder;


import top.abosen.xboot.statemachine.Action;

/**
 * When
 *
 * @author qiubaisen
 * @since 2023/5/12
 */
public interface When<S, E, C> {
    /**
     * Define action to be performed during transition
     *
     * @param action performed action
     */
    void perform(Action<S, E, C> action);
}
