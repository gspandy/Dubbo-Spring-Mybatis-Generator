package com.dafy.dev.config;

/**
 * Created by chunxiaoli on 1/5/17.
 */
public class ParameterInfo {
    private String name;
    private String TypeFullClassName;
    private Class type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeFullClassName() {
        return TypeFullClassName;
    }

    public void setTypeFullClassName(String typeFullClassName) {
        TypeFullClassName = typeFullClassName;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }
}
