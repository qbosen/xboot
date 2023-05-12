package top.abosen.xboot.statemachine.builder;

/**
 * To
 *
 * @author qiubaisen
 * @since 2023/5/12
 */
public interface To<S, E, C> {
    /**
     * Build transition event
     *
     * @param event transition event
     * @return On clause builder
     */
    On<S, E, C> on(E event);
}
