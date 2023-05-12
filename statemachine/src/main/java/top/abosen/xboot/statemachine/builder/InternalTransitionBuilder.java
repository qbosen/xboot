package top.abosen.xboot.statemachine.builder;

/**
 * InternalTransitionBuilder
 *
 * @author qiubaisen
 * @since 2023/5/12
 */
public interface InternalTransitionBuilder<S, E, C> {
    /**
     * Build a internal transition
     *
     * @param stateId id of transition
     * @return To clause builder
     */
    To<S, E, C> within(S stateId);
}
