package top.abosen.xboot.broadcast.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.boot.env.RandomValuePropertySource;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import top.abosen.xboot.broadcast.BroadcastMessageForwarder;
import top.abosen.xboot.broadcast.BroadcastMessageMiddlewareListener;
import top.abosen.xboot.broadcast.BroadcastMessageMiddlewarePublisher;
import top.abosen.xboot.broadcast.BroadcastMessagePublisher;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.Mockito.mock;

/**
 * @author qiubaisen
 * @since 2023/3/29
 */
class BroadcastRedisAutoConfigurationTest {

    final StringRedisTemplate redisTemplate = mock(StringRedisTemplate.class);
    final ObjectMapper objectMapper = new ObjectMapper();
    /**
     * RandomValuePropertySource 是在 {@link org.springframework.boot.env.EnvironmentPostProcessor}
     */
    ApplicationContextRunner contextRunner;

    @Nested
    class ConfigurationTest {

        ApplicationContextRunner setupContextRunner(boolean topic, boolean redis, boolean mapper) {
            contextRunner = new ApplicationContextRunner().withUserConfiguration(BroadcastRedisAutoConfiguration.class);
            if (topic)
                contextRunner = contextRunner.withPropertyValues("xboot.broadcast.redis.topic=foo");
            if (redis)
                contextRunner = contextRunner.withBean(StringRedisTemplate.class, () -> redisTemplate);
            if (mapper)
                contextRunner = contextRunner.withBean(ObjectMapper.class, () -> objectMapper);
            return contextRunner;
        }


        @TestFactory
        Stream<DynamicTest> test_auto_configuration() {
            List<DynamicTest> tests = new ArrayList<>();

            for (int i = 0; i < 1 << 3; i++) {
                boolean topic = (i & 0b001) != 0;
                boolean redis = (i & 0b010) != 0;
                boolean mapper = (i & 0b100) != 0;

                String presentItems = Stream.of(topic ? "topic property" : null, redis ? "RedisTemplate bean" : null, mapper ? "ObjectMapper bean" : null)
                        .filter(Objects::nonNull).collect(Collectors.joining(","));

                if (topic && redis && mapper) {
                    tests.add(DynamicTest.dynamicTest("should configure all beans if all presents", () ->
                            setupContextRunner(topic, redis, mapper).run(context -> assertThat(context)
                                    .hasNotFailed()
                                    .hasSingleBean(BroadcastRedisAutoConfiguration.class)
                                    .hasSingleBean(BroadcastRedisProperties.class)
                                    .hasSingleBean(BroadcastMessageForwarder.class)
                                    .hasSingleBean(BroadcastMessagePublisher.class)
                                    .hasSingleBean(BroadcastMessageMiddlewareListener.class)
                                    .hasSingleBean(BroadcastMessageMiddlewarePublisher.class))
                    ));
                } else {
                    tests.add(DynamicTest.dynamicTest("should ship auto configuration if present items: " + presentItems, () ->
                            setupContextRunner(topic, redis, mapper).run(context -> assertThat(context)
                                    .hasNotFailed()
                                    .doesNotHaveBean(BroadcastRedisAutoConfiguration.class))));
                }

            }
            return tests.stream();
        }

    }

    @Nested
    class InstanceIdTest {
        @BeforeEach
        void setup() {
            contextRunner = new ApplicationContextRunner(() -> {
                AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
                RandomValuePropertySource.addToEnvironment(context.getEnvironment());
                return context;
            }).withUserConfiguration(BroadcastRedisAutoConfiguration.class)
                    .withBean(StringRedisTemplate.class, () -> redisTemplate)
                    .withBean(ObjectMapper.class, () -> objectMapper)
                    .withPropertyValues("xboot.broadcast.redis.topic=foo")
            ;
        }

        @TestFactory
        Stream<DynamicNode> should_set_instance_id() {
            return Stream.of(
                    dynamicContainer("use Properties InstanceId if configured",
                            Stream.of(dynamicTest("only property", () ->
                                            should_set_instance_id_match("bar", null, eq("bar"))),
                                    dynamicTest("empty property", () ->
                                            should_set_instance_id_match("", null, eq(""))),
                                    dynamicTest("both property and consul", () ->
                                            should_set_instance_id_match("bar", "cool", eq("bar")))
                            )),
                    dynamicTest("use Consul InstanceId if property not configured", () ->
                            should_set_instance_id_match(null, "bar", eq("bar"))),
                    dynamicTest("use Consul InstanceId if property not configured even empty", () ->
                            should_set_instance_id_match(null, "", eq(""))),
                    dynamicTest("use UUID if consul/property instanceId none configured", () ->
                            should_set_instance_id_match(null, null, uuid()))
            );
        }

        private void should_set_instance_id_match(
                String propertyInstanceId, String consulInstanceId, Predicate<String> expectInstanceId) {
            Map<String, String> map = new HashMap<String, String>() {{
                put("xboot.broadcast.redis.instance-id", propertyInstanceId);
                put("spring.cloud.consul.discovery.instance-id", consulInstanceId);
            }};
            String[] pairs = map.entrySet().stream().filter(it -> it.getValue() != null)
                    .map(it -> it.getKey() + "=" + it.getValue())
                    .toArray(String[]::new);

            contextRunner.withPropertyValues(pairs)
                    .run(context -> {
                        assertThat(context)
                                .hasNotFailed()
                                .hasSingleBean(Environment.class)
                                .hasSingleBean(BroadcastRedisProperties.class);

                        BroadcastRedisProperties properties = context.getBean(BroadcastRedisProperties.class);
                        assertThat(properties.getTopic()).isEqualTo("foo");
                        assertThat(properties.getInstanceId()).matches(expectInstanceId);
                    });
        }
    }


    private static Predicate<String> eq(String expect) {
        return it -> Objects.equals(it, expect);
    }

    private static Predicate<String> uuid() {
        return it -> {
            assertUuid(it);
            return true;
        };
    }

    private static void assertUuid(String instanceId) {
        assertThat(instanceId).isNotBlank();
        assertThatNoException().isThrownBy(() -> {
            UUID uuid = UUID.fromString(instanceId);
            assertThat(uuid).isNotNull();
        });
    }

}