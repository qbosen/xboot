package top.abosen.xboot.configutil;

/**
 * @author qiubaisen
 * @date 2021/11/12
 */
public class ConsulConfigException extends RuntimeException {
    public ConsulConfigException() {
        super();
    }

    public ConsulConfigException(String message) {
        super(message);
    }

    public ConsulConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConsulConfigException(Throwable cause) {
        super(cause);
    }

    protected ConsulConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
