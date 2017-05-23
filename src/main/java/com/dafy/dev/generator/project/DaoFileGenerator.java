package com.dafy.dev.generator.project;

import com.dafy.dev.config.DaoConfig;
import com.dafy.dev.config.JavaFileConfig;
import com.dafy.dev.config.MethodInfo;
import com.dafy.dev.config.ParameterInfo;
import com.dafy.dev.generator.common.JavaFileGenerator;
import com.dafy.dev.util.CodeGenUtil;
import com.dafy.dev.util.SourceCodeUtil;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.lang.model.element.Modifier;
import java.util.List;

/**
 * Created by chunxiaoli on 1/5/17.
 */
public class DaoFileGenerator {

    private static final Logger logger = LoggerFactory.getLogger(DaoFileGenerator.class);

    private JavaFileGenerator generator;

    private JavaFileConfig    javaFileConfig;

    private DaoConfig daoConfig;

    public DaoFileGenerator(JavaFileConfig javaFileConfig, DaoConfig daoConfig) {
        this.javaFileConfig = javaFileConfig;
        this.daoConfig = daoConfig;
        this.generator = new JavaFileGenerator(javaFileConfig);
    }

    public String generateDaoImpl() {

        String className = javaFileConfig.getClassName();

        TypeSpec.Builder builder = TypeSpec.classBuilder(className)
                .addJavadoc(javaFileConfig.getJavaFileDoc())
                .addAnnotation(Component.class)
                .addModifiers(Modifier.PUBLIC);

        FieldSpec logger = JavaFileGenerator.getLogger(className);


        builder.addField(logger);
        FieldSpec f= addFields(builder);
        addMethods(builder,f);

        return generator.doGenerate(builder);

    }

    private FieldSpec addFields(TypeSpec.Builder builder){
        FieldSpec.Builder fb=FieldSpec.builder(daoConfig.getMapper(),
                SourceCodeUtil.covertFieldName(SourceCodeUtil.getClassName(daoConfig.getMapper()
                        .getName())),Modifier.PRIVATE);
        fb.addAnnotation(Resource.class);
        builder.addField(fb.build());
        return fb.build();
    }

    private void addMethods(TypeSpec.Builder builder,FieldSpec fieldSpec){
        List<MethodInfo> methods= CodeGenUtil.getMethods(daoConfig.getMapper());

        for (MethodInfo info : methods) {
            MethodSpec.Builder b = MethodSpec.methodBuilder(info.getName())
                    .addModifiers(Modifier.PUBLIC);

            //todo
            /*if(JavaFileGenerator.isPrimaryType(info.getReturnTypeFullClassName())){
                b.returns(JavaFileGenerator.resolvePrimaryType(info.getReturnTypeFullClassName()));
            }else {
                TypeName returnType=JavaFileGenerator.resolveType(info.getReturnTypeFullClassName());
                b.returns(returnType);
            }*/
            //b.addTypeVariable()
            b.returns(info.getGenericReturnType());

            for (ParameterInfo p : info.getParameterInfoList()) {
                TypeName typeName= JavaFileGenerator.resolveType(p.getTypeFullClassName());
                b.addParameter(typeName, p.getName());
            }
            Object[] arr= info.getParameterInfoList().stream().map(ParameterInfo::getName).toArray();

            Object[] args= new Object[arr.length+2];
            args[0]=fieldSpec;
            args[1]=info.getName();

            System.arraycopy(arr, 0, args, 2, arr.length);
            //cb.
            StringBuilder s=new StringBuilder();
            for(int i=0;i<arr.length;i++){
                s.append("$N").append(i==arr.length-1?"":",");
            }
            b.addStatement("return $N.$N("+s.toString()+")",args);
            builder.addMethod(b.build());
        }

    }
}
