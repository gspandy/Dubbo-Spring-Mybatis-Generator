package com.dafy.dev.config;

import java.lang.reflect.Type;

/**
 * Created by chunxiaoli on 1/5/17.
 */
public class ParameterInfo {
    private String name;
    private String TypeFullClassName;
    private Class type;
    private Type parameterizedType;

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

    public Type getParameterizedType() {
        return parameterizedType;
    }

    public void setParameterizedType(Type parameterizedType) {
        this.parameterizedType = parameterizedType;
    }
}
