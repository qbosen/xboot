package top.abosen.xboot.extensionfield.widget;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.BasicJsonTester;
import top.abosen.xboot.extensionfield.valueholder.RefValueHolder;
import top.abosen.xboot.extensionfield.valueholder.ValueHolder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author qiubaisen
 * @since 2023/2/27
 */
class BizWidgetExtensionTest {
    ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    BasicJsonTester jsonTester = new BasicJsonTester(getClass());


    @Data
    public static class Wrapper {
        BizWidgetExtension biz;
    }

    @Test
    void should_parse_as_single_string() throws JsonProcessingException {
        Wrapper wrapper = objectMapper.readValue("{\"biz\":{\"key\": \"AnyString\"}}", Wrapper.class);
        assertThat(wrapper.getBiz()).isInstanceOf(DefaultBizWidgetExtension.class)
                .matches(it -> it.getKey().equals("AnyString"));
    }

    @Test
    void should_write_as_single_string() throws JsonProcessingException {
        Wrapper wrapper = new Wrapper();
        wrapper.setBiz(new DefaultBizWidgetExtension("video"));
        String json = objectMapper.writeValueAsString(wrapper);
        System.out.println(json);
        assertThat(jsonTester.from(json)).isEqualToJson("{\"biz\":{\"key\":\"video\"}}");
    }

    @Test
    void should_write_as_null_if_no_value() throws JsonProcessingException {
        Wrapper wrapper = new Wrapper();
        String json = objectMapper.writeValueAsString(wrapper);
        assertThat(jsonTester.from(json)).isEqualToJson("{\"biz\":null}");
    }

    @Test
    void should_parse_custom_extension() throws JsonProcessingException {
        Wrapper wrapper = new Wrapper();
        wrapper.setBiz(new PictureBizExtension());
        String json = objectMapper.writeValueAsString(wrapper);
        System.out.println(json);

        assertThat(jsonTester.from(json)).isEqualToJson("{\"biz\":{\"key\":\"picture\"}}");

        Wrapper unmarshal = objectMapper.readValue(json, Wrapper.class);
        assertThat(unmarshal.getBiz()).isInstanceOf(PictureBizExtension.class);

        ValueHolder ref = RefValueHolder.of(123L);
        boolean checkValue = unmarshal.getBiz().checkValue(ref);

        assertThat(checkValue).isTrue();
        assertThat(ref.get()).isEqualTo(1000123L);
    }
}