package top.abosen.xboot.statemachine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import top.abosen.xboot.statemachine.builder.StateMachineBuilder;
import top.abosen.xboot.statemachine.builder.StateMachineBuilderFactory;
import top.abosen.xboot.statemachine.impl.StateMachineException;

/**
 * StateMachineUnNormalTest
 *
 * @author Frank Zhang
 * @since 2023/5/12
 */
public class StateMachineUnNormalTest {

    @Test
    public void testConditionNotMeet() {
        StateMachineBuilder<StateMachineTest.States, StateMachineTest.Events, StateMachineTest.Context> builder = StateMachineBuilderFactory.create();
        builder.externalTransition()
                .from(StateMachineTest.States.STATE1)
                .to(StateMachineTest.States.STATE2)
                .on(StateMachineTest.Events.EVENT1)
                .when(checkConditionFalse())
                .perform(doAction());

        StateMachine<StateMachineTest.States, StateMachineTest.Events, StateMachineTest.Context> stateMachine = builder.build("NotMeetConditionMachine");
        StateMachineTest.States target = stateMachine.fireEvent(StateMachineTest.States.STATE1, StateMachineTest.Events.EVENT1, new StateMachineTest.Context());
        Assertions.assertEquals(StateMachineTest.States.STATE1, target);
    }


    @Test
    public void testDuplicatedTransition() {
        StateMachineBuilder<StateMachineTest.States, StateMachineTest.Events, StateMachineTest.Context> builder = StateMachineBuilderFactory.create();
        builder.externalTransition()
                .from(StateMachineTest.States.STATE1)
                .to(StateMachineTest.States.STATE2)
                .on(StateMachineTest.Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());

        Assertions.assertThrows(StateMachineException.class, () ->
                builder.externalTransition()
                        .from(StateMachineTest.States.STATE1)
                        .to(StateMachineTest.States.STATE2)
                        .on(StateMachineTest.Events.EVENT1)
                        .when(checkCondition())
                        .perform(doAction())
        );
    }

    @Test
    public void testDuplicateMachine() {
        StateMachineBuilder<StateMachineTest.States, StateMachineTest.Events, StateMachineTest.Context> builder = StateMachineBuilderFactory.create();
        builder.externalTransition()
                .from(StateMachineTest.States.STATE1)
                .to(StateMachineTest.States.STATE2)
                .on(StateMachineTest.Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());

        builder.build("DuplicatedMachine");
        Assertions.assertThrows(StateMachineException.class, () ->
                builder.build("DuplicatedMachine"));
    }

    private Condition<StateMachineTest.Context> checkCondition() {
        return (ctx) -> {
            return true;
        };
    }

    private Condition<StateMachineTest.Context> checkConditionFalse() {
        return (ctx) -> {
            return false;
        };
    }

    private Action<StateMachineTest.States, StateMachineTest.Events, StateMachineTest.Context> doAction() {
        return (from, to, event, ctx) -> {
            System.out.println(ctx.operator + " is operating " + ctx.entityId + "from:" + from + " to:" + to + " on:" + event);
        };
    }
}
