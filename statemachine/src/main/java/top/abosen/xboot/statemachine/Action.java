package top.abosen.xboot.statemachine;

/**
 * Generic strategy interface used by a state machine to respond
 * events by executing an {@code Action}
 *
 * @author qiubaisen
 * @since 2023/5/12
 */
public interface Action<S, E, C> {

    void execute(S from, S to, E event, C context);

}
