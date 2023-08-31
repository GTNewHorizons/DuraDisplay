package duradisplay;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes("com.caedis.duradisplay.annotation.Config")
public class ConfigProcessor extends AbstractProcessor {

    private Messager messager;
    private final ArrayList<String> fullNames = new ArrayList<>();

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

    private boolean isPublicStaticFinal(Element element) {
        return element.getModifiers().contains(Modifier.PUBLIC)
               && element.getModifiers().contains(Modifier.STATIC)
               && element.getModifiers().contains(Modifier.FINAL);
    }

    private boolean testField(TypeElement element, String qualifiedTypeName, String name) {
        var field = element.getEnclosedElements().stream().filter(e ->
            e.getKind() == ElementKind.FIELD && e.getSimpleName().toString().equals(name)
        ).findFirst();

        if (!field.isPresent()) {
            error("Config class must have a public final static String field named category", element);
            return false;
        }

        if (!isPublicStaticFinal(field.get())) {
            error(name + " field must be public static final", field.get());
        }

        if (!field.get().asType().toString().equals(qualifiedTypeName)) {
            error("category field must be " + qualifiedTypeName, field.get());
        }

        return true;
    }

    private boolean testFieldCategory(TypeElement element) {
        return testField(element, "java.lang.String", "category");
    }

    private boolean testFieldInstance(TypeElement element) {
        return testField(element, element.getQualifiedName().toString(), "instance");
    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        var targetAnnotation = annotations.stream().findFirst();
        if (!targetAnnotation.isPresent())
            return false;

        for (Element element : roundEnv.getElementsAnnotatedWith(targetAnnotation.get())) {
            if (element.getKind() == ElementKind.CLASS && !element.getModifiers().contains(Modifier.ABSTRACT)) {
                var te = (TypeElement) element;
                if (testFieldInstance(te) && testFieldCategory(te))
                    fullNames.add(String.format("\"%s\"", te.getQualifiedName().toString()));
            }
        }

        try (var writer = new PrintWriter(
            processingEnv.getFiler()
                .createSourceFile("com.caedis.duradisplay.config.ConfigInfo")
                .openWriter())
        ) {

            writer.println(String.format("""
                    package com.caedis.duradisplay.config;
                    import com.caedis.duradisplay.config.Config;

                    import java.util.Arrays;

                    public class ConfigInfo {
                        private static final String[] configClassNames = new String[]{ %s };
                        private static String[] categorys = null;
                        private static OverlayConfig[] configs = null;

                        public static String[] getCategories() {
                            if (categorys == null) {
                                categorys = Arrays.stream(configClassNames)
                                    .map(n -> {
                                        try {
                                            return Class.forName(n).getField("category").get(null).toString();
                                        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }).toArray(String[]::new);
                            }
                            return categorys;
                        }

                        public static OverlayConfig[] getConfigs() {
                            if (configs == null) {
                                configs = Arrays.stream(configClassNames)
                                    .map(n -> {
                                        try {
                                            return (OverlayConfig) Class.forName(n).getField("instance").get(null);
                                        } catch (Exception e) {
                                            throw new RuntimeException(e);
                                        }
                                    }).toArray(OverlayConfig[]::new);
                            }
                            return configs;
                        }
                    }

                                            """,
                String.join(", ", fullNames)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
