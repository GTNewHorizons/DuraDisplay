package com.caedis.duradisplay;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@SupportedAnnotationTypes("com.caedis.duradisplay.annotation.Overlay")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class OverlayProcessor extends AbstractProcessor {

    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
    }

    private void error(String msg, Element element) {
        messager.printMessage(Diagnostic.Kind.ERROR, msg, element);
    }

    private void warning(String msg, Element element) {
        messager.printMessage(Diagnostic.Kind.WARNING, msg, element);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        var targetAnnotation = annotations.stream().findFirst();
        if (!targetAnnotation.isPresent())
            return false;

        var names = roundEnv.getElementsAnnotatedWith(targetAnnotation.get()).stream()
            .filter(e -> e.getKind() == ElementKind.CLASS && !e.getModifiers().contains(Modifier.ABSTRACT))
            .map(e -> (TypeElement) e)
            .map(TypeElement::getQualifiedName)
            .map(Object::toString)
            .map(s -> String.format("\"%s\"", s))
            .toArray(String[]::new);

        try (var writer = new PrintWriter(
            processingEnv.getFiler()
                .createSourceFile("com.caedis.duradisplay.overlay.OverlayInfo")
                .openWriter())
        ) {

            writer.println(String.format("""
                    package com.caedis.duradisplay.overlay;

                    import com.caedis.duradisplay.config.Config;
                    import com.caedis.duradisplay.overlay.Overlay;
                    import org.jetbrains.annotations.NotNull;
                    import org.jetbrains.annotations.Nullable;

                    import java.util.Arrays;

                    public class OverlayInfo {
                        private static final String[] overlayClassNames = { %s };
                        private static Overlay[] overlays = {};

                        @NotNull
                        public static Overlay[] getOverlays() {
                            if (overlays.length == 0) {
                                overlays = Arrays.stream(overlayClassNames)
                                    .map(n -> {
                                        try {
                                            return (Overlay) Class.forName(n).getConstructor().newInstance();
                                        } catch (Exception e) {
                                            throw new RuntimeException(e);
                                        }
                                    }).toArray(Overlay[]::new);
                            }
                            return overlays;
                        }

                        @Nullable
                        public static Config[] getConfigs() {
                            return Arrays.stream(getOverlays()).map(Overlay::config).toArray(Config[]::new);
                        }
                    }

                                                                                    """,
                String.join(", ", names)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
