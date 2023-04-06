package top.abosen.xboot.demo;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import top.abosen.xboot.extensionfield.extension.ExtensionField;

import java.io.IOException;
import java.util.List;

/**
 * JsonTypeInfo is ignored when serializing a list of annotated object · Issue #336 · FasterXML/jackson-databind](https://github.com/FasterXML/jackson-databind/issues/336)
 *
 * @author qiubaisen
 * @since 2023/3/1
 */
public class ExtensionFieldTypeHandler extends JacksonTypeHandler {
    private static ObjectMapper OBJECT_MAPPER;

    public static ObjectMapper getObjectMapper() {
        if (null == OBJECT_MAPPER) {
            OBJECT_MAPPER = new ObjectMapper();
        }

        return OBJECT_MAPPER;
    }

    public static void setObjectMapper(ObjectMapper objectMapper) {
        Assert.notNull(objectMapper, "ObjectMapper should not be null");
        OBJECT_MAPPER = objectMapper;
    }

    public static final TypeReference<List<ExtensionField>> TYPE_REF = new TypeReference<>() {
    };

    public ExtensionFieldTypeHandler(Class<?> type) {
        super(type);
    }

    @Override
    protected String toJson(Object obj) {
        try {
            return getObjectMapper().writerFor(TYPE_REF).writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Object parse(String json) {
        try {
            return getObjectMapper().readValue(json, TYPE_REF);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}