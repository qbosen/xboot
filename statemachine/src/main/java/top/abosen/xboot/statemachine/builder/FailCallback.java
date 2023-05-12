package top.abosen.xboot.statemachine.builder;

/**
 * FailCallback
 *
 * @author 龙也
 * @since 2023/5/12
 */
@FunctionalInterface
public interface FailCallback<S, E, C> {

    /**
     * Callback function to execute if failed to trigger an Event
     *
     * @param sourceState
     * @param event
     * @param context
     */
    void onFail(S sourceState, E event, C context);
}
