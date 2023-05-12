package top.abosen.xboot.statemachine.exception;

/**
 * @author 龙也
 * @since 2023/5/12
 */
public class TransitionFailException extends RuntimeException {

    public TransitionFailException(String errMsg) {
        super(errMsg);
    }
}
