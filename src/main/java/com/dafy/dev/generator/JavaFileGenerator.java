package com.dafy.dev.generator;

import com.dafy.dev.config.JavaFileConfig;
import com.dafy.dev.config.MethodInfo;
import com.dafy.dev.config.ParameterInfo;
import com.dafy.dev.util.SourceCodeUtil;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.sun.codemodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by chunxiaoli on 10/13/16.
 */
public class JavaFileGenerator {

    private JavaFileConfig javaFileConfig;

    private ClassLoader classLoader;

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public JavaFileConfig getJavaFileConfig() {
        return javaFileConfig;
    }

    public void setJavaFileConfig(JavaFileConfig javaFileConfig) {
        this.javaFileConfig = javaFileConfig;
    }

    public static FieldSpec getLogger(String clsName) {
        //private Logger logger= LoggerFactory.getLogger(AccountServiceImpl.class);
        return FieldSpec
                .builder(Logger.class, "logger", Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
                .initializer("$T.$N($N)", LoggerFactory.class, "getLogger", clsName + ".class")
                .build();
    }

    public JavaFileGenerator() {
    }

    public JavaFileGenerator(JavaFileConfig javaFileConfig) {
        this.javaFileConfig = javaFileConfig;
    }

    public void generator() {
        switch (javaFileConfig.getKind()) {
            case CLASS:
                break;
            case INTERFACE:
                generateInterface();
                break;
            case ANNOTATION:
                break;

        }

    }

    public void generateInterface() {
        generateInterface(this.javaFileConfig);
    }

    public void generateInterface(JavaFileConfig javaFileConfig) {
        TypeSpec.Builder builder = TypeSpec.interfaceBuilder(javaFileConfig.getClassName())
                .addJavadoc(javaFileConfig.getJavaFileDoc())
                .addModifiers(Modifier.PUBLIC);

        if (javaFileConfig.getInterfaces() != null) {
            for (String methodName : javaFileConfig.getInterfaces()) {
                MethodSpec m = MethodSpec.methodBuilder(methodName)
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addParameter(String[].class, "args")
                        .build();
                builder.addMethod(m);
            }
        }

        doGenerate(builder);
    }

    public void generateProviderHookFile() {
        generateProviderHookFile(this.javaFileConfig);
    }

    public void generateProviderHookFile(JavaFileConfig javaFileConfig) {
        TypeSpec.Builder builder = TypeSpec.classBuilder(javaFileConfig.getClassName())
                .addJavadoc(javaFileConfig.getJavaFileDoc())
                .addModifiers(Modifier.PUBLIC);

        builder.addAnnotation(SpringBootApplication.class);

        doGenerate(builder);
    }

    public String doGenerate(TypeSpec.Builder builder) {
        TypeSpec typeSpec = builder.build();
        JavaFile javaFile = JavaFile.builder(javaFileConfig.getPackageName(), typeSpec).build();
        try {
            //javaFile.writeTo(System.out);
            File dir = new File(javaFileConfig.getOutDir());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            javaFile.writeTo(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return getFinalPath();

    }

    private String getFinalPath() {
        return javaFileConfig.getOutDir() + File.separator
                + SourceCodeUtil.convertPackage2Dir(javaFileConfig.getPackageName())
                + File.separator + javaFileConfig.getClassName() + ".java";
    }

    public void generateDaoImpl(HashMap<String, String> fields,
                                String returnType,
                                String interfaceName, String packageName) {

        JCodeModel codeModel = new JCodeModel();

        JPackage jPackage = codeModel._package(packageName);

        JDefinedClass definedClass;

        try {
            definedClass = jPackage._class(this.javaFileConfig.getClassName());

            definedClass._implements(codeModel.ref(interfaceName));

            definedClass.annotate(Component.class);

            HashMap<String, JFieldVar> map = new LinkedHashMap<>();

            for (Map.Entry<String, String> fieldMap : fields.entrySet()) {
                String fieldName = fieldMap.getValue();

                JType fieldType;

                fieldType = codeModel.ref(fieldMap.getKey());

                JFieldVar fieldVar = definedClass
                        .field(JMod.PRIVATE, fieldType, SourceCodeUtil.covertFieldName(fieldName));

                fieldVar.annotate(javax.annotation.Resource.class);

                fieldVar.javadoc().add("comment");

                map.put(fieldName, fieldVar);

                if (this.javaFileConfig.getMethodInfos() != null) {
                    for (MethodInfo m : this.javaFileConfig.getMethodInfos()) {
                        JMethod method = definedClass
                                .method(JMod.PUBLIC, codeModel.ref(m.getReturnTypeFullClassName()),
                                        m.getName());
                        for (ParameterInfo p : m.getParameterInfoList()) {
                            method.param(codeModel.ref(p.getTypeFullClassName()), p.getName());
                        }
                        JBlock block = method.body();
                        block.directStatement("//todo");
                        block._return(JExpr._null());
                    }
                }
            }

        } catch (JClassAlreadyExistsException e) {
            e.printStackTrace();
        }

        try {
            codeModel.build(new File(javaFileConfig.getOutDir()));

        } catch (Exception e) {
            throw new IllegalStateException("ex", e);
        }
    }

    public static boolean isPrimaryType(String type) {
        return type.equals("int") || type.equals("char")
                || type.equals("long") || type.equals("double")
                || type.equals("float") || type.equals("byte")
                || type.equals("boolean");
    }

    public static boolean isPrimaryWrapperType(String type) {
        return type.equals("Integer") || type.equals("Long")
                || type.equals("Character") || type.equals("Float")
                || type.equals("Double") || type.equals("Byte")
                || type.equals("Boolean");
    }

    public static ClassName resolveClassName(String fullName) {
        return ClassName.get(getPackageName(fullName), getClassName(fullName));
    }

    public static TypeName resolveType(String fullName) {
        return ClassName.get(getPackageName(fullName), getClassName(fullName));
    }

    public static Class resolvePrimaryType(String fullName) {
        Class typeName = null;
        switch (fullName) {
            case "int":
                typeName = Integer.TYPE;
                break;
            case "char":
                typeName = Character.TYPE;
                break;
            case "long":
                typeName = Long.TYPE;
                break;
            case "float":
                typeName = Float.TYPE;
                break;
            case "double":
                typeName = Double.TYPE;
                break;
            case "byte":
                typeName = Byte.TYPE;
                break;
            case "boolean":
                typeName = Boolean.TYPE;
                break;

        }
        return typeName;
    }

    public static TypeName getPrimaryType(String fullName) {
        Class typeName = getPrimaryClass(fullName);
        return typeName == null ? null : ClassName.get(typeName);
    }

    public static Class getPrimaryClass(String fullName) {
        Class typeName = null;
        switch (fullName) {
            case "int":
                typeName = Integer.class;
                break;
            case "char":
                typeName = Character.class;
                break;
            case "long":
                typeName = Long.class;
                break;
            case "float":
                typeName = Float.class;
                break;
            case "double":
                typeName = Double.class;
                break;
            case "byte":
                typeName = Byte.class;
                break;
            case "boolean":
                typeName = Boolean.class;
                break;

        }
        return typeName;
    }

    public static String getClassName(String fullClassName) {
        int idx = fullClassName.lastIndexOf(".");
        return fullClassName.substring(idx + 1, fullClassName.length());
    }

    public static String getPackageName(String fullClassName) {

        int idx = fullClassName.lastIndexOf(".");
        return idx > 0 ? fullClassName.substring(0, idx) : fullClassName;
    }

}
