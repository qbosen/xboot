package top.abosen.xboot.configutil;

import com.ecwid.consul.v1.health.model.HealthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author qiubaisen
 * @date 2022/9/6
 */
class ConsulConfigPropertyLocatorTest {

    static Function<String, Boolean> duplicate(boolean duplicate) {
        return x -> duplicate;
    }

    static BiConsumer<String, Object> valueAssert(String expectKey, Object expectValue) {
        return (key, value) -> {
            Assertions.assertEquals(expectKey, key);
            Assertions.assertEquals(expectValue, value);
        };
    }

    @Nested
    class KVTest {

        @Test
        public void throw_exception_if_key_not_exist_but_asked() {
            ConsulConfigRegistryProperties.KV kv = new ConsulConfigRegistryProperties.KV();
            kv.setMustExist(true);
            kv.setAlias("test");

            Assertions.assertThrows(ConsulConfigException.class, () ->
                    ConsulConfigPropertyLocator.kv("key", kv,
                            valueAssert("test", "test"),
                            duplicate(false), k -> Optional.empty()
                    )
            );
        }


        @Test
        public void null_if_key_not_exist() {
            ConsulConfigRegistryProperties.KV kv = new ConsulConfigRegistryProperties.KV();
            kv.setMustExist(false);
            kv.setAlias("test");

            ConsulConfigPropertyLocator.kv("key", kv,
                    valueAssert("test", null),
                    duplicate(false), k -> Optional.empty()
            );
        }

        @Test
        public void null_if_kv_absent() {
            ConsulConfigRegistryProperties.KV kv = new ConsulConfigRegistryProperties.KV();
            kv.setMustExist(false);
            kv.setAlias("test");
            kv.setBlankToNull(true);

            ConsulConfigPropertyLocator.kv("test", kv,
                    valueAssert("test", null),
                    duplicate(false), k -> Optional.empty()
            );
        }

        @Test
        public void set_null_to_value() {
            ConsulConfigRegistryProperties.KV kv = new ConsulConfigRegistryProperties.KV();
            kv.setMustExist(false);
            kv.setAlias("test");
            kv.setBlankToNull(true);
            kv.setNullToValue("null_str");

            ConsulConfigPropertyLocator.kv("test", kv,
                    valueAssert("test", "null_str"),
                    duplicate(false), k -> Optional.empty()
            );
        }

        @Test
        public void keep_value_if_kv_present() {
            ConsulConfigRegistryProperties.KV kv = new ConsulConfigRegistryProperties.KV();
            kv.setMustExist(false);
            kv.setAlias("test");
            kv.setBlankToNull(true);
            kv.setNullToValue("null_str");

            ConsulConfigPropertyLocator.kv("key", kv,
                    valueAssert("test", "present"),
                    duplicate(false), k -> Optional.of("present")
            );
        }

        @Test
        public void set_blank_to_null() {
            ConsulConfigRegistryProperties.KV kv = new ConsulConfigRegistryProperties.KV();
            kv.setMustExist(false);
            kv.setAlias("test");
            kv.setBlankToNull(true);
            ConsulConfigPropertyLocator.kv("key", kv,
                    valueAssert("test", null),
                    duplicate(false), k -> Optional.of("")
            );
        }

        @Test
        public void set_blank_to_null_and_null_to_str() {
            ConsulConfigRegistryProperties.KV kv = new ConsulConfigRegistryProperties.KV();
            kv.setMustExist(false);
            kv.setAlias("test");
            kv.setBlankToNull(true);
            kv.setNullToValue("null_str");
            ConsulConfigPropertyLocator.kv("key", kv,
                    valueAssert("test", "null_str"),
                    duplicate(false), k -> Optional.of("")
            );
        }
    }

    @Nested
    class ServiceTest {

        private List<HealthService.Service> mock(Object... hostAndPorts) {
            return IntStream.range(0, hostAndPorts.length / 2)
                    .mapToObj(i -> {
                        HealthService.Service service = new HealthService.Service();
                        service.setAddress((String) hostAndPorts[2 * i]);
                        service.setPort((Integer) hostAndPorts[2 * i + 1]);
                        return service;
                    }).collect(Collectors.toList());
        }

        @Test
        public void throw_exception_if_service_not_exist() {
            ConsulConfigRegistryProperties.Service svc = new ConsulConfigRegistryProperties.Service();
            svc.setAlias("test");

            Assertions.assertThrows(ConsulConfigException.class, () ->
                    ConsulConfigPropertyLocator.svc("key", svc,
                            valueAssert("test", null),
                            duplicate(false), k -> Optional.empty()
                    )
            );
        }


        @Test
        public void host_port() {
            ConsulConfigRegistryProperties.Service svc = new ConsulConfigRegistryProperties.Service();
            svc.setAlias("test");
            Map<String, Object> container = new HashMap<>();

            ConsulConfigPropertyLocator.svc("key", svc,
                    container::put,
                    duplicate(false), k -> Optional.of(mock("consul", 8500))
            );

            Assertions.assertEquals("consul", container.get("test.host"));
            Assertions.assertEquals(8500, container.get("test.port"));
        }

        @Test
        public void join_with_semicolon() {
            ConsulConfigRegistryProperties.Service svc = new ConsulConfigRegistryProperties.Service();
            svc.setAlias("test");
            svc.setAction(ConsulConfigRegistryProperties.Service.Action.JOIN_ALL);
            svc.setJoinWith(";");

            ConsulConfigPropertyLocator.svc("key", svc,
                    valueAssert("test", "mq1:9898;mq2:9988;mq3:9090"),
                    duplicate(false), k -> Optional.of(mock("mq1", 9898, "mq2", 9988, "mq3", 9090))
            );
        }

        @Test
        public void join_with_comma() {
            ConsulConfigRegistryProperties.Service svc = new ConsulConfigRegistryProperties.Service();
            svc.setAlias("test");
            svc.setAction(ConsulConfigRegistryProperties.Service.Action.JOIN_ALL);
            svc.setJoinWith(",");

            ConsulConfigPropertyLocator.svc("key", svc,
                    valueAssert("test", "es1:9898,es2:9988,es3:9090"),
                    duplicate(false), k -> Optional.of(mock("es1", 9898, "es2", 9988, "es3", 9090))
            );
        }

    }

}