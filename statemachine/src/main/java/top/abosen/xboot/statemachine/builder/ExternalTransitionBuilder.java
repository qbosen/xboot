package top.abosen.xboot.statemachine.builder;

/**
 * ExternalTransitionBuilder
 *
 * @author qiubaisen
 * @since 2023/5/12
 */
public interface ExternalTransitionBuilder<S, E, C> {
    /**
     * Build transition source state.
     *
     * @param stateId id of state
     * @return from clause builder
     */
    From<S, E, C> from(S stateId);

}
