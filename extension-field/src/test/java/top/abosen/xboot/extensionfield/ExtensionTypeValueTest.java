package top.abosen.xboot.extensionfield;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.BasicJsonTester;
import top.abosen.xboot.extensionfield.extension.ExtensionTypeValue;

import static org.assertj.core.api.Assertions.*;

/**
 * @author qiubaisen
 * @date 2023/2/23
 */
class ExtensionTypeValueTest {
    @Test
    void should_parse_any_json() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        BasicJsonTester tester = new BasicJsonTester(getClass());

        String json = "{\n" +
                "  \"list\":" +
                "  [\n" +
                "     {\n" +
                "      \"foo\": \"bar\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"obj\": {\n" +
                "    \"double\": 3.14,\n" +
                "    \"array\": [1,2,3]\n" +
                "  },\n" +
                "  \"@meta\": \"meta\"\n" +
                "}";
        ExtensionTypeValue extensionTypeValue = mapper.readValue(json, ExtensionTypeValue.class);

        assertThat(extensionTypeValue.toMap()).containsKeys("list", "obj", "@meta");
        assertThat(tester.from(mapper.writeValueAsString(extensionTypeValue))).isEqualToJson(json);
    }
}