package top.abosen.xboot.statemachine.builder;

/**
 * From
 *
 * @author qiubaisen
 * @since 2023/5/12
 */
public interface From<S, E, C> {
    /**
     * Build transition target state and return to clause builder
     *
     * @param stateId id of state
     * @return To clause builder
     */
    To<S, E, C> to(S stateId);

}
