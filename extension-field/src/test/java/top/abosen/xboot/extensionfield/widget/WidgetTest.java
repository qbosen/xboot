package top.abosen.xboot.extensionfield.widget;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.BasicJsonTester;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author qiubaisen
 * @date 2023/2/27
 */
class WidgetTest {
    ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    BasicJsonTester jsonTester = new BasicJsonTester(getClass());

    @Nested
    class InputWidgetTest {
        InputWidget widget;

        @BeforeEach
        void setup() {
            widget = new InputWidget();
            widget.setMultiple(true);
        }

        @Test
        void should_write_json() throws JsonProcessingException {
            String json = objectMapper.writeValueAsString(widget);
            System.out.println(json);
            assertThat(jsonTester.from(json)).isEqualToJson("{\"@type\":\"input\",\"multiple\":true}");
        }

        @Test
        void should_parse_json() throws JsonProcessingException {
            Widget unmarshal = objectMapper.readValue("{\"@type\":\"input\",\"multiple\":true}", Widget.class);
            assertThat(unmarshal).hasSameClassAs(widget).isEqualTo(widget);
        }

        @Test
        void should_accept_any_value(){
            assertThat(widget.checkValue(null)).isTrue();
        }
    }

    @Nested
    class SelectWidgetTest {
        SelectWidget widget;

        @BeforeEach
        void setup() {
            widget = new SelectWidget();
            widget.setMultiple(true);
            widget.setBizString("foo");
        }

        @Test
        void should_write_json() throws JsonProcessingException {
            String json = objectMapper.writeValueAsString(widget);
            System.out.println(json);
            assertThat(jsonTester.from(json))
                    .isEqualToJson("{\"@type\":\"select\",\"multiple\":true}")
                    .isEqualToJson("{\"biz\":{\"key\":\"foo\"}}");

        }

        @Test
        void should_parse_json() throws JsonProcessingException {
            Widget unmarshal = objectMapper.readValue("{\"@type\":\"select\",\"multiple\":true,\"biz\":{\"key\":\"foo\"}}", Widget.class);
            assertThat(unmarshal).hasSameClassAs(widget).isEqualTo(widget);
        }

        @Test
        void should_accept_any_value(){
            assertThat(widget.checkValue(null)).isTrue();
        }
    }
}