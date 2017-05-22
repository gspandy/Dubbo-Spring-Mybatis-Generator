package com.dafy.dev.generator.web;

import com.dafy.dev.codegen.ClassLoaderUtil;
import com.dafy.dev.config.DtoConfig;
import com.dafy.dev.config.JavaFileConfig;
import com.dafy.dev.generator.ProjectGenerator;
import com.dafy.dev.generator.common.Generator;
import com.dafy.dev.generator.common.JavaFileGenerator;
import com.dafy.dev.pojo.CgiConfig;
import com.dafy.dev.pojo.CgiInfo;
import com.dafy.dev.pojo.ControllerConfig;
import com.dafy.dev.util.SourceCodeUtil;
import com.squareup.javapoet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.lang.model.element.Modifier;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by chunxiaoli on 10/19/16.
 */
public class ControllerJavaFileGenerator implements Generator {

    private static final Logger logger = LoggerFactory.getLogger(ProjectGenerator.class);

    private JavaFileGenerator generator;
    private JavaFileConfig    javaFileConfig;

    private DtoConfig dtoConfig;

    URLClassLoader classLoader = null;


    public ControllerJavaFileGenerator(JavaFileConfig config, DtoConfig dtoConfig) {
        this.dtoConfig = dtoConfig;
        this.javaFileConfig = config;
        generator = new JavaFileGenerator(config);
    }

    @Override
    public void generate() {

    }

    private String getClassName(String fullClassName) {
        int idx = fullClassName.lastIndexOf(".");
        return fullClassName.substring(idx + 1, fullClassName.length());
    }

    private String getPackageName(String fullClassName) {
        int idx = fullClassName.lastIndexOf(".");
        return fullClassName.substring(0, idx);
    }






    public String generateControllerFile(ControllerConfig controllerConfig,String constantsClass) {

        String className = javaFileConfig.getClassName();
        TypeSpec.Builder builder = TypeSpec.classBuilder(className)
                .addAnnotation(Controller.class)
                .addJavadoc(javaFileConfig.getJavaFileDoc())
                .addModifiers(Modifier.PUBLIC);

        FieldSpec logger =JavaFileGenerator.getLogger(className);

        //TypeName typeName=TypeName.get(cgiConstantsClass.name)

        Class cls = ClassLoaderUtil.loadFromFile(constantsClass);

        String dtoDir= dtoConfig.getDir()+ File.separator+
                SourceCodeUtil.convertPackage2Dir(dtoConfig.getPackageName()+".dto");

        ClassLoader classLoader= ClassLoaderUtil.loadAllClass(dtoDir);

        controllerConfig.getCgiInfoList().forEach(item -> {
            String cgi = item.getCgi();
            //todo dry

            ParameterSpec reqDto = null;
            Class rspCls = null;
            if (item.getRequest() != null) {
                reqDto = ParameterSpec.builder(getReqDtoClass(item,classLoader), "reqDto")
                        .addAnnotation(RequestBody.class)
                        .build();
            }

            if (item.getResponse() != null) {
                rspCls = getResDtoClass(item,classLoader);
            }

            ParameterSpec req = ParameterSpec.builder(HttpServletRequest.class, "request").build();

            ParameterSpec response = ParameterSpec.builder(HttpServletResponse.class, "response")
                    .build();

            AnnotationSpec mapping = AnnotationSpec.builder(RequestMapping.class).addMember("value",
                    "$T.$N", cls, cgi.toUpperCase())
                    .build();

            AnnotationSpec resp = AnnotationSpec.builder(ResponseBody.class)
                    .build();

            String methodName=SourceCodeUtil.convertFieldUppercase(cgi);

            MethodSpec.Builder b = MethodSpec
                    .methodBuilder(methodName)
                    .addAnnotation(mapping)
                    .addAnnotation(resp)
                    .addModifiers(Modifier.PUBLIC);

            if (reqDto != null) {
                b.addParameter(reqDto);
                b.addStatement("$N.debug(\"$N {}\",$N)",logger,methodName,reqDto);
            }else {
                b.addParameter(req).addParameter(response);
            }



            if (rspCls != null) {
                b.addStatement("return new $T()",rspCls);
                b.returns(rspCls);
            }

            builder.addMethod(b.build());
        });

        builder.addField(logger);

        return generator.doGenerate(builder);

    }

    private Class getReqDtoClass(CgiInfo cgiInfo,ClassLoader classLoader){
       return getDtoClass(cgiInfo,dtoConfig.getRequestDtoPackage(),this.dtoConfig.getReqDtoNameSuffix(),classLoader);
    }

    private Class getResDtoClass(CgiInfo cgiInfo,ClassLoader classLoader){
        return getDtoClass(cgiInfo,dtoConfig.getResponseDtoPackage(),this.dtoConfig.getResDtoNameSuffix(),classLoader);
    }

    private Class getDtoClass(CgiInfo cgiInfo,String packageName,String suffixName,ClassLoader classLoader){
        String dtoName = SourceCodeUtil.jsontoUpperCase(cgiInfo.getCgi()) + suffixName;
        String dtoFullName =dtoConfig.getDir()+ File.separator+
                SourceCodeUtil.convertPackage2Dir(packageName)
                        + dtoName + ".java";
        Class cls = ClassLoaderUtil.loadClass(dtoFullName,classLoader);
        return cls;
    }

    public String generateConstantsConfigFile(List<String> urls) {

        TypeSpec.Builder builder = TypeSpec.classBuilder(javaFileConfig.getClassName())
                .addJavadoc(javaFileConfig.getJavaFileDoc())
                .addModifiers(Modifier.PUBLIC);

        for (String cgi : urls) {
            FieldSpec field = FieldSpec.builder(String.class, cgi.toUpperCase(),
                    Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer("\"$N\"", cgi)
                    .build();
            builder.addField(field);
        }

        logger.debug("name :{}", builder.build().name);

        return generator.doGenerate(builder);
    }

    public String generateConstantsConfigFile(CgiConfig cgiConfig) {

        TypeSpec.Builder builder = TypeSpec.classBuilder(javaFileConfig.getClassName())
                .addJavadoc(javaFileConfig.getJavaFileDoc())
                .addModifiers(Modifier.PUBLIC);

       List<CgiInfo> list=cgiConfig.getControllerConfigList().stream().map(
                ControllerConfig::getCgiInfoList).collect(Collectors.toList())
               .stream().flatMap(Collection::stream).collect(Collectors.toList());

        for (CgiInfo item : list) {
            FieldSpec field = FieldSpec.builder(String.class, item.getCgi().toUpperCase(),
                    Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer("\"$N\"", item.getCgi())
                    .build();
            builder.addField(field);
        }

        logger.debug("name :{}", builder.build().name);

        return generator.doGenerate(builder);
    }
}
