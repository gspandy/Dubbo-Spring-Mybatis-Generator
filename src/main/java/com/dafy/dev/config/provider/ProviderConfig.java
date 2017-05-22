package com.dafy.dev.config.provider;

import com.dafy.dev.config.CommonConfig;
import com.dafy.dev.config.MybatisConfig;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by chunxiaoli on 5/19/17.
 */
public class ProviderConfig extends CommonConfig{
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

    private String serviceProviderPomTemplatePath;

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
}
