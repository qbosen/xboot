package top.abosen.xboot.broadcast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import top.abosen.xboot.broadcast.test.MockitoBase;

import static org.mockito.Mockito.*;

/**
 * @author qiubaisen
 * @since 2023/3/30
 */
public class BroadcastMessagePublisherTest extends MockitoBase {

    @Spy
    BroadcastInstanceContext context = new BroadcastInstanceContext("foo");
    @Mock
    BroadcastMessageMiddlewarePublisher realPublisher;
    @Mock
    ObjectMapper objectMapper;
    @Mock
    InstanceMessage instanceMessage;
    String rawMessageStr;

    BroadcastMessagePublisher publisher;

    @BeforeEach
    void setup() throws JsonProcessingException {
        rawMessageStr = "any raw message";
        publisher = new BroadcastMessagePublisherImpl(context, objectMapper, realPublisher);
        when(objectMapper.writeValueAsString(same(instanceMessage))).thenReturn(rawMessageStr);
    }

    @Test
    void should_publish_message_to_real_publisher() {
        publisher.publish(instanceMessage);

        verify(instanceMessage, atLeast(1).description("message should set instanceId from context")).setInstanceId(eq("foo"));
        verify(realPublisher, description("should receive raw message")).publish(eq(rawMessageStr));
    }

    @Test
    void should_skip_publish_message_when_context_isBroadcasting() {
        when(context.isBroadcasting()).thenReturn(true);

        publisher.publish(instanceMessage);

        verify(realPublisher, never()).publish(eq(rawMessageStr));
    }

    @Test
    void should_publish_message_when_context_isBroadcasting_and_forcePublish() {
        when(context.isBroadcasting()).thenReturn(true);

        publisher.forcePublish(instanceMessage);

        verify(instanceMessage, description("message should set instanceId from context")).setInstanceId(eq("foo"));
        verify(realPublisher, description("should receive raw message")).publish(eq(rawMessageStr));
    }
}
