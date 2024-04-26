package com.trs.app.tools.auto_regiter_annotation_processer;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.trs.app.tools.auto_register_annotation.AutoRegister;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * <pre>
 * Created by zhuguohui
 * Date: 2024/4/26
 * Time: 10:41
 * Desc:
 * </pre>
 */
@AutoService(Processor.class)
public class AutoRegisterProcessor extends AbstractProcessor {

    private Messager messager;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
    }


    //这个方法非常必要，否则将不会执行到process()方法
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(AutoRegister.class.getCanonicalName());
    }


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Map<String, Set<String>> registeredClassNamesMap = new HashMap<>();
        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
            for (Element element : annotatedElements) {
                TypeElement typeElement = (TypeElement) element;
                String className = typeElement.getQualifiedName().toString();
                String generatedClassName = typeElement.getAnnotation(AutoRegister.class).value();
                Set<String> set = registeredClassNamesMap.get(generatedClassName);
                if (set == null) {
                    set = new HashSet<>();
                    registeredClassNamesMap.put(generatedClassName, set);
                }
                set.add(className);
            }
        }


        if (!registeredClassNamesMap.isEmpty()) {
            Set<String> generateClassNameSet = registeredClassNamesMap.keySet();
            for (String generatedClassName : generateClassNameSet) {
                try {
                    generateAutoRegisterClass(generatedClassName, registeredClassNamesMap.get(generatedClassName));
                } catch (IOException e) {
                    messager.printMessage(Diagnostic.Kind.ERROR, "Error generating auto-register class: " + e.getMessage());
                }
            }


        }
        return true;
    }


    private void generateAutoRegisterClass(String fullClassName, Set<String> registeredClassNames) throws IOException {

        if (fullClassName == null || fullClassName.isEmpty()) {
            return;
        }

        String packageName = fullClassName.substring(0, fullClassName.lastIndexOf("."));
        String className = fullClassName.substring(fullClassName.lastIndexOf(".")+1);

        // 创建CLASSES数组

        StringBuilder nameBuilder = new StringBuilder();
        for (String rName : registeredClassNames) {
            nameBuilder.append("\n\"" + rName + "\",");
        }

        TypeName stringArrayType = ArrayTypeName.of(String.class);
        CodeBlock arrayInitializer = CodeBlock.builder()
                .add("new String[]{ $L}", nameBuilder.toString())
                .build();

        // 创建CLASSES字段
        FieldSpec classesField = FieldSpec.builder(stringArrayType, "CLASSES")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer(arrayInitializer)
                .build();

        // 创建类并添加字段和初始化块
        // 创建JsMethodRegister类
        TypeSpec registerTypeSpec = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addField(classesField)
                .build();

        // 将类写入文件
        JavaFile javaFile = JavaFile.builder(packageName, registerTypeSpec)
                .build();
        javaFile.writeTo(filer);
    }
}