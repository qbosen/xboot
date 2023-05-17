package top.abosen.xboot.statemachine.impl;

import top.abosen.xboot.statemachine.State;
import top.abosen.xboot.statemachine.StateMachine;
import top.abosen.xboot.statemachine.Transition;
import top.abosen.xboot.statemachine.Visitor;

/**
 * MermaidUMLVisitor
 *
 * @author qiubaisen
 * @since 2023/5/12
 */
public class MermaidUMLVisitor implements Visitor {

    /**
     * Since the state machine is stateless, there is no initial state.
     * <p>
     * You have to add "{@code [*] -> initialState}" to mark it as a state machine diagram.
     * otherwise it will be recognized as a sequence diagram.
     *
     * @param visitable the element to be visited.
     * @return
     */
    @Override
    public String visitOnEntry(StateMachine<?, ?, ?> visitable) {
        return "stateDiagram-v2" + LF;
    }

    @Override
    public String visitOnExit(StateMachine<?, ?, ?> visitable) {
        return "";
    }

    @Override
    public String visitOnEntry(State<?, ?, ?> state) {
        StringBuilder sb = new StringBuilder();
        for (Transition<?, ?, ?> transition : state.getAllTransitions()) {
            sb.append(transition.getSource().getId())
                    .append(" --> ")
                    .append(transition.getTarget().getId())
                    .append(" : ")
                    .append(transition.getEvent())
                    .append(LF);
        }
        return sb.toString();
    }

    @Override
    public String visitOnExit(State<?, ?, ?> state) {
        return "";
    }
}
