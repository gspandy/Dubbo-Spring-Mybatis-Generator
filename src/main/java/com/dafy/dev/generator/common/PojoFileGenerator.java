package com.dafy.dev.generator.common;

import com.dafy.dev.config.PojoConfig;
import com.dafy.dev.util.SerialVersionUIDUtil;
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
import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Created by chunxiaoli on 10/17/16.
 */
public class PojoFileGenerator {

    private PojoConfig pojoConfig;

    public PojoFileGenerator(PojoConfig pojoConfig) {
        this.pojoConfig = pojoConfig;
    }

    public void generate(Class sourceCls, String targetClassFullName) {
        Field[] fields = sourceCls.getDeclaredFields();
        String className = SourceCodeUtil.getClassName(targetClassFullName);
        TypeSpec.Builder builder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC);

        FieldSpec logger = getLogger(className);

        //builder.addField(logger);

        if (pojoConfig.isSerializable()) {

            builder.addSuperinterface(Serializable.class);

            FieldSpec version = FieldSpec
                    .builder(Logger.class, "serialVersionUID", Modifier.PRIVATE, Modifier.FINAL,
                            Modifier.STATIC)
                    .initializer("$S",SerialVersionUIDUtil.generate())
                    .build();
            builder.addField(version);
        }

        for (Field f : fields) {
            builder.addField(getField(f));
        }

        for (Field f : fields) {
            builder.addMethod(getSetterMethod(f).build());
            builder.addMethod(getGetterMethod(f).build());
        }

        if(pojoConfig.isToString()){
            builder.addMethod(getToStringMethod(sourceCls,className).build());
        }
        doGenerate(builder);

    }

    private FieldSpec getField(Field field) {
        return FieldSpec
                .builder(field.getType(), field.getName(), Modifier.PRIVATE)
                .build();
    }
    /*
      @Override
   public String toString() {
       return "User{" +
               "userAge=" + userAge +
               ", status=" + status +
               ", userName='" + userName + '\'' +
               ", createTime=" + createTime +
               ", updateStatusTime=" + updateStatusTime +
               ", yes=" + yes +
               '}';
   }
    */
    private MethodSpec.Builder getToStringMethod(Class cls,String className) {
        String name = "toString";
        MethodSpec.Builder mBuilder = MethodSpec.methodBuilder(name).addModifiers(Modifier.PUBLIC);
        mBuilder.returns(String.class);
        Field[] fields = cls.getDeclaredFields();
        StringBuffer sb = new StringBuffer();
        sb.append("return \"").append(className).append("{\"+\n");

        Object[] args = new Object[fields.length];
        int i=0;
        for (Field f : fields) {
            sb.append(i==0?"\"":"\", ").append(f.getName()).append("=\" + $N ").append(" + \n");
            args[i++]=f.getName();
        }
        sb.append("\"}\"");

        mBuilder.addStatement(sb.toString(),args);

        return mBuilder;

    }

    private MethodSpec.Builder getSetterMethod(Field field) {
        String fieldName = field.getName();
        MethodSpec.Builder mBuilder = MethodSpec
                .methodBuilder(SourceCodeUtil.getSetterName(fieldName))
                .addModifiers(Modifier.PUBLIC);
        mBuilder.addParameter(field.getType(), fieldName);
        mBuilder.addStatement("this.$N=$N", fieldName, fieldName);
        return mBuilder;

    }

    private MethodSpec.Builder getGetterMethod(Field field) {
        String fieldName = field.getName();
        MethodSpec.Builder mBuilder = MethodSpec
                .methodBuilder(SourceCodeUtil.getGetterName(fieldName))
                .addModifiers(Modifier.PUBLIC).returns(field.getType());
        mBuilder.addStatement("return this.$N", fieldName);
        return mBuilder;

    }

    public void doGenerate(TypeSpec.Builder builder) {
        JavaFile javaFile = JavaFile.builder(pojoConfig.getPackageName(), builder.build()).build();
        try {
            javaFile.writeTo(System.out);
            File dir = new File(pojoConfig.getOutDir());
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
