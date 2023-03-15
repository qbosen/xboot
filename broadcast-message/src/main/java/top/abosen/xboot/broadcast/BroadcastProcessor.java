package top.abosen.xboot.broadcast;

import com.google.auto.common.MoreElements;
import com.google.auto.service.AutoService;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.SimpleAnnotationValueVisitor8;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static com.google.auto.common.AnnotationMirrors.getAnnotationValue;
import static com.google.auto.common.MoreElements.getAnnotationMirror;
import static com.google.auto.common.MoreStreams.toImmutableSet;
import static javax.tools.Diagnostic.Kind;

/**
 * @author qiubaisen
 * @since 2023/3/14
 */


@AutoService(Processor.class)
@SupportedOptions({"debug"})
public class BroadcastProcessor extends AbstractProcessor {
    @VisibleForTesting
    static final String MISSING_NAME_ERROR = "No broadcast message name provided for element!";

    private final Map<String, String> namedBroadcast = Maps.newHashMap();

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return ImmutableSet.of(BroadcastMessage.class.getName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            generateConfigFiles();
        } else {
            processAnnotations(annotations, roundEnv);
        }

        return false;
    }

    private void processAnnotations(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(BroadcastMessage.class);
        log(annotations.toString());
        log(elements.toString());

        for (Element e : elements) {
            // TODO(gak): check for error trees?
            TypeElement messageImpl = MoreElements.asType(e);
            AnnotationMirror annotationMirror = getAnnotationMirror(e, BroadcastMessage.class).get();
            Set<String> messageNamedTypes = getValueFieldOfClasses(annotationMirror);
            if (messageNamedTypes.isEmpty()) {
                error(MISSING_NAME_ERROR, e, annotationMirror);
                continue;
            }
            String className = getBinaryName(messageImpl);
            for (String messageName : messageNamedTypes) {
                log("msg name" + messageName);
                log("provider implementer: " + messageImpl.getQualifiedName());

                namedBroadcast.put(messageName, className);
            }
        }

    }

    private void generateConfigFiles() {
        Filer filer = processingEnv.getFiler();
        Properties properties = new Properties();
        String resourceFile = BroadcastFile.FILE_PATH;

        try {
            // would like to be able to print the full path
            // before we attempt to get the resource in case the behavior
            // of filer.getResource does change to match the spec, but there's
            // no good way to resolve CLASS_OUTPUT without first getting a resource.
            FileObject existingFile =
                    filer.getResource(StandardLocation.CLASS_OUTPUT, "", resourceFile);
            log("Looking for existing resource file at " + existingFile.toUri());
            Properties existingConfig;
            try (InputStream inputStream = existingFile.openInputStream()) {
                existingConfig = BroadcastFile.readConfigFile(inputStream);
            }
            log("Existing broadcast configs: " + existingConfig);
            properties.putAll(existingConfig);
        } catch (IOException e) {
            // According to the javadoc, Filer.getResource throws an exception
            // if the file doesn't already exist.  In practice this doesn't
            // appear to be the case.  Filer.getResource will happily return a
            // FileObject that refers to a non-existent file but will throw
            // IOException if you try to open an input stream for it.
            log("Resource file did not already exist.");
        }

        log("Working on resource file: " + resourceFile);
        try {
            properties.putAll(namedBroadcast);

            log("New service file contents: " + properties);
            FileObject fileObject =
                    filer.createResource(StandardLocation.CLASS_OUTPUT, "", resourceFile);
            try (OutputStream out = fileObject.openOutputStream()) {
                BroadcastFile.writeConfigFile(properties, out);
            }
            log("Wrote to: " + fileObject.toUri());
        } catch (IOException e) {
            fatalError("Unable to create " + resourceFile + ", " + e);
        }

    }

    private ImmutableSet<String> getValueFieldOfClasses(AnnotationMirror annotationMirror) {
        return getAnnotationValue(annotationMirror, "value")
                .accept(
                        new SimpleAnnotationValueVisitor8<ImmutableSet<String>, Void>(ImmutableSet.of()) {
                            @Override
                            public ImmutableSet<String> visitString(String s, Void unused) {
                                return ImmutableSet.of(s);
                            }

                            @Override
                            public ImmutableSet<String> visitArray(
                                    List<? extends AnnotationValue> values, Void v) {
                                return values.stream()
                                        .flatMap(value -> value.accept(this, null).stream())
                                        .collect(toImmutableSet());
                            }
                        },
                        null);
    }

    /**
     * Returns the binary name of a reference type. For example,
     * {@code com.google.Foo$Bar}, instead of {@code com.google.Foo.Bar}.
     */
    private String getBinaryName(TypeElement element) {
        return getBinaryNameImpl(element, element.getSimpleName().toString());
    }

    private String getBinaryNameImpl(TypeElement element, String className) {
        Element enclosingElement = element.getEnclosingElement();

        if (enclosingElement instanceof PackageElement) {
            PackageElement pkg = MoreElements.asPackage(enclosingElement);
            if (pkg.isUnnamed()) {
                return className;
            }
            return pkg.getQualifiedName() + "." + className;
        }

        TypeElement typeElement = MoreElements.asType(enclosingElement);
        return getBinaryNameImpl(typeElement, typeElement.getSimpleName() + "$" + className);
    }


    private void log(String msg) {
        if (processingEnv.getOptions().containsKey("debug")) {
            processingEnv.getMessager().printMessage(Kind.NOTE, msg);
        }
    }

    private void warning(String msg, Element element, AnnotationMirror annotation) {
        processingEnv.getMessager().printMessage(Kind.WARNING, msg, element, annotation);
    }

    private void error(String msg, Element element, AnnotationMirror annotation) {
        processingEnv.getMessager().printMessage(Kind.ERROR, msg, element, annotation);
    }

    private void fatalError(String msg) {
        processingEnv.getMessager().printMessage(Kind.ERROR, "FATAL ERROR: " + msg);
    }
}
