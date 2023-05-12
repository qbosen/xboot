package top.abosen.xboot.statemachine;

import top.abosen.xboot.statemachine.impl.StateMachineException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * StateMachineFactory
 *
 * @author qiubaisen
 * @since 2023/5/12
 */
public class StateMachineFactory {
    static Map<String /* machineId */, StateMachine<?, ?, ?>> stateMachineMap = new ConcurrentHashMap<>();

    public static <S, E, C> void register(StateMachine<S, E, C> stateMachine) {
        String machineId = stateMachine.getMachineId();
        if (stateMachineMap.get(machineId) != null) {
            throw new StateMachineException("The state machine with id [" + machineId + "] is already built, no need to build again");
        }
        stateMachineMap.put(stateMachine.getMachineId(), stateMachine);
    }

    @SuppressWarnings("unchecked")
    public static <S, E, C> StateMachine<S, E, C> get(String machineId) {
        StateMachine<S, E, C> stateMachine = (StateMachine<S, E, C>) stateMachineMap.get(machineId);
        if (stateMachine == null) {
            throw new StateMachineException("There is no stateMachine instance for " + machineId + ", please build it first");
        }
        return stateMachine;
    }
}
