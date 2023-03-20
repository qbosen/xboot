package top.abosen.xboot.broadcast;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author qiubaisen
 * @since 2023/3/20
 */

@Slf4j
public class BroadcastMessageTypeIdResolver extends TypeIdResolverBase {

    JavaType baseType;
    private final Map<String, Class<?>> idToClass;
    private final Map<Class<?>, String> classToId;
    private static final String BROADCAST_PROPERTIES = "META-INF/broadcast.properties";

    private static Properties broadcastMapping;


    public BroadcastMessageTypeIdResolver() {
        setupBroadcastMapping();

        this.idToClass = new HashMap<>();
        this.classToId = new HashMap<>();

        for (Map.Entry<Object, Object> entry : broadcastMapping.entrySet()) {
            String messageId = String.valueOf(entry.getKey());
            Class<?> messageType = null;
            try {
                messageType = Class.forName(String.valueOf(entry.getValue()));
            } catch (ClassNotFoundException e) {
                log.warn("不存在的消息类型", e);
            }
            if (messageType == null || !(InstanceMessage.class.isAssignableFrom(messageType))) {
                continue;
            }
            idToClass.putIfAbsent(messageId, messageType);
            classToId.putIfAbsent(messageType, messageId);
        }
    }

    private synchronized static void setupBroadcastMapping() {
        if (broadcastMapping == null) {
            try {
                broadcastMapping = loadBroadcastMapping();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static Properties loadBroadcastMapping() throws IOException {
        Properties props = new Properties();
        Enumeration<URL> broadcasts = Thread.currentThread().getContextClassLoader()
                .getResources(BROADCAST_PROPERTIES);
        while (broadcasts.hasMoreElements()) {
            URL broadcast = broadcasts.nextElement();
            try (InputStream stream = broadcast.openStream()) {
                props.load(stream);
            }
        }
        return props;
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
