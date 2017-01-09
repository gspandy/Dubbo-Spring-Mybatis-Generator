package com.dafy.dev.config;

/**
 * Created by chunxiaoli on 10/26/16.
 */
public class DtoConfig {
    //json文件路径
    private String jsonConfigPath;
    private String packageName;
    private String dir;


    private String reqDtoNameSuffix;

    private String resDtoNameSuffix;


    public String getReqDtoNameSuffix() {
        return reqDtoNameSuffix;
    }

    public void setReqDtoNameSuffix(String reqDtoNameSuffix) {
        this.reqDtoNameSuffix = reqDtoNameSuffix;
    }

    public String getResDtoNameSuffix() {
        return resDtoNameSuffix;
    }

    public void setResDtoNameSuffix(String resDtoNameSuffix) {
        this.resDtoNameSuffix = resDtoNameSuffix;
    }

    public String getJsonConfigPath() {
        return jsonConfigPath;
    }

    public void setJsonConfigPath(String jsonConfigPath) {
        this.jsonConfigPath = jsonConfigPath;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getRequestDtoPackage(){
        return getPackageName()+".dto.request.";
    }

    public String getResponseDtoPackage(){
        return getPackageName()+".dto.response.";
    }
}
