package com.dafy.dev.util;

import com.dafy.dev.codegen.ClassLoaderUtil;
import org.junit.Test;

/**
 * Created by chunxiaoli on 5/15/17.
 */
public class ClassLoaderUtilTest {

    @Test
    public void testClassPathBuild(){
       String path= ClassLoaderUtil.buildClassPath
               ("/Users/chunxiaoli/Library/Caches/IntelliJIdea2017.1/plugins-sandbox/plugins");
       System.out.println("path:"+path);
    }

    @Test
    public void testLoadClass(){
        String d="/Users/chunxiaoli/.gradle/caches/modules-2/files-2.1/com.jetbrains.intellij.idea/ideaIU/171.4073.35/786e3fdca7eda7f1d3256aa96489b32fbcdd60b2/ideaIU-171.4073.35/lib/openapi.jar";
        String dir="/Users/chunxiaoli/IdeaProjects/Dubbo-Spring-Mybatis-Generator/Gaia1-rpc/Gaia1-api/";
        ClassLoaderUtil.loadAllClass(dir);
    }

    @Test
    public void testLoadPluginClass(){
        String dir="/Users/chunxiaoli/IdeaProjects/Dubbo-Spring-Mybatis-Generator/Gaia1-rpc/";
        ClassLoaderUtil.loadAllClass(dir);
    }
}
