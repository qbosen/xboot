package top.abosen.xboot.extensionfield.jackson;

import cn.hutool.core.stream.StreamUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.google.auto.service.AutoService;
import top.abosen.xboot.extensionfield.util.Utils;
import top.abosen.xboot.extensionfield.extension.ExtensionField;
import top.abosen.xboot.extensionfield.schema.Schema;
import top.abosen.xboot.extensionfield.widget.BizWidgetExtension;
import top.abosen.xboot.extensionfield.widget.Widget;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author qiubaisen
 * @date 2023/2/21
 */
@AutoService(Module.class)
public class DynamicSubtypeModule extends Module {

    public static final Version VERSION = VersionUtil.parseVersion("2.14.3", "top.abosen.xboot", "extension-field");
    private final ConcurrentHashMap<Class<?>, List<NamedType>> typeMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Class<?>, String> typeNameMap = new ConcurrentHashMap<>();

    private static List<NamedType> namedSubtypesFromAnno(Class<?> type) {
        return Utils.getAnno(type, JsonSubType.class).map(anno ->
                        Arrays.stream(anno.value()).filter(StrUtil::isNotBlank)
                                .map(name -> new NamedType(type, name)).collect(Collectors.toList()))
                .orElseGet(Collections::emptyList);
    }

    @Override
    public String getModuleName() {
        return VERSION.getArtifactId();
    }

    @Override
    public Version version() {
        return VERSION;
    }

    @Override
    public void setupModule(SetupContext context) {
        initSchemaSubtypes();
        initWidgetSubtypes();
        initFieldSubtypes();
        initBizSubtypes();
        initSubtypes();

        context.insertAnnotationIntrospector(new AnnotationIntrospector() {
            @Override
            public Version version() {
                return VERSION;
            }

            @Override
            public String findTypeName(AnnotatedClass ac) {
                return typeNameMap.get(ac.getRawType());
            }

            @Override
            public List<NamedType> findSubtypes(Annotated a) {
                return typeMap.getOrDefault(a.getRawType(), Collections.emptyList());
            }
        });
    }

    private void initSchemaSubtypes() {
        List<NamedType> schemaSubtypes = StreamUtil.of(ServiceLoader.load(Schema.class).iterator())
                .map(it -> new NamedType(it.getClass(), it.getType()))
                .collect(Collectors.toList());
        registerNamedSubtypes(Schema.class, schemaSubtypes);
    }

    private void initWidgetSubtypes() {
        List<NamedType> widgetSubtypes = StreamUtil.of(ServiceLoader.load(Widget.class).iterator())
                .map(it -> new NamedType(it.getClass(), it.getType()))
                .collect(Collectors.toList());
        registerNamedSubtypes(Widget.class, widgetSubtypes);
    }

    private void initBizSubtypes() {
        List<NamedType> bizExtensionSubtypes = StreamUtil.of(ServiceLoader.load(BizWidgetExtension.class).iterator())
                .map(it -> new NamedType(it.getClass(), it.getKey()))
                .collect(Collectors.toList());
        registerNamedSubtypes(BizWidgetExtension.class, bizExtensionSubtypes);
    }

    private void initFieldSubtypes() {
        List<NamedType> extensionFieldSubtypes = StreamUtil.of(ServiceLoader.load(ExtensionField.class).iterator())
                .map(it -> new NamedType(it.getClass(), it.getType()))
                .collect(Collectors.toList());
        registerNamedSubtypes(ExtensionField.class, extensionFieldSubtypes);
    }

    private void initSubtypes() {
        StreamUtil.of(ServiceLoader.load(ParentTypeResolver.class).iterator())
                .flatMap(resolver -> resolver.getParentTypes().stream())
                .distinct().forEach(this::registerParentSpiType);
    }

    public void registerParentSpiType(Class<?> parentType) {
        if (shouldRegister(parentType)) {
            registerSubtypesNamedByAnno(parentType, StreamUtil.of(ServiceLoader.load(parentType).iterator()).map(Object::getClass).collect(Collectors.toList()));
        }
    }

    public void registerSubtypesNamedByAnno(Class<?> parentType, List<Class<?>> subtypes) {
        if (shouldRegister(parentType)) {
            List<NamedType> namedTypes = subtypes.stream().flatMap(subtype -> namedSubtypesFromAnno(subtype).stream()).collect(Collectors.toList());
            registerNamedSubtypes(parentType, namedTypes);
        }
    }

    public void registerNamedSubtypes(Class<?> parentType, List<NamedType> namedTypes) {
        if (shouldRegister(parentType)) {
            Set<String> seenNames = new HashSet<>();
            List<NamedType> namedSubtypes = new ArrayList<>();
            namedTypes.forEach(namedType -> {
                if (seenNames.contains(namedType.getName())) {
                    throw new IllegalArgumentException("Parent type [" + parentType + "] got repeated subtype name [" + namedType.getName() + "]");
                }
                seenNames.add(namedType.getName());
                namedSubtypes.add(namedType);
                typeNameMap.putIfAbsent(namedType.getType(), namedType.getName());
            });
            typeMap.put(parentType, namedSubtypes);
        }
    }

    public boolean shouldRegister(Class<?> parentType) {
        return !typeMap.containsKey(parentType);
    }

    public void removeRegister(Class<?> parentType) {
        typeMap.remove(parentType);
    }

}
