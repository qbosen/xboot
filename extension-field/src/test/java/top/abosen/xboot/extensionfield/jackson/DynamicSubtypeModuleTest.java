package top.abosen.xboot.extensionfield.jackson;

import cn.hutool.core.collection.ListUtil;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.google.auto.service.AutoService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.BasicJsonTester;
import org.springframework.boot.test.json.JsonContent;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author qiubaisen
 * @since 2023/2/22
 */
class DynamicSubtypeModuleTest {

    @AutoService(ParentTypeResolver.class)
    public static class ParentTypeContainer implements ParentTypeResolver {
        @Override
        public List<Class<?>> getParentTypes() {
            return ListUtil.of(Parent.class);
        }
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
    interface Parent {
    }

    ObjectMapper objectMapper;
    BasicJsonTester jsonTester;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper().findAndRegisterModules();
        jsonTester = new BasicJsonTester(getClass());
    }

    @JsonSubType("child")
    @AutoService(Parent.class)
    @Data
    public static class Child implements Parent {
        String foo;
    }

    @Test
    void should_write_json_for_subtype() throws JsonProcessingException {
        Child child = new Child();
        child.setFoo("foo");
        final Parent parent = child;
        JsonContent<Object> json = jsonTester.from(objectMapper.writeValueAsString(parent));
        assertThat(json).extractingJsonPathValue("$.@type").isEqualTo("child");
        assertThat(json).extractingJsonPathValue("$.foo").isEqualTo("foo");
    }

    @Test
    void should_parse_json_for_subtype() throws JsonProcessingException {
        Parent unmarshal = objectMapper.readValue("{\"@type\": \"child\", \"foo\": \"bar\"}", Parent.class);
        assertThat(unmarshal).isInstanceOf(Child.class).hasFieldOrPropertyWithValue("foo", "bar");
    }


    @JsonSubType({"another-child", "child2"})
    @AutoService(Parent.class)
    @Data
    public static class AlternativeChild implements Parent {
        String foo;
    }

    @Test
    void should_parse_alternative_name_for_subtype() throws JsonProcessingException {
        Parent child1 = objectMapper.readValue("{\"@type\": \"another-child\", \"foo\": \"bar\"}", Parent.class);
        Parent child2 = objectMapper.readValue("{\"@type\": \"child2\", \"foo\": \"bar\"}", Parent.class);
        assertThat(child1).isInstanceOf(AlternativeChild.class).hasFieldOrPropertyWithValue("foo", "bar");
        assertThat(child2).isInstanceOf(AlternativeChild.class).hasFieldOrPropertyWithValue("foo", "bar");
    }


    @JsonSubType("grandson")
    @AutoService(Parent.class)
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Grandson extends Child {
        String data;
    }

    @Test
    void should_write_json_for_sub_subtype() throws JsonProcessingException {
        Grandson grandson = new Grandson();
        grandson.setFoo("foo");
        grandson.setData("data");

        final Parent parent = grandson;
        JsonContent<Object> json = jsonTester.from(objectMapper.writeValueAsString(parent));

        assertThat(json).extractingJsonPathValue("$.@type").isEqualTo("grandson");
        assertThat(json).extractingJsonPathValue("$.foo").isEqualTo("foo");
        assertThat(json).extractingJsonPathValue("$.data").isEqualTo("data");
    }

    @Test
    void should_parse_json_for_sub_subtype() throws JsonProcessingException {
        Parent unmarshal = objectMapper.readValue("{\"@type\": \"grandson\", \"foo\": \"bar\", \"data\":  \"meta\"}", Parent.class);
        assertThat(unmarshal).isInstanceOf(Grandson.class)
                .hasFieldOrPropertyWithValue("foo", "bar")
                .hasFieldOrPropertyWithValue("data", "meta");
    }


    @Test
    void should_throw_exception_if_subtype_name_duplicated() {
        DynamicSubtypeModule module = new DynamicSubtypeModule();
        module.removeRegister(Parent.class);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> module.registerNamedSubtypes(Parent.class, ListUtil.of(
                        new NamedType(Child.class, "duplicate-name"),
                        new NamedType(AlternativeChild.class, "duplicate-name"))))
                .withMessageContainingAll("repeated subtype", "duplicate-name");
    }
}