package top.abosen.xboot.broadcast;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.json.BasicJsonTester;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

/**
 * @author qiubaisen
 * @since 2023/3/20
 */
class BroadcastMessageTypeIdResolverTest {

    ObjectMapper objectMapper;
    BasicJsonTester jsonTester;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        jsonTester = new BasicJsonTester(getClass());
    }

    @Test
    void should_deserialize_message_by_constructor() throws JsonProcessingException {
        String content = "{\n" +
                         "  \"@type\":" + "  \"value\",\n" +
                         "  \"instanceId\": \"abcd\",\n" +
                         "  \"id\": 999,\n" +
                         "  \"name\": \"valueName\"\n" +
                         "}";


        InstanceMessage message = objectMapper.readValue(content, InstanceMessage.class);
        assertThat(message)
                .asInstanceOf(type(ValueMessage.class))
                .extracting(ValueMessage::getId, ValueMessage::getName, ValueMessage::getInstanceId)
                .containsExactly(999L, "valueName", "abcd");
        System.out.println(message);
    }

    @Value
    @BroadcastMessage("value")
    public static class ValueMessage extends InstanceMessage {
        long id;
        String name;

    }

    @Test
    void should_serialize_message_with_type_info() throws JsonProcessingException {
        MyMessage message = new MyMessage();
        message.setUserId(123);
        message.setInstanceId("instanceId");

        String content = objectMapper.writeValueAsString(message);
        System.out.println(content);
        assertThat(jsonTester.from(content))
                .hasJsonPathStringValue("$.@type", "my")
                .hasJsonPathStringValue("$.instanceId", "instanceId")
                .hasJsonPathNumberValue("$.userId", 123);
    }

    @Test
    void should_deserialize_message_by_setter() throws JsonProcessingException {
        String content = "{\n" +
                         "  \"@type\": \"my\",\n" +
                         "  \"instanceId\": \"abcd\",\n" +
                         "  \"userId\": 999\n" +
                         "}";

        InstanceMessage message = objectMapper.readValue(content, InstanceMessage.class);
        assertThat(message).asInstanceOf(type(MyMessage.class))
                .extracting(MyMessage::getUserId, MyMessage::getInstanceId)
                .containsExactly(999L, "abcd");
        System.out.println(message);
    }


    @Getter
    @Setter
    @BroadcastMessage("my")
    @ToString(callSuper = true)
    public static class MyMessage extends InstanceMessage {
        long userId;
    }



    @Nested
    class CustomBroadcastResolver {

        private void configBroadcastMapping(Map<String, String> nameToClass) {
            BroadcastMappingResourceProvider.TEST_MAPPING_ONLY.putAll(nameToClass);
        }

        @Test
        void should_use_custom_broadcast_config() throws JsonProcessingException {
            configBroadcastMapping(ImmutableMap.of("custom", MyMessage.class.getName()));
            String content = "{ \"@type\": \"custom\", \"instanceId\": \"abcd\", \"userId\": 999}";

            assertThat(objectMapper.readValue(content, InstanceMessage.class))
                    .asInstanceOf(type(MyMessage.class))
                    .extracting(MyMessage::getUserId, MyMessage::getInstanceId)
                    .containsExactly(999L, "abcd");
        }


        @Test
        void should_serialize_with_type_as_first_name_if_message_configure_multi_name() throws JsonProcessingException {
            configBroadcastMapping(ImmutableMap.of("foo", MyMessage.class.getName(), "bar", MyMessage.class.getName()));

            String content = objectMapper.writeValueAsString(new MyMessage());
            assertThat(jsonTester.from(content))
                    .hasJsonPathStringValue("$.@type", "foo");

        }
        @Nested
        class CheckLog {
            Logger logger;
            ListAppender<ILoggingEvent> listAppender;


            @BeforeEach
            public void setUp() {
                logger = (Logger) LoggerFactory.getLogger(BroadcastMessageTypeIdResolver.class);
                listAppender = new ListAppender<>();
                listAppender.start();
                logger.addAppender(listAppender);
            }

            @AfterEach
            public void tearDown() {
                logger.detachAppender(listAppender);
                listAppender.stop();
            }

            @Test
            void should_throwException_and_logWarn_if_message_configure_with_illegal_ClassName() throws JsonProcessingException {
                configBroadcastMapping(ImmutableMap.of("not_exist", "com.class_not_found.IllegalMessage"));

                assertThatExceptionOfType(InvalidTypeIdException.class)
                        .isThrownBy(() -> objectMapper.readValue("{ \"@type\": \"not_exist\", \"instanceId\": \"1234\"}", InstanceMessage.class));

                assertThat(listAppender.list)
                        .filteredOn(it -> it.getLevel() == Level.WARN)
                        .filteredOn(it -> it.getMessage().contains("不存在的消息类型"))
                        .isNotEmpty();
            }


            @Test
            void should_throwException_and_logWarn_if_message_type_not_instanceOf_InstanceMessage() {
                configBroadcastMapping(ImmutableMap.of("illegal", "java.time.LocalDateTime"));

                assertThatExceptionOfType(InvalidTypeIdException.class)
                        .isThrownBy(() -> objectMapper.readValue("{ \"@type\": \"illegal\", \"instanceId\": \"1234\"}", InstanceMessage.class));

                assertThat(listAppender.list)
                        .filteredOn(it -> it.getLevel() == Level.WARN)
                        .filteredOn(it -> it.getMessage().contains("不是一个有效的 InstanceMessage 类型"))
                        .isNotEmpty();
            }

        }
    }

}