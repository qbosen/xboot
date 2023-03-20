package top.abosen.xboot.broadcast;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author qiubaisen
 * @since 2023/3/14
 */
class BroadcastProcessorTest {

    @Test
    void should_generate_BroadcastConfigFile_when_single_name_configured() throws IOException {
        Compilation compilation = Compiler.javac()
                .withProcessors(new BroadcastProcessor())
                .compile(JavaFileObjects.forResource("test/SingleName.java"));

        assertThat(compilation).succeededWithoutWarnings();

        Optional<JavaFileObject> generatedConfig = compilation.generatedFile(StandardLocation.CLASS_OUTPUT, "META-INF/broadcast.properties");

        assertThat(generatedConfig).isPresent();

        Properties configFile = BroadcastFile.readConfigFile(generatedConfig.get().openInputStream());
        assertThat(configFile)
                .hasSize(1)
                .containsEntry("message-name", "test.SingleName");
    }

    @Test
    void should_generate_BroadcastConfigFile_when_multiple_name_configured() throws IOException {
        Compilation compilation = Compiler.javac()
                .withProcessors(new BroadcastProcessor())
                .compile(JavaFileObjects.forResource("test/MultipleName.java"));

        assertThat(compilation).succeededWithoutWarnings();

        Optional<JavaFileObject> generatedConfig = compilation.generatedFile(StandardLocation.CLASS_OUTPUT, "META-INF/broadcast.properties");

        assertThat(generatedConfig).isPresent();

        Properties configFile = BroadcastFile.readConfigFile(generatedConfig.get().openInputStream());
        assertThat(configFile)
                .hasSize(2)
                .containsEntry("alias2", "test.MultipleName")
                .containsEntry("simple-message", "test.MultipleName");
    }

    @Test
    void should_fail_if_annotation_without_value() {
        Compilation compilation = Compiler.javac()
                .withProcessors(new BroadcastProcessor())
                .compile(JavaFileObjects.forResource("test/NoName.java"));
        assertThat(compilation).failed();
        assertThat(compilation).hadErrorContaining(BroadcastProcessor.MISSING_NAME_ERROR);
    }

    @Test
    void should_not_generate_configFile_if_annotation_not_present() {
        Compilation compilation = Compiler.javac()
                .withProcessors(new BroadcastProcessor())
                .compile(JavaFileObjects.forResource("test/NoAnnotation.java"));
        assertThat(compilation).succeededWithoutWarnings();
        assertThat(compilation.generatedFile(StandardLocation.CLASS_OUTPUT, "META-INF/broadcast.properties")).isEmpty();
    }

    @Test
    void should_generate_BroadcastConfigFile_for_static_nested_class() throws IOException {
        Compilation compilation = Compiler.javac()
                .withProcessors(new BroadcastProcessor())
                .compile(JavaFileObjects.forResource("test/Nested.java"));

        assertThat(compilation).succeededWithoutWarnings();

        Optional<JavaFileObject> generatedConfig = compilation.generatedFile(StandardLocation.CLASS_OUTPUT, "META-INF/broadcast.properties");

        assertThat(generatedConfig).isPresent();

        Properties configFile = BroadcastFile.readConfigFile(generatedConfig.get().openInputStream());
        assertThat(configFile)
                .hasSize(2)
                .containsEntry("foo", "test.Nested$Foo")
                .containsEntry("bar", "test.Nested$Bar");
    }

    @Test
    void should_fail_if_broadcastMessages_have_duplicated_name() {
        Compilation compilation = Compiler.javac()
                .withProcessors(new BroadcastProcessor())
                .compile(JavaFileObjects.forResource("test/DuplicateName.java"));
        assertThat(compilation).failed();
        assertThat(compilation).hadErrorContaining(BroadcastProcessor.DUPLICATED_NAME_ERROR);
    }
}