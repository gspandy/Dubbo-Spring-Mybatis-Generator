package com.dafy.dev.config;

import com.dafy.dev.pojo.ServiceInfo;
import com.dafy.dev.pojo.WebServerInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by chunxiaoli on 12/28/16.
 */
public class GlobalConfig {

    @JsonProperty("name")
    private String name;

    @JsonProperty("api_dir_name")
    private String apiDirName;

    @JsonProperty("config_dir_name")
    private String configDirName;

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


    private String outputDir;

    @JsonProperty("output_dir")
    private String dir;

    private String type;

    @JsonProperty("group_id")
    private String groupId;

    private String artifactId;

    private String version;

    private String owner;



    @JsonProperty("mybatis")
    private MybatisConfig mybatisConfig;

    private String mapperXmlFilePost;

    private String serviceFilePost;

    private String rootDir;

    private String constantsDir;

    private String controllerDir;



    //spring 自动配置类路径
    private String springAutoConfigFullClassPath;

    public String getSpringAutoConfigFullClassPath() {
        return springAutoConfigFullClassPath;
    }

    public void setSpringAutoConfigFullClassPath(String springAutoConfigFullClassPath) {
        this.springAutoConfigFullClassPath = springAutoConfigFullClassPath;
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

    public String getRootDir() {
        return rootDir;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

    @JsonProperty("application_properties_template_path")
    private String applicationPropertiesTemplatePath;

    @JsonProperty("web_application_properties_template_path")
    private String webApplicationPropertiesTemplatePath;

    @JsonProperty("service_provider_pom_template")
    private String serviceProviderPomTemplatePath;

    @JsonProperty("service_parent_pom_template")
    private String serviceParentPomTemplatePath;

    @JsonProperty("service_api_pom_template")
    private String serviceApiPomTemplatePath;

    @JsonProperty("web_pom_template")
    private String webPomTemplatePath;

    @JsonProperty("dubbo_provider_template")
    private String dubboProviderTemplatePath;

    @JsonProperty("dubbo_consumer_template")
    private String dubboConsumerTemplatePath;

    @JsonProperty("cgi_json_file")
    private String cgiJsonFile;

    @JsonProperty("log_config_file")
    private String logConfigFilePath;

    @JsonProperty("services")
    private List<ServiceInfo> serviceInfoList;

    @JsonProperty("webservers")
    private List<WebServerInfo> webServerInfos;


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


    public String getCgiJsonFile() {
        return cgiJsonFile;
    }

    public void setCgiJsonFile(String cgiJsonFile) {
        this.cgiJsonFile = cgiJsonFile;
    }

    public String getApplicationPropertiesTemplatePath() {
        return applicationPropertiesTemplatePath;
    }

    public void setApplicationPropertiesTemplatePath(String applicationPropertiesTemplatePath) {
        this.applicationPropertiesTemplatePath = applicationPropertiesTemplatePath;
    }

    public String getLogFile() {
        return logFile;
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDaoDirName() {
        return daoDirName;
    }

    public void setDaoDirName(String daoDirName) {
        this.daoDirName = daoDirName;
    }

    public MybatisConfig getMybatisConfig() {
        return mybatisConfig;
    }

    public void setMybatisConfig(MybatisConfig mybatisConfig) {
        this.mybatisConfig = mybatisConfig;
    }

    public String getApiDirName() {
        return apiDirName;
    }

    public void setApiDirName(String apiDirName) {
        this.apiDirName = apiDirName;
    }

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

    public String getConfigDirName() {
        return configDirName;
    }

    public void setConfigDirName(String configDirName) {
        this.configDirName = configDirName;
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

    public String getWebApplicationPropertiesTemplatePath() {
        return webApplicationPropertiesTemplatePath;
    }

    public void setWebApplicationPropertiesTemplatePath(
            String webApplicationPropertiesTemplatePath) {
        this.webApplicationPropertiesTemplatePath = webApplicationPropertiesTemplatePath;
    }

    public String getServiceProviderPomTemplatePath() {
        return serviceProviderPomTemplatePath;
    }

    public void setServiceProviderPomTemplatePath(String serviceProviderPomTemplatePath) {
        this.serviceProviderPomTemplatePath = serviceProviderPomTemplatePath;
    }

    public String getServiceParentPomTemplatePath() {
        return serviceParentPomTemplatePath;
    }

    public void setServiceParentPomTemplatePath(String serviceParentPomTemplatePath) {
        this.serviceParentPomTemplatePath = serviceParentPomTemplatePath;
    }

    public String getServiceApiPomTemplatePath() {
        return serviceApiPomTemplatePath;
    }

    public void setServiceApiPomTemplatePath(String serviceApiPomTemplatePath) {
        this.serviceApiPomTemplatePath = serviceApiPomTemplatePath;
    }

    public String getWebPomTemplatePath() {
        return webPomTemplatePath;
    }

    public void setWebPomTemplatePath(String webPomTemplatePath) {
        this.webPomTemplatePath = webPomTemplatePath;
    }

    public String getDubboProviderTemplatePath() {
        return dubboProviderTemplatePath;
    }

    public void setDubboProviderTemplatePath(String dubboProviderTemplatePath) {
        this.dubboProviderTemplatePath = dubboProviderTemplatePath;
    }

    public String getDubboConsumerTemplatePath() {
        return dubboConsumerTemplatePath;
    }

    public void setDubboConsumerTemplatePath(String dubboConsumerTemplatePath) {
        this.dubboConsumerTemplatePath = dubboConsumerTemplatePath;
    }

    public String getLogConfigFilePath() {
        return logConfigFilePath;
    }

    public void setLogConfigFilePath(String logConfigFilePath) {
        this.logConfigFilePath = logConfigFilePath;
    }

    public List<ServiceInfo> getServiceInfoList() {
        return serviceInfoList;
    }

    public void setServiceInfoList(List<ServiceInfo> serviceInfoList) {
        this.serviceInfoList = serviceInfoList;
    }

    public List<WebServerInfo> getWebServerInfos() {
        return webServerInfos;
    }

    public void setWebServerInfos(List<WebServerInfo> webServerInfos) {
        this.webServerInfos = webServerInfos;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "GlobalConfig{" +
                "name='" + name + '\'' +
                ", apiDirName='" + apiDirName + '\'' +
                ", configDirName='" + configDirName + '\'' +
                ", providerDirName='" + providerDirName + '\'' +
                ", dtoDirName='" + dtoDirName + '\'' +
                ", daoDirName='" + daoDirName + '\'' +
                ", ormDirName='" + ormDirName + '\'' +
                ", pojoDirName='" + pojoDirName + '\'' +
                ", logFile='" + logFile + '\'' +
                ", outputDir='" + outputDir + '\'' +
                ", dir='" + dir + '\'' +
                ", type='" + type + '\'' +
                ", groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", version='" + version + '\'' +
                ", owner='" + owner + '\'' +
                ", mybatisConfig=" + mybatisConfig +
                ", mapperXmlFilePost='" + mapperXmlFilePost + '\'' +
                ", serviceFilePost='" + serviceFilePost + '\'' +
                ", rootDir='" + rootDir + '\'' +
                ", constantsDir='" + constantsDir + '\'' +
                ", controllerDir='" + controllerDir + '\'' +
                ", springAutoConfigFullClassPath='" + springAutoConfigFullClassPath + '\'' +
                ", applicationPropertiesTemplatePath='" + applicationPropertiesTemplatePath + '\'' +
                ", webApplicationPropertiesTemplatePath='" + webApplicationPropertiesTemplatePath
                + '\''
                +
                ", serviceProviderPomTemplatePath='" + serviceProviderPomTemplatePath + '\'' +
                ", serviceParentPomTemplatePath='" + serviceParentPomTemplatePath + '\'' +
                ", serviceApiPomTemplatePath='" + serviceApiPomTemplatePath + '\'' +
                ", webPomTemplatePath='" + webPomTemplatePath + '\'' +
                ", dubboProviderTemplatePath='" + dubboProviderTemplatePath + '\'' +
                ", dubboConsumerTemplatePath='" + dubboConsumerTemplatePath + '\'' +
                ", cgiJsonFile='" + cgiJsonFile + '\'' +
                ", logConfigFilePath='" + logConfigFilePath + '\'' +
                ", serviceInfoList=" + serviceInfoList +
                ", webServerInfos=" + webServerInfos +
                ", reqDtoNameSuffix='" + reqDtoNameSuffix + '\'' +
                ", resDtoNameSuffix='" + resDtoNameSuffix + '\'' +
                '}';
    }
}
