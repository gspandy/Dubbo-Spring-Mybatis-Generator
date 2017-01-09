package com.dafy.dev.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by chunxiaoli on 1/3/17.
 */
public class CgiConfig {

    @JsonProperty("cgi_prefix")
    private String cgiPrefix;

    @JsonProperty("controller_list")
    private List<ControllerConfig> controllerConfigList;

    public String getCgiPrefix() {
        return cgiPrefix;
    }

    public void setCgiPrefix(String cgiPrefix) {
        this.cgiPrefix = cgiPrefix;
    }

    public List<ControllerConfig> getControllerConfigList() {
        return controllerConfigList;
    }

    public void setControllerConfigList(
            List<ControllerConfig> controllerConfigList) {
        this.controllerConfigList = controllerConfigList;
    }
}
