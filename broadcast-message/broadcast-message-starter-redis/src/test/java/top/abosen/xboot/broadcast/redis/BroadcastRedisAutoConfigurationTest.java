package top.abosen.xboot.broadcast.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.boot.env.RandomValuePropertySource;
import org.springframework.boot.test.context.assertj.AssertableApplicationContext;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.util.TestPropertyValues.Pair;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import top.abosen.xboot.broadcast.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;
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
    final ApplicationContextRunner contextRunner = new ApplicationContextRunner(() -> {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        RandomValuePropertySource.addToEnvironment(context.getEnvironment());
        return context;
    }).withUserConfiguration(BroadcastRedisAutoConfiguration.class)
            .withBean(StringRedisTemplate.class, () -> redisTemplate)
            .withBean(ObjectMapper.class, () -> objectMapper);

    @Test
    public void should_auto_config_if_topic_specified() {
        contextRunner.withPropertyValues(BroadcastRedisProperties.TOPIC + "=myTopic")
                .run(context -> {
                    assertThat(context)
                            .hasNotFailed()
                            .doesNotHaveBean(BroadcastMessageListener.class)
                            .hasSingleBean(BroadcastRedisProperties.class)
                            .hasSingleBean(BroadcastMessageForwarder.class)
                            .hasSingleBean(BroadcastMessagePublisher.class)
                            .hasSingleBean(BroadcastMessageMiddlewareListener.class)
                            .hasSingleBean(BroadcastMessageMiddlewarePublisher.class);

                    BroadcastRedisProperties properties = context.getBean(BroadcastRedisProperties.class);
                    assertThat(properties.getTopic()).isEqualTo("myTopic");
                    assertThat(properties.getInstanceId()).isNotBlank().matches(uuid());
                });
    }

    @Test
    public void should_ignore_if_no_topic_specified() {
        contextRunner.run(context -> {
            assertThat(context).hasNotFailed()
                    .doesNotHaveBean(BroadcastRedisAutoConfiguration.class);
        });
    }


    @TestFactory
    Stream<DynamicNode> should_set_instance_id() {
        return Stream.of(
                dynamicContainer("use Properties InstanceId if configured",
                        Stream.of(dynamicTest("only property", () ->
                                        should_set_instance_id_match(contextRunner, "bar", null, eq("bar"))),
                                dynamicTest("empty property", () ->
                                        should_set_instance_id_match(contextRunner, "", null, eq(""))),
                                dynamicTest("both property and consul", () ->
                                        should_set_instance_id_match(contextRunner, "bar", "cool", eq("bar")))
                        )),
                dynamicTest("use Consul InstanceId if property not configured", () ->
                        should_set_instance_id_match(contextRunner, null, "bar", eq("bar"))),
                dynamicTest("use Consul InstanceId if property not configured even empty", () ->
                        should_set_instance_id_match(contextRunner, null, "", eq(""))),
                dynamicTest("use UUID if consul/property instanceId none configured", () ->
                        should_set_instance_id_match(contextRunner, null, null, uuid()))
        );
    }

    private static void should_set_instance_id_match(
            ApplicationContextRunner contextRunner, String propertyInstanceId, String consulInstanceId, Predicate<String> expectInstanceId) {
        Map<String, String> map = new HashMap<String, String>() {{
            put("xboot.broadcast.redis.topic", "foo");
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