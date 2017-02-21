package com.dafy.dev.config;

import com.dafy.dev.util.StringUtil;

import java.util.List;

/**
 * Created by chunxiaoli on 10/13/16.
 */
public class JavaFileConfig {
    private Kind kind;
    private String className;
    private String packageName;
    private String outDir;
    private String javaFileDoc;
    private List<String> interfaces;

    private List<MethodInfo> methodInfos;

    public List<String> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(List<String> interfaces) {
        this.interfaces = interfaces;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return StringUtil.captize(className) ;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Kind getKind() {
        return kind;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    public String getOutDir() {
        return outDir;
    }

    public void setOutDir(String outDir) {
        this.outDir = outDir;
    }

    public String getJavaFileDoc() {
        return javaFileDoc;
    }

    public void setJavaFileDoc(String javaFileDoc) {
        this.javaFileDoc = javaFileDoc;
    }

    public List<MethodInfo> getMethodInfos() {
        return methodInfos;
    }

    public void setMethodInfos(List<MethodInfo> methodInfos) {
        this.methodInfos = methodInfos;
    }
}
