package com.dafy.dev.config;

import java.util.List;

/**
 * Created by chunxiaoli on 1/5/17.
 */
public class Method {
    private String name;
    private Class returnType;
    private List<Parameter> parameterInfoList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getReturnType() {
        return returnType;
    }

    public void setReturnType(Class returnType) {
        this.returnType = returnType;
    }

    public List<Parameter> getParameterInfoList() {
        return parameterInfoList;
    }

    public void setParameterInfoList(List<Parameter> parameterInfoList) {
        this.parameterInfoList = parameterInfoList;
    }
}
