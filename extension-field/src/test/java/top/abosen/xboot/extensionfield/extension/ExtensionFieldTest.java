package top.abosen.xboot.extensionfield.extension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

/**
 * @author qiubaisen
 * @since 2023/2/27
 */
class ExtensionFieldTest {
    ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void should_map_to_simple_field_by_default() throws JsonProcessingException {
        ExtensionField extensionField = objectMapper.readValue("{\n" +
                "  \"key\":" +
                "  \"some\",\n" +
                "  \"foo\": \"bar\"\n" +
                "}", ExtensionField.class);
        assertThat(extensionField).isInstanceOf(SimpleExtensionField.class)
                .hasFieldOrPropertyWithValue("key", "some")
                .extracting(ExtensionField::extension).hasFieldOrPropertyWithValue("foo", "bar");
    }

}