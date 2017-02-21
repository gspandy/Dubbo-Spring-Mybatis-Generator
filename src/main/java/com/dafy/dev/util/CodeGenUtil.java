package com.dafy.dev.util;

import com.dafy.dev.config.MethodInfo;
import com.dafy.dev.config.ParameterInfo;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chunxiaoli on 1/5/17.
 */
public class CodeGenUtil {
    public static List<MethodInfo> getMethods(Class cls){
        if(cls!=null){
            Method[]methods=cls.getDeclaredMethods();
            if(methods!=null&&methods.length>0){

                List<MethodInfo> list=new ArrayList<>();
                for(Method m:methods){
                    Type retType= m.getGenericReturnType();




                    Class returnType=m.getReturnType();


                    Class[] types= m.getParameterTypes();
                    Parameter[] parameters= m.getParameters();
                    List<ParameterInfo> parameterInfos=new ArrayList<>();
                    int i=0;
                    for (Parameter p :parameters){
                        ParameterInfo parameterInfo=new ParameterInfo();
                        //todo change name
                        //parameterInfo.setName(SourceCodeUtil.covertFieldName(SourceCodeUtil.getClassName(types[i].getName())));
                        parameterInfo.setType(types[i]);
                        parameterInfo.setName(p.getName());
                        parameterInfo.setTypeFullClassName(types[i].getName());
                        parameterInfos.add(parameterInfo);
                        i++;
                    }
                    MethodInfo info=new MethodInfo();
                    info.setReturnType(m.getReturnType());
                    info.setParameterInfoList(parameterInfos);
                    info.setName(m.getName());
                    info.setReturnTypeFullClassName(returnType.getName());
                    list.add(info);
                }
                return list;
            }

        }
        return null;
    }

    public static List<MethodInfo>  getParameterList(Class cls){
        if(cls!=null){
            Method[]methods=cls.getDeclaredMethods();
            if(methods!=null&&methods.length>0){

                List<MethodInfo> list=new ArrayList<>();
                for(Method m:methods){
                    Class returnType=m.getReturnType();
                    Class[] types= m.getParameterTypes();
                    Parameter[] parameters= m.getParameters();
                    List<ParameterInfo> parameterInfos=new ArrayList<>();
                    int i=0;
                    for (Parameter p :parameters){
                        ParameterInfo parameterInfo=new ParameterInfo();
                        //todo change name
                        //parameterInfo.setName(SourceCodeUtil.covertFieldName(SourceCodeUtil.getClassName(types[i].getName())));
                        parameterInfo.setName(p.getName());
                        parameterInfo.setTypeFullClassName(types[i].getName());
                        parameterInfos.add(parameterInfo);
                        i++;
                    }
                    MethodInfo info=new MethodInfo();
                    info.setParameterInfoList(parameterInfos);
                    info.setName(m.getName());
                    info.setReturnTypeFullClassName(returnType.getName());
                    list.add(info);
                }
                return list;
            }

        }
        return null;
    }




}
