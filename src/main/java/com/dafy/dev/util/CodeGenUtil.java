package com.dafy.dev.util;

import com.dafy.dev.codegen.ClassLoaderUtil;
import com.dafy.dev.config.MethodInfo;
import com.dafy.dev.config.ParameterInfo;
import com.dafy.dev.generator.provider.ProviderModuleGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chunxiaoli on 1/5/17.
 */
public class CodeGenUtil {
    private final static Logger logger = LoggerFactory.getLogger(ProviderModuleGenerator.class);
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
                        parameterInfo.setParameterizedType(p.getParameterizedType());
                        parameterInfos.add(parameterInfo);
                        i++;
                    }
                    MethodInfo info=new MethodInfo();
                    info.setReturnType(m.getReturnType());
                    info.setParameterInfoList(parameterInfos);
                    info.setName(m.getName());
                    info.setReturnTypeFullClassName(returnType.getName());
                    info.setGenericReturnType(retType);
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

    public static List<Class> loadClassFromDir(String dir,ClassLoader classLoader) {
        List<Class> classes = new ArrayList<>();
        File files[] = new File(dir).listFiles();
        if (files != null && files.length > 0) {
            for (File f : files) {
                if (FileUtil.isJavaFile(f)) {
                    Class cls = ClassLoaderUtil.loadClass(f.getPath(),classLoader);
                    if(cls!=null){
                        classes.add(cls);
                    }else {
                        logger.error("load class {} error",f.getAbsoluteFile());
                    }
                }
            }
        }
        return classes;
    }




}
