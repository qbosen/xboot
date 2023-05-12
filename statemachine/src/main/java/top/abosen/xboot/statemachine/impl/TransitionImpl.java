package top.abosen.xboot.statemachine.impl;

import lombok.EqualsAndHashCode;
import top.abosen.xboot.statemachine.Action;
import top.abosen.xboot.statemachine.Condition;
import top.abosen.xboot.statemachine.State;
import top.abosen.xboot.statemachine.Transition;

/**
 * TransitionImpl。
 * <p>
 * This should be designed to be immutable, so that there is no thread-safe risk
 *
 * @author qiubaisen
 * @since 2023/5/12
 */
@EqualsAndHashCode(of = {"event", "source", "target"})
public class TransitionImpl<S, E, C> implements Transition<S, E, C> {

    private State<S, E, C> source;

    private State<S, E, C> target;

    private E event;

    private Condition<C> condition;

    private Action<S, E, C> action;

    private TransitionType type = TransitionType.EXTERNAL;

    @Override
    public State<S, E, C> getSource() {
        return source;
    }

    @Override
    public void setSource(State<S, E, C> state) {
        this.source = state;
    }

    @Override
    public E getEvent() {
        return this.event;
    }

    @Override
    public void setEvent(E event) {
        this.event = event;
    }

    @Override
    public void setType(TransitionType type) {
        this.type = type;
    }

    @Override
    public State<S, E, C> getTarget() {
        return this.target;
    }

    @Override
    public void setTarget(State<S, E, C> target) {
        this.target = target;
    }

    @Override
    public Condition<C> getCondition() {
        return this.condition;
    }

    @Override
    public void setCondition(Condition<C> condition) {
        this.condition = condition;
    }

    @Override
    public Action<S, E, C> getAction() {
        return this.action;
    }

    @Override
    public void setAction(Action<S, E, C> action) {
        this.action = action;
    }

    @Override
    public State<S, E, C> transit(C ctx, boolean checkCondition) {
        Debugger.debug("Do transition: " + this);
        this.verify();
        if (!checkCondition || condition == null || condition.isSatisfied(ctx)) {
            if (action != null) {
                action.execute(source.getId(), target.getId(), event, ctx);
            }
            return target;
        }

        Debugger.debug("Condition is not satisfied, stay at the " + source + " state ");
        return source;
    }

    @Override
    public final String toString() {
        return source + "-[" + event.toString() + ", " + type + "]->" + target;
    }

    @Override
    public void verify() {
        if (type == TransitionType.INTERNAL && source != target) {
            throw new StateMachineException(String.format("Internal transition source state '%s' " +
                    "and target state '%s' must be same.", source, target));
        }
    }
}
