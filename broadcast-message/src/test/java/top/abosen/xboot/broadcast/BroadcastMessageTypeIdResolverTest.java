package top.abosen.xboot.broadcast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.BasicJsonTester;

import static org.assertj.core.api.Assertions.assertThat;
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


    // todo config: illegal class value
    // todo config: multiple name and serial to first name
    // todo mock config inject from jars
    // todo value message
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
        //language=JSON
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
    public static class MyMessage extends InstanceMessage {
        long userId;
    }
}