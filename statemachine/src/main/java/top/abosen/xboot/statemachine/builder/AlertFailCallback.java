package top.abosen.xboot.statemachine.builder;

import top.abosen.xboot.statemachine.exception.TransitionFailException;

/**
 * Alert fail callback, throw an {@code TransitionFailException}
 *
 * @author 龙也
 * @since 2023/5/12
 */
public class AlertFailCallback<S, E, C> implements FailCallback<S, E, C> {

    @Override
    public void onFail(S sourceState, E event, C context) {
        throw new TransitionFailException(
                "Cannot fire event [" + event + "] on current state [" + sourceState + "] with context [" + context + "]"
        );
    }
}
