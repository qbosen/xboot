package top.abosen.xboot.broadcast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

/**
 * @author qiubaisen
 * @since 2023/3/20
 */
class BroadcastMessageTypeIdResolverTest {

    @Test
    void should_resolve() throws JsonProcessingException {
        //language=JSON
        String content = "{\n" +
                         "  \"@type\":" +"  \"my\",\n" +
                         "  \"instanceId\": \"abcd\",\n" +
                         "  \"userId\": 999\n" +
                         "}";

        ObjectMapper mapper = new ObjectMapper();
        InstanceMessage message = mapper.readValue(content, InstanceMessage.class);
        assertThat(message).isInstanceOf(MyMessage.class)
                .hasFieldOrPropertyWithValue("userId", 999L);
        System.out.println(message);
    }

    @Test
    void should_parse() throws JsonProcessingException {
        MyMessage message = new MyMessage();
        message.setUserId(123);
        message.setInstanceId("instanceId");

        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(message);
        System.out.println(content);
        assertThat(content).contains("my");
    }


    @Getter @Setter
    @BroadcastMessage("my")
    public static class MyMessage extends InstanceMessage{
        long userId;

        @Override
        public String toString() {
            return "{" +
                   "userId=" + userId +
                   ", instanceId='" + instanceId + '\'' +
                   '}';
        }
    }
}