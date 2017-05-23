package com.dafy.dev.config.api;

import com.dafy.dev.config.CommonConfig;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by chunxiaoli on 5/19/17.
 */
public class ApiConfig extends CommonConfig{

    @JsonProperty("api_dir_name")
    private String apiDirName;

    @JsonProperty("dto_dir_name")
    private String dtoDirName;

    private String serviceApiPomTemplatePath;

    private String serviceFilePost;

    public String getApiDirName() {
        return apiDirName;
    }

    public void setApiDirName(String apiDirName) {
        this.apiDirName = apiDirName;
    }

    public String getDtoDirName() {
        return dtoDirName;
    }

    public void setDtoDirName(String dtoDirName) {
        this.dtoDirName = dtoDirName;
    }

    public String getServiceApiPomTemplatePath() {
        return serviceApiPomTemplatePath;
    }

    public void setServiceApiPomTemplatePath(String serviceApiPomTemplatePath) {
        this.serviceApiPomTemplatePath = serviceApiPomTemplatePath;
    }

    public String getServiceFilePost() {
        return serviceFilePost;
    }

    public void setServiceFilePost(String serviceFilePost) {
        this.serviceFilePost = serviceFilePost;
    }
}
