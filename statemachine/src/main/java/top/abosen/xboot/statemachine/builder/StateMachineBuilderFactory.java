package top.abosen.xboot.statemachine.builder;

/**
 * StateMachineBuilderFactory
 *
 * @author qiubaisen
 * @since 2023/5/12
 */
public class StateMachineBuilderFactory {
    public static <S, E, C> StateMachineBuilder<S, E, C> create() {
        return new StateMachineBuilderImpl<>();
    }
}
