package top.abosen.xboot.statemachine.builder;

/**
 * Default fail callback, do nothing.
 *
 * @author 龙也
 * @since 2023/5/12
 */
public class NumbFailCallback<S, E, C> implements FailCallback<S, E, C> {

    @Override
    public void onFail(S sourceState, E event, C context) {
        //do nothing
    }
}
