package com.dafy.dev.config;

import java.io.Serializable;

/**
 * Created by chunxiaoli on 10/12/16.
 */
public class PojoConfig implements Serializable{

    private static final long serialVersionUID = -6386877430578914326L;

    private String className;
    private String  packageName;
    private String  outDir;
    private boolean serializable;
    private boolean toString;



    public boolean isToString() {
        return toString;
    }

    public void setToString(boolean toString) {
        this.toString = toString;
    }

    public boolean isSerializable() {
        return serializable;
    }

    public void setSerializable(boolean serializable) {
        this.serializable = serializable;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getOutDir() {
        return outDir;
    }

    public void setOutDir(String outDir) {
        this.outDir = outDir;
    }






}
