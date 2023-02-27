package top.abosen.xboot.extensionfield.extension;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

/**
 * @author qiubaisen
 * @date 2023/2/27
 */
class ExtensionFieldTest {
    ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void should_map_to_simple_field_by_default() {

    }

}