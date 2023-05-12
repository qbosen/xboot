package top.abosen.xboot.statemachine.impl;

/**
 * Debugger, This is used to decouple Logging framework dependency
 *
 * @author qiubaisen
 * @since 2023/5/12
 */
public class Debugger {

    private static boolean isDebugOn = false;

    public static void debug(String message) {
        if (isDebugOn) {
            System.out.println(message);
        }
    }

    public static void enableDebug() {
        isDebugOn = true;
    }
}
