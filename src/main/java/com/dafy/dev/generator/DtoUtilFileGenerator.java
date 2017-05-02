package com.dafy.dev.generator;

import com.dafy.dev.annotation.DtoField;
import com.dafy.dev.annotation.DtoFieldIgnore;
import com.dafy.dev.config.DtoUtilConfig;
import com.dafy.dev.config.ServiceConfig;
import com.dafy.dev.util.SourceCodeUtil;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by chunxiaoli on 10/20/16.
 */
public class DtoUtilFileGenerator implements Generator{


    private DtoUtilConfig dtoConfig;

    public DtoUtilFileGenerator(DtoUtilConfig config){
        this.dtoConfig=config;
    }

    @Override
    public void generate() {
    }



    public void generateDtoUtilFile(Class pojo, Class dto,String pojoObj) {
        String fileName=pojo.getSimpleName()+"To"+dto.getSimpleName()+"Util";
        generateDtoUtilFile(pojo,dto,fileName,"convert",pojoObj,true);
    }

    public void generateDtoUtilFiles(List<ServiceConfig.ServiceDaoInfo> serviceDaoInfos) {
        serviceDaoInfos.forEach(item->{
            generateDtoUtilFile(item.getPojo(),item.getDao());
        });
    }

    public void generateDtoUtilFile(Class pojo, Class dto) {

        String fileName=dto.getSimpleName()+"Util";

        generateDtoUtilFile(pojo,dto,fileName,"convert","obj",true);
    }

    public void generateDtoUtilFile(Class pojo, Class dto,String fileName,String pojoObj) {

        generateDtoUtilFile(pojo,dto,fileName,"convert",pojoObj,true);
    }

    public void generateDtoUtilFile(Class pojo, Class dto,String fileName,String methodName,
                                    String pojoObj,boolean convertAllField) {

        TypeSpec.Builder builder = TypeSpec.classBuilder(fileName)
                .addModifiers(Modifier.PUBLIC);

        FieldSpec logger = getLogger(fileName);

        builder.addField(logger);


        builder.addMethod(convertToDto(pojo,dto,pojoObj,convertAllField,logger).build());
        builder.addMethod(convertToPojo(dto,pojo,pojoObj,convertAllField,logger).build());

       doGenerate(builder);
    }

    private MethodSpec.Builder convertToDto(Class source,Class target,String pojoObj,
                                         boolean convertAllField,FieldSpec logger){
        return getMethod("convertToDto",source,target,pojoObj,convertAllField,logger);
    }
    private MethodSpec.Builder convertToPojo(Class source,Class target,String pojoObj,
                                            boolean convertAllField,FieldSpec logger){
        return getMethod("convertToPojo",source,target,pojoObj,convertAllField,logger);
    }

    private MethodSpec.Builder getMethod(String methodName,Class source,Class target,String pojoObj,
                           boolean convertAllField,FieldSpec logger){
        MethodSpec.Builder mBuilder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC,Modifier.STATIC);
        String retObj = "ret";
        mBuilder.addParameter(source,pojoObj);
        mBuilder.addStatement("$N.debug(\"pojo:{}\",$N)", logger, pojoObj);
        mBuilder.addStatement("$T $N=new $T()", target, retObj, target);
        Field [] fields= source.getDeclaredFields();
        for (Field f : fields) {
            boolean ignore=f.getAnnotation(DtoFieldIgnore.class)!=null;
            if (!(f.getName().toUpperCase().equals("serialVersionUID".toUpperCase()))
                    &&!ignore&&convertAllField||f.getAnnotation(DtoField.class) != null) {
                String fieldName = SourceCodeUtil.uppercase(f.getName(),false);
                mBuilder.addStatement("$N.set$N($N.get$N())", retObj, fieldName, pojoObj,
                        fieldName);
            }
        }
        mBuilder.addStatement("return $N",retObj);
        mBuilder.returns(target);

        return mBuilder;

    }

    public   void doGenerate(TypeSpec.Builder builder){
        JavaFile javaFile = JavaFile.builder(dtoConfig.getPackageName(),builder.build()).build();
        try {
            javaFile.writeTo(System.out);
            File dir = new File(dtoConfig.getOutDir());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            javaFile.writeTo(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private FieldSpec getLogger(String clsName) {
        return FieldSpec
                .builder(Logger.class, "logger", Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
                .initializer("$T.$N($N)", LoggerFactory.class, "getLogger", clsName + ".class")
                .build();
    }



}
