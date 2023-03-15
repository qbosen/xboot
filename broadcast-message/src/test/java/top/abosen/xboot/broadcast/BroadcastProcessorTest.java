package top.abosen.xboot.broadcast;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.Locale;
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
    void should() throws IOException {
        Compilation compilation = Compiler.javac()
                .withProcessors(new BroadcastProcessor())
                .compile(JavaFileObjects.forResource("test/MultipleName.java"));

        assertThat(compilation).succeededWithoutWarnings();

        Optional<JavaFileObject> generatedConfig = compilation.generatedFile(StandardLocation.CLASS_OUTPUT, "/META-INF/broadcast.properties");

        assertThat(generatedConfig).isPresent();

        Properties configFile = BroadcastFile.readConfigFile(generatedConfig.get().openInputStream());
        assertThat(configFile)
                .containsEntry("alias2", "test.MultipleName")
                .containsEntry("simple-message", "test.MultipleName");
    }

    @Test
    void should_failed_if_annotation_without_value() {
        Compilation compilation = Compiler.javac()
                .withProcessors(new BroadcastProcessor())
                .compile(JavaFileObjects.forResource("test/NoName.java"));
        assertThat(compilation).failed();
        assertThat(compilation).hadErrorContaining(BroadcastProcessor.MISSING_NAME_ERROR);
    }
}