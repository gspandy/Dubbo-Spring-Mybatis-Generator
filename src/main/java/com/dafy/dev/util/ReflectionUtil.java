package com.dafy.dev.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by chunxiaoli on 5/25/17.
 */
public class ReflectionUtil {
    public static List<Field> getFields(Class cls, boolean getSuperField) {
        Field[] fields = cls.getDeclaredFields();
        List<Field> list = new ArrayList<>();
        list.addAll(Arrays.asList(fields));
        if (getSuperField) {
            Class superCls = cls.getSuperclass();
            while (!superCls .getSimpleName().equals("Object")) {
                list.addAll(Arrays.asList(superCls.getDeclaredFields()));
                superCls=superCls.getSuperclass();
            }
        }
        return list;
    }

    public static Object getFieldValue(Object obj,String fieldName) {
        Object ret = null;
        try {
            Method getter=obj.getClass().getMethod(SourceCodeUtil.getGetterName(fieldName));
            if(getter!=null){
                 ret = getter.invoke(obj);
            }else {
                ret=obj.getClass().getField(fieldName).get(obj);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ret;
    }
}
