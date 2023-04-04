package top.abosen.xboot.broadcast;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.Callable;

/**
 * @author qiubaisen
 * @since 2023/3/13
 */

@RequiredArgsConstructor
public class BroadcastInstanceContext {
    @Getter
    final String instanceId;

    private static final ThreadLocal<Boolean> BROADCASTING = new ThreadLocal<>();

    protected void beginBroadcast() {
        BROADCASTING.set(true);
    }

    protected void finishBroadcast() {
        BROADCASTING.set(false);
    }

    public boolean isBroadcasting() {
        Boolean during = BROADCASTING.get();
        return during != null && during;
    }

    public <T> T broadcast(Callable<T> callable) {
        try {
            beginBroadcast();
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            finishBroadcast();
        }
    }

    public void broadcast(Runnable runnable) {
        try {
            beginBroadcast();
            runnable.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            finishBroadcast();
        }
    }
}
