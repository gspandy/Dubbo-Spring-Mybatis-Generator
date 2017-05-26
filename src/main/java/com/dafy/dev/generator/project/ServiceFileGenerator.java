package com.dafy.dev.generator.project;

import com.dafy.dev.codegen.ClassLoaderUtil;
import com.dafy.dev.config.DtoUtilConfig;
import com.dafy.dev.config.JavaFileConfig;
import com.dafy.dev.config.MethodInfo;
import com.dafy.dev.config.ParameterInfo;
import com.dafy.dev.config.ServiceConfig;
import com.dafy.dev.generator.common.JavaFileGenerator;
import com.dafy.dev.util.CodeGenUtil;
import com.dafy.dev.util.SourceCodeUtil;
import com.dafy.dev.util.StringUtil;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.lang.model.element.Modifier;
import java.util.List;

/**
 * Created by chunxiaoli on 1/5/17.
 */
public class ServiceFileGenerator {

    private static final Logger logger = LoggerFactory.getLogger(ServiceFileGenerator.class);

    private JavaFileGenerator generator;

    private JavaFileConfig javaFileConfig;

    private ServiceConfig serviceConfig;

    public ServiceFileGenerator(JavaFileConfig javaFileConfig, ServiceConfig serviceConfig) {
        this.javaFileConfig = javaFileConfig;
        this.serviceConfig = serviceConfig;
        this.generator = new JavaFileGenerator(javaFileConfig);
    }

    private FieldSpec getField(Class daoCls, Class pojoCls) {
        FieldSpec.Builder fb = FieldSpec.builder(daoCls,
                SourceCodeUtil.covertFieldName(SourceCodeUtil.getClassName(daoCls.getName())),
                Modifier.PRIVATE);
        fb.addAnnotation(Resource.class);
        return fb.build();
    }

    private void addMethod(TypeSpec.Builder builder, FieldSpec fieldSpec, Class daoCls,
                           Class pojoCls, boolean isImpl,ClassLoader classLoader,String utilPackge) {
        List<MethodInfo> methods = CodeGenUtil.getMethods(daoCls);
        String pojoName = null;

        if (isImpl && pojoCls != null) {
            pojoName = SourceCodeUtil.covertFieldName(pojoCls.getSimpleName());
        }

        for (MethodInfo info : methods) {
            MethodSpec.Builder b = MethodSpec
                    .methodBuilder(resolveServiceMethodNameForDao(daoCls, info.getName()))
                    .addModifiers(Modifier.PUBLIC);
            if (!isImpl) {
                b.addModifiers(Modifier.ABSTRACT);
            }

            TypeName returnType = null;

            if (JavaFileGenerator.isPrimaryType(info.getReturnTypeFullClassName())) {
                returnType = JavaFileGenerator.getPrimaryType(info.getReturnTypeFullClassName());
            } else {
                returnType = resolveDtoTypeFromClass(info.getReturnTypeFullClassName());
            }

            b.returns(returnType);
            //b.returns(info.getGenericReturnType());

            for (ParameterInfo p : info.getParameterInfoList()) {
                TypeName typeName = resolveDtoTypeFromClass(p.getTypeFullClassName());
                b.addParameter(typeName, p.getName());
            }

            if (isImpl) {

                //build dao method call code body
                Object[] daoMethodParams = info.getParameterInfoList().stream()
                        .map(ParameterInfo::getName)
                        .toArray();

                //concat with args to construct method body
                //todo
                if(info.getName().toUpperCase().contains("INSERT")||info.getName().toUpperCase().contains("UPDATE")){

                    Object[] daoArgs = new Object[daoMethodParams.length + 2];
                    daoArgs[0] = fieldSpec;
                    daoArgs[1] = info.getName();

                    System.arraycopy(daoMethodParams, 0, daoArgs, 2, daoMethodParams.length);

                    StringBuilder s = new StringBuilder();
                    for (int i = 0; i < daoMethodParams.length; i++) {
                        s.append("$N").append(i == daoMethodParams.length - 1 ? "" : ",");
                    }

                    Class dtoType = resolveDtoClass(info.getReturnTypeFullClassName(),classLoader);

                    if(pojoCls!=null) {

                        String dtoUtilClassFullName = (StringUtil.isEmpty(utilPackge)?"":utilPackge) + "." + pojoCls.getSimpleName()+"DtoUtil";

                        Class utilCls = ClassLoaderUtil
                                .load(dtoUtilClassFullName, classLoader);

                        if(utilCls!=null){
                            String pojoObj="pojo";
                            //todo many params handle
                            b.addStatement("$T $N=$T.convertToPojo($N)", pojoCls,pojoObj, utilCls, daoArgs[2]);

                            daoArgs[2]=pojoObj;
                        }else {
                            b.addStatement("//todo");
                        }
                    }

                    b.addStatement("return $N.$N(" + s.toString() + ")", daoArgs);

                }else if(info.getName().toUpperCase().contains("SELECT")){
                    Object[] daoArgs = new Object[daoMethodParams.length + 4];
                    daoArgs[0] = pojoCls;
                    daoArgs[1] = pojoName;
                    daoArgs[2] = fieldSpec;
                    daoArgs[3] = info.getName();

                    System.arraycopy(daoMethodParams, 0, daoArgs, 4, daoMethodParams.length);

                    StringBuilder s = new StringBuilder();
                    for (int i = 0; i < daoMethodParams.length; i++) {
                        s.append("$N").append(i == daoMethodParams.length - 1 ? "" : ",");
                    }

                    String dto = "dto";

                    b.addStatement("$T $N=new $T()", returnType, dto, returnType);

                    b.addStatement("$T $N=$N.$N(" + s.toString() + ")", daoArgs);

                    //Class dtoType = info.getReturnType();
                    Class dtoType = resolveDtoClass(info.getReturnTypeFullClassName(),classLoader);

                    String dtoUtilClassFullName = (StringUtil.isEmpty(utilPackge)?"":utilPackge) + "."+dtoType.getSimpleName()+"Util";


                    Class utilCls = ClassLoaderUtil.load(dtoUtilClassFullName, classLoader);
                    if(utilCls!=null){
                        b.addStatement("$N=$T.convertToDto($N)", dto, utilCls, pojoName);
                    }else {
                        b.addStatement("//todo ");
                    }


                    b.addStatement("return $N", dto);
                }

            }
            builder.addMethod(b.build());
        }

    }

    private void createUtilFiles() {
        DtoUtilConfig config = new DtoUtilConfig();
        //new DtoUtilFileGenerator(config).generateDtoUtilFile();
    }

    private String resolveServiceMethodNameForDao(Class daoCls, String originMethodName) {
        //add prefix for every dao obj
        String dao = daoCls.getSimpleName();
        if (dao.endsWith("Dao")) {
            dao = dao.replace("Dao", "");
        }
        String name = SourceCodeUtil.jsontoUpperCase(dao + "_" + originMethodName);
        return SourceCodeUtil.covertFieldName(name);
    }

    private TypeName resolveDtoTypeFromClass(String fullName) {
        String type = "";
        String pojoName = JavaFileGenerator.getClassName(fullName);
        if (JavaFileGenerator.isPrimaryType(pojoName) ||
                JavaFileGenerator.isPrimaryWrapperType(pojoName)) {
            type = fullName;
        } else {
            type = this.serviceConfig.getDtoPackageName() + "." + pojoName + "Dto";
        }
        return JavaFileGenerator.resolveType(type);
    }

    private Class resolveDtoClass(String fullName,ClassLoader classLoader) {
        String type = "";
        String pojoName = JavaFileGenerator.getClassName(fullName);
        if (JavaFileGenerator.isPrimaryType(pojoName) ||
                JavaFileGenerator.isPrimaryWrapperType(pojoName)) {
            return JavaFileGenerator.getPrimaryClass(fullName);
        } else {
            type = this.serviceConfig.getDtoPackageName() + "." + pojoName + "Dto";
            return ClassLoaderUtil.load(type,classLoader);
        }
    }

    private TypeName resolvePojoTypeFromClass(String fullName) {
        String type = "";
        String pojoName = JavaFileGenerator.getClassName(fullName);
        if (JavaFileGenerator.isPrimaryType(pojoName) ||
                JavaFileGenerator.isPrimaryWrapperType(pojoName)) {
            type = fullName;
        } else {
            type = this.serviceConfig.getPojoPackageName() + "." + pojoName;
        }
        return JavaFileGenerator.resolveType(type);
    }

    //create service interface
    public void generateServiceInterfaceFile(ClassLoader classLoader,String utilPackage) {
        String className = javaFileConfig.getClassName();
        TypeSpec.Builder builder = TypeSpec.interfaceBuilder(className)
                .addJavadoc(javaFileConfig.getJavaFileDoc())
                .addModifiers(Modifier.PUBLIC);
        //FieldSpec logger = JavaFileGenerator.getLogger(className);
        //builder.addField(logger);
        for (ServiceConfig.ServiceDaoInfo info : serviceConfig.getDaoInfos()) {
            Class daoCls = info.getDao();
            FieldSpec field = getField(daoCls, null);
            addMethod(builder, field, daoCls, null, false,classLoader,utilPackage);
        }
        generator.doGenerate(builder);
    }

    public void generateServiceImplFile(String interfaceFullName,ClassLoader classLoader,String utilPackage) {

        ClassName interfaceName = JavaFileGenerator.resolveClassName(interfaceFullName);

        String className = javaFileConfig.getClassName();

        TypeSpec.Builder builder = TypeSpec.classBuilder(className)
                .addSuperinterface(interfaceName)
                .addAnnotation(Service.class)
                .addJavadoc(javaFileConfig.getJavaFileDoc())
                .addModifiers(Modifier.PUBLIC);

        FieldSpec logger = JavaFileGenerator.getLogger(className);
        builder.addField(logger);
        for (ServiceConfig.ServiceDaoInfo info : serviceConfig.getDaoInfos()) {
            Class daoCls = info.getDao();
            Class pojoCls = info.getPojo();
            FieldSpec field = getField(daoCls, pojoCls);
            builder.addField(field);
            addMethod(builder, field, daoCls, pojoCls, true,classLoader,utilPackage);
        }
        generator.doGenerate(builder);
    }
}
