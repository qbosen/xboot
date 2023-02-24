package top.abosen.xboot.extensionfield.schema;

import cn.hutool.core.collection.ListUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author qiubaisen
 * @date 2023/2/23
 */
class SchemaJsonTest {

    static ObjectMapper objectMapper;

    @BeforeAll
    static void setup(){
        objectMapper = new ObjectMapper().findAndRegisterModules();
    }

    static Stream<Schema> schemaProvider(){
        List<Schema> schemas = new ArrayList<>();

        IntegerSchema integerSchema = new IntegerSchema();
        integerSchema.setMin(null);
        integerSchema.setMax(100);
        integerSchema.setRequired(true);
        integerSchema.setDefaultValue(8);
        schemas.add(integerSchema);

        IntegerListSchema integerListSchema = new IntegerListSchema();
        integerListSchema.setRequired(false);
        integerListSchema.setMin(null);
        integerListSchema.setMax(100);
        schemas.add(integerListSchema);


        StringSchema stringSchema = new StringSchema();
        stringSchema.setMinLength(3);
        stringSchema.setMaxLength(null);
        stringSchema.setRequired(true);
        stringSchema.setRegex("\\w+");
        stringSchema.setDefaultValue("default string");
        schemas.add(stringSchema);

        StringListSchema stringListSchema = new StringListSchema();
        stringListSchema.setMinLength(3);
        stringListSchema.setMaxLength(null);
        stringListSchema.setRequired(false);
        stringListSchema.setRegex("\\w+");
        schemas.add(stringListSchema);

        return schemas.stream();
    }


    @ParameterizedTest
    @MethodSource("schemaProvider")
    void should_handle_json_schema(Schema schema) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(schema);
        System.out.println(json);
        Schema unmarshal = objectMapper.readValue(json, Schema.class);
        Assertions.assertThat(unmarshal).hasSameClassAs(schema).isEqualTo(schema);
    }
}