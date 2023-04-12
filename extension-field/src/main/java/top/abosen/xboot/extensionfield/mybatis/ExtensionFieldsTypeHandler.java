package top.abosen.xboot.extensionfield.mybatis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.abosen.xboot.extensionfield.extension.ExtensionField;
import top.abosen.xboot.extensionfield.extension.ExtensionFields;

import java.io.IOException;
import java.util.List;

/**
 * @author qiubaisen
 * @since 2023/4/12
 */
@MappedTypes({Object.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class ExtensionFieldsTypeHandler extends AbstractJsonTypeHandler<ExtensionFields> {
    private static ObjectMapper OBJECT_MAPPER;

    public static final TypeReference<List<ExtensionField>> TYPE_REF = new TypeReference<List<ExtensionField>>() {
    };

    private static final Logger log = LoggerFactory.getLogger(ExtensionFieldsTypeHandler.class);
    private final Class<?> type;

    public ExtensionFieldsTypeHandler(Class<?> type) {
        if (log.isTraceEnabled()) {
            log.trace("ExtensionFieldsTypeHandler(" + type + ")");
        }

        this.type = type;
    }

    protected String toJson(ExtensionFields obj) {
        try {
            return getObjectMapper().writerFor(TYPE_REF).writeValueAsString(obj.getFields());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    protected ExtensionFields parse(String json) {
        try {
            return new ExtensionFields(getObjectMapper().readValue(json, TYPE_REF));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ObjectMapper getObjectMapper() {
        if (null == OBJECT_MAPPER) {
            OBJECT_MAPPER = new ObjectMapper();
        }

        return OBJECT_MAPPER;
    }

    public static void setObjectMapper(ObjectMapper objectMapper) {
        OBJECT_MAPPER = objectMapper;
    }
}
