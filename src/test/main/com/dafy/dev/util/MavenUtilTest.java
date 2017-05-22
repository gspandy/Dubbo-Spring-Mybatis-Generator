package com.dafy.dev.util;

import com.dafy.dev.generator.ProjectGenerator;
import com.dafy.dev.generator.maven.MavenDirUtil;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chunxiaoli on 5/15/17.
 */
public class MavenUtilTest{
    private static final Logger logger = LoggerFactory.getLogger(ProjectGenerator.class);

    private final String dir=".";

    @Test
    public void testUtil() {
        Class<MavenDirUtil> cls = MavenDirUtil.class;
        Method[] methods = cls.getDeclaredMethods();
        for (Method method:methods){
            if(Modifier.isPublic(method.getModifiers())&&Modifier.isStatic(method.getModifiers())){
                try {
                    Parameter[] parameters = method.getParameters();
                    List<Object> params= new ArrayList<>(parameters.length);
                    for (Parameter p:parameters){
                        Class t= p.getType();
                        params.add(t.newInstance());
                    }
                    Object ret= method.invoke(null,params.toArray());
                    logger.info("method {},{}",method.getName(),ret);

                } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Test
    public void testDir(){
        String baseDir= MavenDirUtil.getMavenBaseDir(dir);
        Assert.assertTrue((dir+"/src/main").equals(baseDir));
        Assert.assertTrue((dir+"/src/main").equals(baseDir));
    }


}
