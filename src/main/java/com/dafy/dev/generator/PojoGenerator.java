package com.dafy.dev.generator;

import com.dafy.dev.util.SourceCodeUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.codemodel.*;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by chunxiaoli on 10/17/16.
 */
public class PojoGenerator {

    private  final JCodeModel codeModel = new JCodeModel();

    public void generatePojo(){

    }

    /**
     * 生成POJO 文件
     * @param classFullName
     * @param fields
     * @param out
     */
    public void generatePojo(String classFullName,
                             Field[] fields, File out,boolean serializable,boolean addJsonAnnotation){


        JDefinedClass definedClass = null;

        try {
            definedClass = codeModel._class(classFullName);



            //生成序列化
            if(serializable){
                definedClass._implements(Serializable.class);
                JFieldVar field1 = definedClass.field(JMod.PRIVATE | JMod.STATIC | JMod.FINAL, long.class, "serialVersionUID");
                field1.init(JExpr.lit(1L));
            }

            HashMap<String, JFieldVar> map=new LinkedHashMap<>();

            for ( Field var : fields) {
                String fieldName = var.getName();
                Class cls=var.getType();

                JType fieldType;

                if(cls.isPrimitive()){
                    fieldType=codeModel._ref(cls);
                }else {
                    fieldType=codeModel.ref(var.getType());
                }

                JFieldVar fieldVar =definedClass.field(JMod.PRIVATE, fieldType, SourceCodeUtil.convertFieldUppercase(fieldName));


                map.put(fieldName,fieldVar);
                if(addJsonAnnotation){
                    //加注释
                    JAnnotationUse jsonAnnotation = fieldVar.annotate(JsonProperty.class);
                    jsonAnnotation.param("value",SourceCodeUtil.upperCaseToJson(fieldName));
                }

                //生成getter方法
                generateGetter(definedClass,fieldName,fieldType,fieldVar);

                //生成setter方法
                generateSetter(definedClass,fieldName,fieldType);
            }

            //toString方法 todo
            //generateToString(definedClass,classFullName,fields,map);


        } catch (JClassAlreadyExistsException e) {
            e.printStackTrace();
        }



        try {
            codeModel.build(out);

        } catch (Exception e) {
            throw new IllegalStateException("ex", e);
        }
    }

    /*private JClass getJClassFromPrimitive(Class cls){
        codeModel._ref(cls);
    }*/

    private void generateGetter(JDefinedClass definedClass, String fieldName, JType fieldType,JFieldVar fieldVar){


        String getterName=getGetterName(fieldName,fieldType);

        JMethod getterMethod = definedClass.method(JMod.PUBLIC, fieldType, getterName);



        JBlock block = getterMethod.body();
        block._return(fieldVar);
    }

    private void generateSetter(JDefinedClass definedClass, String fieldName, JType fieldType){

        String setterMethodName =getSetterName(fieldName);
        JMethod setterMethod = definedClass.method(JMod.PUBLIC, Void.TYPE, setterMethodName);

        String setterParameter = SourceCodeUtil.convertFieldUppercase(fieldName);

        setterMethod.param(fieldType, setterParameter);
        setterMethod.body().assign(JExpr._this().ref(fieldName), JExpr.ref(setterParameter));
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

    //todo
    private void generateToString(JDefinedClass definedClass,
                                  String className, Field[] fields,
                                  HashMap<String, JFieldVar> map){
        JMethod toStringMethod=definedClass.method(JMod.PUBLIC,String.class,"toString");
        StringBuilder builder=new StringBuilder();
        JBlock body = toStringMethod.body();
        builder.append(className).append("{");
        for (Field field:fields) {
            JVar key= null;
            try {
                key = body.decl(codeModel.parseType("String"),field.getName()+"=");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if(key!=null){
                body.assignPlus(key,map.get(field.getName()));
            }

        }
        builder.append("}");

    }

    private String getGetterName(String fieldName,JType fieldType){
        String prefix="get";
        //todo
        /*String fieldTypeName=fieldType.fullName();
        if (fieldTypeName.equals("boolean") || fieldTypeName.equals("java.lang.Boolean")) {
            prefix = "is";
        }*/
       return prefix+ SourceCodeUtil.convertMethodUppercase(fieldName);
    }

    private String getSetterName(String fieldName){
        return "set"+SourceCodeUtil.convertMethodUppercase(fieldName);
    }




}
