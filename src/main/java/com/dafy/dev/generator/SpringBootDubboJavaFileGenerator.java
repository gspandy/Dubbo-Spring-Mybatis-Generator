package com.dafy.dev.generator;

import com.dafy.dev.codegen.ClassLoaderUtil;
import com.dafy.dev.config.JavaFileConfig;
import com.dafy.dev.config.MethodInfo;
import com.dafy.dev.config.ParameterInfo;
import com.dafy.dev.generator.common.Generator;
import com.dafy.dev.generator.common.JavaFileGenerator;
import com.dafy.dev.pojo.ControllerConfig;
import com.dafy.dev.util.SourceCodeUtil;
import com.dafy.dev.util.launcher.DubboServiceLauncherTemplate;
import com.squareup.javapoet.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.lang.model.element.Modifier;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chunxiaoli on 10/19/16.
 */
public class SpringBootDubboJavaFileGenerator implements Generator {

    private static final Logger logger = LoggerFactory.getLogger(ProjectGenerator.class);

    private JavaFileGenerator generator;

    private JavaFileConfig    javaFileConfig;

    public JavaFileGenerator getGenerator() {
        return generator;
    }

    public void setGenerator(JavaFileGenerator generator) {
        this.generator = generator;
    }

    public SpringBootDubboJavaFileGenerator() {
    }

    public SpringBootDubboJavaFileGenerator(JavaFileConfig config) {
        this.javaFileConfig = config;
        generator = new JavaFileGenerator(config);
    }

    @Override
    public void generate() {

    }

    public void generateSpringLauncherFile() {

        String name = javaFileConfig.getClassName() + "Launcher";

        TypeSpec.Builder builder = TypeSpec.classBuilder(name)
                .addJavadoc(javaFileConfig.getJavaFileDoc())
                .addModifiers(Modifier.PUBLIC);
        builder.addAnnotation(SpringBootApplication.class);

        String pool = "threadPoolTaskScheduler";
        String spring = "springApplication";

        FieldSpec logger = JavaFileGenerator.getLogger(JavaFileGenerator.getClassName(name));

        builder.addField(logger);

        String args = "args";

        TypeSpec thread = TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(ParameterizedTypeName.get(Runnable.class))
                .addMethod(MethodSpec.methodBuilder("run")
                        .addModifiers(Modifier.PUBLIC)
                        .build())
                .build();

        MethodSpec m = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$N.debug(\"$N start:{}\",$N)", logger, name, args)
                .addStatement("$T.out.println(\"$N start.....\")", System.class, name)
                .addStatement("$T $N =new $T()", ThreadPoolTaskScheduler.class, pool,
                        ThreadPoolTaskScheduler.class)
                .addStatement("$N.initialize()", pool)
                .addStatement("$N.setPoolSize(1)", pool)
                .addStatement("$N.scheduleAtFixedRate($L,new $T(),60*1000L)", pool, thread,
                        Date.class)
                .addStatement("$T $N=new $T($N.class)", SpringApplication.class, spring,
                        SpringApplication.class, name)
                .addStatement("$N.addListeners(new $T())", spring, ApplicationPidFileWriter.class)
                .addStatement("$N.run($N)", spring, args)
                .build();
        builder.addMethod(m);
        generator.doGenerate(builder);
    }

    public void generateSpringWebLauncherFile() {

        String name = javaFileConfig.getClassName() + "Launcher";

        TypeSpec.Builder builder = TypeSpec.classBuilder(name)
                .superclass(DubboServiceLauncherTemplate.class)
                .addJavadoc(javaFileConfig.getJavaFileDoc())
                .addModifiers(Modifier.PUBLIC);
        builder.addAnnotation(SpringBootApplication.class);

        FieldSpec logger = JavaFileGenerator.getLogger(JavaFileGenerator.getClassName(name));
        String args = "args";
        MethodSpec m = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, args)
                .addStatement("$N.debug(\"$N start:{}\",$N)", logger, name, args)
                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                .addStatement(name + ".startService(" + name + ".class,args);")
                .build();
        builder.addMethod(m);
        builder.addField(logger);

        generator.doGenerate(builder);
    }

    public void generateMybatisConfigFile(String propertySource, String mapperScan) {

        String name = SourceCodeUtil.getFirstUppercase(javaFileConfig.getClassName()) + "MybatisConfig";

        AnnotationSpec propertySourceAnnotation = AnnotationSpec.builder(PropertySource.class).
                addMember("value", "$S", propertySource).build();

        AnnotationSpec mapperScanAnnotation = AnnotationSpec.builder(MapperScan.class).
                addMember("value", "$S", mapperScan).build();

        TypeSpec.Builder builder = TypeSpec.classBuilder(name)
                .addJavadoc(javaFileConfig.getJavaFileDoc())
                .addModifiers(Modifier.PUBLIC);

        builder.addAnnotation(Configuration.class);
        builder.addAnnotation(propertySourceAnnotation);
        builder.addAnnotation(mapperScanAnnotation);

        generator.doGenerate(builder);
    }



    public void generateDaoInterfaceFile(String pojoFullClassName, String methodName) {
        TypeSpec.Builder builder = TypeSpec.interfaceBuilder(javaFileConfig.getClassName())
                .addJavadoc(javaFileConfig.getJavaFileDoc())
                .addModifiers(Modifier.PUBLIC);

        TypeName entityType = ClassName
                .get(JavaFileGenerator.getPackageName(pojoFullClassName), JavaFileGenerator.getClassName(pojoFullClassName));

        MethodSpec m = MethodSpec.methodBuilder("findById")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(Long.class, "id")
                .returns(entityType)
                .build();
        builder.addMethod(m);
        generator.doGenerate(builder);
    }

    public void generateInterfaceFile() {
        TypeSpec.Builder builder = TypeSpec.interfaceBuilder(javaFileConfig.getClassName())
                .addJavadoc(javaFileConfig.getJavaFileDoc())
                .addModifiers(Modifier.PUBLIC);

        for (MethodInfo info : javaFileConfig.getMethodInfos()) {
            MethodSpec.Builder b = MethodSpec.methodBuilder(info.getName())
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);

            if(JavaFileGenerator.isPrimaryType(info.getReturnTypeFullClassName())){
                b.returns(JavaFileGenerator.resolvePrimaryType(info.getReturnTypeFullClassName()));
            }else {
                TypeName returnType=JavaFileGenerator.resolveType(info.getReturnTypeFullClassName());
                b.returns(returnType);
            }

            for (ParameterInfo p : info.getParameterInfoList()) {
                TypeName type =JavaFileGenerator.resolveType(p.getTypeFullClassName());
                b.addParameter(type, p.getName());

            }
            builder.addMethod(b.build());
        }

        generator.doGenerate(builder);
    }



    public void generateServiceImplFile(String interfaceFullName, String implFullName,
                                        List<String> daoFullClassNames) {

        ClassName interfaceName =JavaFileGenerator.resolveClassName (interfaceFullName);

        String serviceName = SourceCodeUtil.covertFieldName(javaFileConfig.getClassName());

        TypeSpec.Builder builder = TypeSpec.classBuilder(javaFileConfig.getClassName())
                .addAnnotation(
                        AnnotationSpec.builder(Service.class).addMember("value", "$S", serviceName)
                                .build())
                .addSuperinterface(interfaceName)
                .addJavadoc(javaFileConfig.getJavaFileDoc())
                .addModifiers(Modifier.PUBLIC);

        for (String daoFullClassName : daoFullClassNames) {
            ClassName dao = JavaFileGenerator.resolveClassName(daoFullClassName);
            String daoFieldName = SourceCodeUtil.covertFieldName(JavaFileGenerator.getClassName(daoFullClassName));
            FieldSpec field = FieldSpec.builder(dao, daoFieldName, Modifier.PRIVATE)
                    .addAnnotation(Resource.class)
                    .build();
            builder.addField(field);
        }

        FieldSpec logger = JavaFileGenerator.getLogger(JavaFileGenerator.getClassName(implFullName));

        builder.addField(logger);

        generator.doGenerate(builder);
    }

    public void generateTestCaseFile(String baseTestFullName, String serviceFullClassName,Class serviceCls) {

        TypeName serviceType = JavaFileGenerator.resolveType(serviceFullClassName);

        String serviceField = SourceCodeUtil.covertFieldName(JavaFileGenerator.getClassName(serviceFullClassName));

        String testClassName =
                SourceCodeUtil.covertClassName(JavaFileGenerator.getClassName(serviceFullClassName)) + "Test";

        TypeName baseTest = JavaFileGenerator.resolveType(baseTestFullName);

        TypeSpec.Builder builder = TypeSpec.classBuilder(testClassName)
                .superclass(baseTest)
                .addJavadoc(javaFileConfig.getJavaFileDoc())
                .addModifiers(Modifier.PUBLIC);

        //Class serviceCls = CompilerUtil.loadFromFile(serviceJavaPath);

        if (serviceCls == null) {
            logger.error("serviceCls is null ");
            return;
        }

        FieldSpec field = FieldSpec.builder(serviceCls, serviceField, Modifier.PRIVATE)
                .addAnnotation(Resource.class)
                .build();

        FieldSpec logger = JavaFileGenerator.getLogger(JavaFileGenerator.getClassName(baseTestFullName));

        builder.addField(logger);
        //Service 没法正确自动import进来 这里先注释掉
        builder.addField(field);

        //test method

        MethodSpec m = MethodSpec.methodBuilder(serviceField)
                .addModifiers(Modifier.PUBLIC)
                //.addStatement("$N.debug(\"$N start:{}\",$N)", logger, serviceField, field)
                //.addStatement("$T.assertNotNull($N)", Assert.class, field)
                .addAnnotation(Test.class)
                .build();

        builder.addMethod(m);

        generator.doGenerate(builder);
    }

    //生成provider BaseTest文件
    public void generateBaseTestCaseFile(String baseTestFullName, String hookClassFullName) {

        String clsName = JavaFileGenerator.getClassName(baseTestFullName);

        ClassName hookCls = JavaFileGenerator.resolveClassName(hookClassFullName);

        TypeSpec.Builder builder = TypeSpec.classBuilder(clsName)
                .addJavadoc(javaFileConfig.getJavaFileDoc())
                .addAnnotation(AnnotationSpec.builder(RunWith.class)
                        .addMember("value", "$T$N", SpringJUnit4ClassRunner.class,
                                ".class").build())
                .addAnnotation(AnnotationSpec.builder(SpringApplicationConfiguration.class)
                        .addMember("classes", "$T$N",
                                hookCls, ".class").build())
                .addAnnotation(Transactional.class)
                .addAnnotation(AnnotationSpec.builder(TransactionConfiguration.class)
                        .addMember("defaultRollback",
                                "$L", true).build())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);

        FieldSpec logger = JavaFileGenerator.getLogger(JavaFileGenerator.getClassName(clsName));
        builder.addField(logger);

        generator.doGenerate(builder);
    }

    public void generateDaoImpl(HashMap<String, String> fields, String returnType,
                                String interfaceName, String packageName) {
        generator.generateDaoImpl(fields, returnType, interfaceName, packageName);

    }

    public String generateControllerFile(List<String> cgiList, String constantsClass) {

        String className = javaFileConfig.getClassName();
        TypeSpec.Builder builder = TypeSpec.classBuilder(className)
                .addAnnotation(Controller.class)
                .addJavadoc(javaFileConfig.getJavaFileDoc())
                .addModifiers(Modifier.PUBLIC);

        FieldSpec logger = JavaFileGenerator.getLogger(className);

        //TypeName typeName=TypeName.get(cgiConstantsClass.name)

        Class cls = ClassLoaderUtil.loadFromFile(constantsClass);

        cgiList.forEach(item -> {

            ParameterSpec req = ParameterSpec.builder(HttpServletRequest.class, "request").build();

            ParameterSpec response = ParameterSpec.builder(HttpServletResponse.class, "response")
                    .build();

            AnnotationSpec mapping = AnnotationSpec.builder(RequestMapping.class).addMember("value",
                    "$T.$N", cls, item.toUpperCase())
                    .build();

            AnnotationSpec resp = AnnotationSpec.builder(ResponseBody.class)
                    .build();

            MethodSpec m = MethodSpec.methodBuilder(SourceCodeUtil.convertMethodUppercase(item))
                    .addAnnotation(mapping)
                    .addAnnotation(resp)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(req)
                    .addParameter(response)
                    //.addStatement("$N.debug(\"$N start:{}\",$N)", logger, serviceField, field)
                    //.addStatement("$T.assertNotNull($N)", Assert.class, field)
                    .build();

            builder.addMethod(m);
        });

        builder.addField(logger);

        return generator.doGenerate(builder);

    }

    public String generateControllerFile(ControllerConfig cgiConfig, String constantsClass) {

        String className = javaFileConfig.getClassName();
        TypeSpec.Builder builder = TypeSpec.classBuilder(className)
                .addAnnotation(Controller.class)
                .addJavadoc(javaFileConfig.getJavaFileDoc())
                .addModifiers(Modifier.PUBLIC);

        FieldSpec logger = JavaFileGenerator.getLogger(className);

        //TypeName typeName=TypeName.get(cgiConstantsClass.name)

        Class cls = ClassLoaderUtil.loadFromFile(constantsClass);

        cgiConfig.getCgiInfoList().forEach(item -> {
            String cgi = item.getCgi();

            ParameterSpec req = ParameterSpec.builder(HttpServletRequest.class, "request").build();

            ParameterSpec response = ParameterSpec.builder(HttpServletResponse.class, "response")
                    .build();

            AnnotationSpec mapping = AnnotationSpec.builder(RequestMapping.class).addMember("value",
                    "$T.$N", cls, cgi.toUpperCase())
                    .build();

            AnnotationSpec resp = AnnotationSpec.builder(ResponseBody.class)
                    .build();

            MethodSpec m = MethodSpec.methodBuilder(SourceCodeUtil.convertMethodUppercase(cgi))
                    .addAnnotation(mapping)
                    .addAnnotation(resp)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(req)
                    .addParameter(response)
                    //.addStatement("$N.debug(\"$N start:{}\",$N)", logger, serviceField, field)
                    //.addStatement("$T.assertNotNull($N)", Assert.class, field)
                    .build();

            builder.addMethod(m);
        });

        builder.addField(logger);

        return generator.doGenerate(builder);

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
}
