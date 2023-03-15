package top.abosen.xboot.broadcast;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Properties;

public class JacksonTypeResolver extends StdTypeResolverBuilder {

    private final StdTypeResolverBuilder builder = new StdTypeResolverBuilder();

    private static final String BROADCAST_PROPERTIES = "/broadcast.properties";

    private final Properties broadcastMapping;

    public JacksonTypeResolver() throws IOException {
        this.broadcastMapping = loadBroadcastMapping();
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        builder.typeProperty("type");
    }

    private Properties loadBroadcastMapping() throws IOException {
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
    public TypeDeserializer buildTypeDeserializer(DeserializationConfig config, JavaType baseType,
                                                  Collection<NamedType> subtypes) {
        builder.typeIdVisibility(true);
        for (NamedType subtype : subtypes) {
            String name = subtype.getType().getName();
            if (broadcastMapping.containsKey(name)) {
                String value = broadcastMapping.getProperty(name);
//                builder.registerSubtypeWithTypeId(subtype.getType(), value);
            } else {
//                builder.registerSubtype(subtype.getType());
            }
        }
        return builder.buildTypeDeserializer(config, baseType, subtypes);
    }

    @Override
    public TypeSerializer buildTypeSerializer(SerializationConfig config, JavaType baseType,
                                              Collection<NamedType> subtypes) {
        builder.typeIdVisibility(true);
        for (NamedType subtype : subtypes) {
            String name = subtype.getType().getName();
            if (broadcastMapping.containsKey(name)) {
                String value = broadcastMapping.getProperty(name);
//                builder.registerSubtypeWithTypeId(subtype.getType(), value);
            } else {
//                builder.registerSubtype(subtype.getType());
            }
        }
        return builder.buildTypeSerializer(config, baseType, subtypes);
    }

    @Override
    public StdTypeResolverBuilder init(JsonTypeInfo.Id idType, TypeIdResolver idRes) {
        builder.init(idType, idRes);
        return builder;
    }

    @Override
    public JacksonTypeResolver inclusion(JsonTypeInfo.As includeAs) {
        builder.inclusion(includeAs);
        return this;
    }

}
