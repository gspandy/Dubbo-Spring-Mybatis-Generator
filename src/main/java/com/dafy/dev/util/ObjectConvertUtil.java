package com.dafy.dev.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by WangLiang on 2016/11/8.
 */
public class ObjectConvertUtil {



    /**
     * 对象转换，字段名一致而且返回值一致的情况下才能转，其他情况不转，需手动设置
     * @param sourceObj
     * @param destClass
     * @return
     * @throws Exception
     */
    public static Object convert( Object sourceObj , Class destClass) throws Exception{
        return convert(sourceObj,destClass,false);
    }

    public static Object convertObject( Object sourceObj , Class destClass){
        try {
            return convert(sourceObj,destClass,false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对象转换，字段名一致而且返回值一致的情况下才能转，其他情况不转，需手动设置
     * @param sourceObj
     * @param destClass
     * @param iterateSuperClass 是否遍历继承的属性
     * @return
     * @throws Exception
     */
    public static Object convert( Object sourceObj , Class destClass,boolean iterateSuperClass) throws Exception{
        Class sourceClass = sourceObj.getClass();
        Field[] fields = destClass.getDeclaredFields();

        List<Field> fieldList=new ArrayList<>();

        fieldList.addAll(Arrays.asList(fields));

        if(iterateSuperClass){
            Class cls=destClass.getSuperclass();
            while (cls!=null){
                fieldList.addAll(Arrays.asList(cls.getDeclaredFields()));
                cls=cls.getSuperclass();
            }
        }

        Object destObj = destClass.newInstance();
        for( Field field : fieldList ){
            String fieldName = field.getName();
            if( "serialVersionUID".equals(fieldName) ){
                //LogicUtil.doNothing();
            } else {
                String getMethodStr = componentGetter(fieldName);
                Method getMethod = null;
                try {
                    getMethod = sourceClass.getMethod(getMethodStr);
                }catch (Exception e){//如果找不到这个方法就不转化
                    continue;
                }
                Object sourceValue = getMethod.invoke(sourceObj);
                Class returnTypeClass = getMethod.getReturnType();
                //得到方法的返回类型
                String returnType = returnTypeClass.getTypeName();

                field.setAccessible(true);

                if ( isSameType(returnType,field.getType().getName()) ) {
                    field.set(destObj, sourceValue);
                }
            }
        }

        return destObj;
    }
    public static String componentGetter(String type){
        String typeFirstLetter = type.substring(0,1);
        StringBuilder sb = new StringBuilder("get");
        sb.append( typeFirstLetter.toUpperCase() )
                .append( type.substring(1) );
        return sb.toString();
    }
    private static boolean isSameType(String typeA, String typeB){
        if( typeA.equals(typeB) ){
            return true;
        }
        if( isInt(typeA) && isInt(typeB) ){
            return true;
        }
        if( isLong( typeA ) && isLong( typeB )){
            return true;
        }
        if( isByte( typeA ) && isByte( typeB ) ){
            return true;
        }
        return false;
    }

    private static boolean isInt(String type){
        return "int".equals( type ) || "java.lang.Integer".equals( type );
    }
    private static boolean isLong(String type){
        return "long".equals( type ) || "java.lang.Long".equals( type );
    }
    private static boolean isByte(String type){
        return "byte".equals( type ) || "java.lang.Byte".equals( type );
    }


    public  static <T>  T merge(T source, T target){

        if(source!=null&&target!=null){
            Field[] oldFields= source.getClass().getDeclaredFields();

            Field[] newFields= target.getClass().getDeclaredFields();

            //生成新对象
            //Object ret=cloneObject(target);
            T ret=target;

            for (Field field:oldFields){
                boolean needUpdateValue=false;
                try{
                    String fieldName=field.getName();
                    String getMethodStr = componentGetter(fieldName);
                    Method getMethod=null;
                    try {
                        getMethod=ret.getClass().getMethod(getMethodStr);
                    }catch (Exception e){//新对象里找不到该属性 则需要把原始值更新到新对象
                        needUpdateValue=true;
                    }

                    //新对象的值是空的 同样也要把原始的非空值更新到该新对象
                    if(getMethod!=null){
                        Object targetValue = getMethod.invoke(ret);

                        if(!isEmpty(targetValue) && !(targetValue instanceof String )&&!(targetValue instanceof Number)){
                            Object origin=getMethod.invoke(source);

                            ObjectConvertUtil.merge(origin,targetValue);
                            //updateValue(targetValue,subProperty,field);
                        }

                        if(isEmpty(targetValue)){
                            needUpdateValue=true;
                        }
                    }

                    if(needUpdateValue){
                        updateValue(source,ret,field);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return ret;
        }
        return target;

    }

    private static Object cloneObject(Object target){
        Object ret=null;
        try {
            ret =target.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        if(ret==null){
            return null;
        }

        for (Field field:ret.getClass().getDeclaredFields()){
            String fieldName=field.getName();
            String getMethodStr = componentGetter(fieldName);
            Method getMethod=null;
            try {
                getMethod=target.getClass().getMethod(getMethodStr);
                Object targetValue = getMethod.invoke(target);
                field.setAccessible(true);
                field.set(ret, targetValue);
            }catch (Exception e){//新对象里找不到该属性 则需要把原始值更新到新对象
                continue;
            }
        }
        return ret;
    }

    private static void   updateValue(Object source,Object target,Field field){
        try{
            String fieldName=field.getName();
            String getMethodStr = componentGetter(fieldName);
            Method getMethod = null;
            try {
                getMethod = source.getClass().getMethod(getMethodStr);
            }catch (Exception e){
                e.printStackTrace();
            }
            if(getMethod!=null){
                Object sourceValue = getMethod.invoke(source);
                //非空的则复制
                if(!isEmpty(sourceValue)){
                    Class returnTypeClass = getMethod.getReturnType();
                    String returnType = returnTypeClass.getTypeName();
                    field.setAccessible(true);
                    if (isSameType(returnType,field.getType().getName()) ) {
                        field.set(target, sourceValue);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public static boolean isEmpty(Object object){
        if(object==null){
            return true;
        }
        if(object instanceof Integer){
            return ((Integer)object)==0;
        }else if(object instanceof Float){
            return ((Float)object)==0;
        }else if(object instanceof Double){
            return ((Float)object)==0;
        }else if(object instanceof String){
            return ((String)object).equals("");
        }
        return false;
    }

}
