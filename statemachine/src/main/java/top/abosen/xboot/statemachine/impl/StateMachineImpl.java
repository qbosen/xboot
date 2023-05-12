package top.abosen.xboot.statemachine.impl;

import top.abosen.xboot.statemachine.State;
import top.abosen.xboot.statemachine.StateMachine;
import top.abosen.xboot.statemachine.Transition;
import top.abosen.xboot.statemachine.Visitor;
import top.abosen.xboot.statemachine.builder.FailCallback;

import java.util.List;
import java.util.Map;

/**
 * For performance consideration,
 * The state machine is made "stateless" on purpose.
 * Once it's built, it can be shared by multi-thread
 * <p>
 * One side effect is since the state machine is stateless, we can not get current state from State Machine.
 *
 * @author qiubaisen
 * @since 2023/5/12
 */
public class StateMachineImpl<S, E, C> implements StateMachine<S, E, C> {

    private final Map<S, State<S, E, C>> stateMap;
    private String machineId;
    private boolean ready;

    private FailCallback<S, E, C> failCallback;

    public StateMachineImpl(Map<S, State<S, E, C>> stateMap) {
        this.stateMap = stateMap;
    }

    @Override
    public boolean verify(S sourceStateId, E event) {
        isReady();

        State<S, E, C> sourceState = getState(sourceStateId);

        List<Transition<S, E, C>> transitions = sourceState.getEventTransitions(event);

        return transitions != null && transitions.size() != 0;
    }

    @Override
    public S fireEvent(S sourceStateId, E event, C ctx) {
        isReady();
        Transition<S, E, C> transition = routeTransition(sourceStateId, event, ctx);

        if (transition == null) {
            Debugger.debug("There is no Transition for " + event);
            failCallback.onFail(sourceStateId, event, ctx);
            return sourceStateId;
        }

        return transition.transit(ctx, false).getId();
    }

    private Transition<S, E, C> routeTransition(S sourceStateId, E event, C ctx) {
        State<S, E, C> sourceState = getState(sourceStateId);

        List<Transition<S, E, C>> transitions = sourceState.getEventTransitions(event);

        if (transitions == null || transitions.size() == 0) {
            return null;
        }

        Transition<S, E, C> transit = null;
        for (Transition<S, E, C> transition : transitions) {
            if (transition.getCondition() == null) {
                transit = transition;
            } else if (transition.getCondition().isSatisfied(ctx)) {
                transit = transition;
                break;
            }
        }

        return transit;
    }

    private State<S, E, C> getState(S currentStateId) {
        return StateHelper.getState(stateMap, currentStateId);
    }

    private void isReady() {
        if (!ready) {
            throw new StateMachineException("State machine is not built yet, can not work");
        }
    }

    @Override
    public String accept(Visitor visitor) {
        StringBuilder sb = new StringBuilder();
        sb.append(visitor.visitOnEntry(this));
        for (State<S, E, C> state : stateMap.values()) {
            sb.append(state.accept(visitor));
        }
        sb.append(visitor.visitOnExit(this));
        return sb.toString();
    }


    @Override
    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public void setFailCallback(FailCallback<S, E, C> failCallback) {
        this.failCallback = failCallback;
    }
}
