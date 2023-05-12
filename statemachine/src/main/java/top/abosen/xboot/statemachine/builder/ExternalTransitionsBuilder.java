package top.abosen.xboot.statemachine.builder;

/**
 * ExternalTransitionsBuilder
 * <p>
 * This builder is for multiple transitions, currently only support multiple sources {@code <---->} one target
 *
 * @author qiubaisen
 * @since 2023/5/12
 */
public interface ExternalTransitionsBuilder<S, E, C> {

    From<S, E, C> fromAmong(S... stateIds);
}
