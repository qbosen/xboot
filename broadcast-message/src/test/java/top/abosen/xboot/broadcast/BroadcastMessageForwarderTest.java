package top.abosen.xboot.broadcast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import top.abosen.xboot.broadcast.test.MockitoBase;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

/**
 * @author qiubaisen
 * @since 2023/3/30
 */
@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
class BroadcastMessageForwarderTest extends MockitoBase {
    @Spy
    BroadcastInstanceContext context = new BroadcastInstanceContext("foo");
    @Mock
    BroadcastMessageListener<InstanceMessage> listener;
    @Mock
    ObjectMapper objectMapper;
    @Mock
    InstanceMessage instanceMessage;

    BroadcastMessageForwarder messageForwarder;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        when(instanceMessage.getInstanceId()).thenReturn("foo");
        when(context.getInstanceId()).thenReturn("other host");

        when(objectMapper.readValue(anyString(), same(InstanceMessage.class))).thenReturn(instanceMessage);
        doReturn(instanceMessage.getClass()).when(listener).type();

        messageForwarder = new BroadcastMessageForwarderImpl(context, objectMapper, Arrays.asList(listener));
    }

    @Test
    void should_forward_message() {
        messageForwarder.forward("bar");

        verify(listener, times(1)).onMessage(same(instanceMessage));
    }

    @Test
    void should_skip_forward_if_instance_same() {
        when(context.getInstanceId()).thenReturn("foo");
        messageForwarder.forward("bar");

        verify(listener, never()).onMessage(same(instanceMessage));
    }

    @Test
    void should_skip_forward_if_type_not_match() {
        doReturn(Object.class).when(listener).type();

        messageForwarder.forward("bar");

        verify(listener, never()).onMessage(same(instanceMessage));
    }

    @Test
    void should_mark_broadcasting_when_listen_message() {
        assertThat(context.isBroadcasting()).as("before broadcasting").isFalse();
        messageForwarder.forward("bar");
        assertThat(context.isBroadcasting()).as("after broadcasting").isFalse();

        doAnswer(invocation -> {
            assertThat(context.isBroadcasting()).as("during broadcasting").isTrue();
            return null;
        }).when(listener).onMessage(same(instanceMessage));

        verify(listener, times(1)).onMessage(same(instanceMessage));
    }

}