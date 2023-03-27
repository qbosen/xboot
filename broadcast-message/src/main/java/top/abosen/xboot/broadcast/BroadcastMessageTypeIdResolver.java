package top.abosen.xboot.broadcast;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qiubaisen
 * @since 2023/3/20
 */

@Slf4j
public class BroadcastMessageTypeIdResolver extends TypeIdResolverBase {

    JavaType baseType;
    private final Map<String, Class<?>> idToClass;
    private final Map<Class<?>, String> classToId;

    public BroadcastMessageTypeIdResolver() {
        this(new BroadcastMappingResourceProvider());
    }

    public BroadcastMessageTypeIdResolver(BroadcastMappingProvider broadcastMappingProvider) {

        this.idToClass = new HashMap<>();
        this.classToId = new HashMap<>();

        for (Map.Entry<Object, Object> entry : broadcastMappingProvider.getBroadcastMapping().entrySet()) {
            String messageId = String.valueOf(entry.getKey());
            String messageTypeValue = String.valueOf(entry.getValue());

            Class<?> messageType;
            try {
                messageType = Class.forName(messageTypeValue);
            } catch (ClassNotFoundException e) {
                log.warn("不存在的消息类型{}={}", messageId, messageTypeValue);
                continue;
            }
            if (!(InstanceMessage.class.isAssignableFrom(messageType))) {
                log.warn("类型{}不是一个有效的 InstanceMessage 类型", messageTypeValue);
                continue;
            }
            idToClass.putIfAbsent(messageId, messageType);
            classToId.putIfAbsent(messageType, messageId);
        }
    }


    @Override
    public void init(JavaType bt) {
        this.baseType = bt;
    }

    @Override
    public String idFromValue(Object value) {
        return classToId.get(value.getClass());
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {
        return classToId.get(suggestedType);
    }

    @Override
    public JavaType typeFromId(DatabindContext context, String id) throws IOException {
        Class<?> clazz = idToClass.get(id);
        if (clazz != null) {
            return context.constructSpecializedType(baseType, clazz);
        }
        return null;
    }

    @Override
    public String getDescForKnownTypeIds() {
        return String.join(",", idToClass.keySet());
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }
}
