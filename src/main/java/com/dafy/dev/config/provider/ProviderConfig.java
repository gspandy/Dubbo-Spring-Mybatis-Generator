package com.dafy.dev.config.provider;

import com.dafy.dev.config.MybatisConfig;
import com.dafy.dev.config.api.ApiConfig;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by chunxiaoli on 5/19/17.
 */
public class ProviderConfig extends ApiConfig{
    @JsonProperty("provider_dir_name")
    private String providerDirName;

    @JsonProperty("dto_dir_name")
    private String dtoDirName;

    @JsonProperty("dao_dir_name")
    private String daoDirName;

    @JsonProperty("orm_dir_name")
    private String ormDirName;

    @JsonProperty("pojo_dir_name")
    private String pojoDirName;

    @JsonProperty("log_file")
    private String logFile;

    @JsonProperty("mybatis")
    private MybatisConfig mybatisConfig;

    private String rootDir;

    private String constantsDir;

    private String controllerDir;


    private String mapperXmlFilePost;

    private String serviceFilePost;

    private String serviceProviderPomTemplatePath;


    @JsonProperty("log_config_file")
    private String logConfigFilePath;

    @JsonProperty("application_properties_template_path")
    private String applicationPropertiesTemplatePath;

    @JsonProperty("config_dir_name")
    private String configDirName;

    //spring 自动配置类路径
    private String springAutoConfigFullClassPath;


    public String getProviderDirName() {
        return providerDirName;
    }

    public void setProviderDirName(String providerDirName) {
        this.providerDirName = providerDirName;
    }

    public String getDtoDirName() {
        return dtoDirName;
    }

    public void setDtoDirName(String dtoDirName) {
        this.dtoDirName = dtoDirName;
    }

    public String getDaoDirName() {
        return daoDirName;
    }

    public void setDaoDirName(String daoDirName) {
        this.daoDirName = daoDirName;
    }

    public String getOrmDirName() {
        return ormDirName;
    }

    public void setOrmDirName(String ormDirName) {
        this.ormDirName = ormDirName;
    }

    public String getPojoDirName() {
        return pojoDirName;
    }

    public void setPojoDirName(String pojoDirName) {
        this.pojoDirName = pojoDirName;
    }

    public String getLogFile() {
        return logFile;
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    public String getServiceProviderPomTemplatePath() {
        return serviceProviderPomTemplatePath;
    }

    public MybatisConfig getMybatisConfig() {
        return mybatisConfig;
    }

    public void setMybatisConfig(MybatisConfig mybatisConfig) {
        this.mybatisConfig = mybatisConfig;
    }

    public void setServiceProviderPomTemplatePath(String serviceProviderPomTemplatePath) {
        this.serviceProviderPomTemplatePath = serviceProviderPomTemplatePath;
    }

    public String getMapperXmlFilePost() {
        return mapperXmlFilePost;
    }

    public void setMapperXmlFilePost(String mapperXmlFilePost) {
        this.mapperXmlFilePost = mapperXmlFilePost;
    }

    public String getServiceFilePost() {
        return serviceFilePost;
    }

    public void setServiceFilePost(String serviceFilePost) {
        this.serviceFilePost = serviceFilePost;
    }

    public String getLogConfigFilePath() {
        return logConfigFilePath;
    }

    public void setLogConfigFilePath(String logConfigFilePath) {
        this.logConfigFilePath = logConfigFilePath;
    }

    public String getApplicationPropertiesTemplatePath() {
        return applicationPropertiesTemplatePath;
    }

    public void setApplicationPropertiesTemplatePath(String applicationPropertiesTemplatePath) {
        this.applicationPropertiesTemplatePath = applicationPropertiesTemplatePath;
    }

    public String getSpringAutoConfigFullClassPath() {
        return springAutoConfigFullClassPath;
    }

    public void setSpringAutoConfigFullClassPath(String springAutoConfigFullClassPath) {
        this.springAutoConfigFullClassPath = springAutoConfigFullClassPath;
    }

    public String getConfigDirName() {
        return configDirName;
    }

    public void setConfigDirName(String configDirName) {
        this.configDirName = configDirName;
    }

    public String getRootDir() {
        return rootDir;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

    public String getConstantsDir() {
        return constantsDir;
    }

    public void setConstantsDir(String constantsDir) {
        this.constantsDir = constantsDir;
    }

    public String getControllerDir() {
        return controllerDir;
    }

    public void setControllerDir(String controllerDir) {
        this.controllerDir = controllerDir;
    }

    @Override
    public String toString() {
        return "ProviderConfig{" +
                "providerDirName='" + providerDirName + '\'' +
                ", dtoDirName='" + dtoDirName + '\'' +
                ", daoDirName='" + daoDirName + '\'' +
                ", ormDirName='" + ormDirName + '\'' +
                ", pojoDirName='" + pojoDirName + '\'' +
                ", logFile='" + logFile + '\'' +
                ", mybatisConfig=" + mybatisConfig +
                ", rootDir='" + rootDir + '\'' +
                ", constantsDir='" + constantsDir + '\'' +
                ", controllerDir='" + controllerDir + '\'' +
                ", mapperXmlFilePost='" + mapperXmlFilePost + '\'' +
                ", serviceFilePost='" + serviceFilePost + '\'' +
                ", serviceProviderPomTemplatePath='" + serviceProviderPomTemplatePath + '\'' +
                ", logConfigFilePath='" + logConfigFilePath + '\'' +
                ", applicationPropertiesTemplatePath='" + applicationPropertiesTemplatePath + '\'' +
                ", configDirName='" + configDirName + '\'' +
                ", springAutoConfigFullClassPath='" + springAutoConfigFullClassPath + '\'' +
                '}';
    }
}
