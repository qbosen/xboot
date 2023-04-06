package top.abosen.xboot.configutil;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.stream.Collectors;

/**
 * @author qiubaisen
 * @since 2022/9/7
 */
public class ParallelSpeedUpTest {
    @SneakyThrows
    private String getKV(String key) {
        Thread.sleep(500);
        return "value";
    }

    @Test
    @Disabled
    public void parallel_action_should_faster() {
        long t = System.currentTimeMillis();
        Collections.nCopies(10, "key").stream().map(this::getKV).collect(Collectors.toList());
        long sequenceCost = System.currentTimeMillis() - t;

        t = System.currentTimeMillis();
        Collections.nCopies(10, "key").stream().parallel().map(this::getKV).collect(Collectors.toList());
        long parallelCost = System.currentTimeMillis() - t;

        System.out.println("s:" + sequenceCost);
        System.out.println("p:" + parallelCost);
    }

    @Test
    @Disabled
    public void parallel_action_should_work_in_collect() {
        long t = System.currentTimeMillis();
        Collections.nCopies(10, "key").stream().collect(Collectors.toMap(it -> it, this::getKV, (a, b) -> b));
        long sequenceCost = System.currentTimeMillis() - t;

        t = System.currentTimeMillis();
        Collections.nCopies(10, "key").stream().parallel().collect(Collectors.toMap(it -> it, this::getKV, (a, b) -> b));
        long parallelCost = System.currentTimeMillis() - t;

        System.out.println("s:" + sequenceCost);
        System.out.println("p:" + parallelCost);
    }
}
