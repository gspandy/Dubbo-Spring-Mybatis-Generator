package com.dafy.dev.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by chunxiaoli on 1/3/17.
 */
public class ControllerConfig {

    @JsonProperty("name")
    private String name;

    @JsonProperty("cgi_list")
    private List<CgiInfo> cgiInfoList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CgiInfo> getCgiInfoList() {
        return cgiInfoList;
    }

    public void setCgiInfoList(List<CgiInfo> cgiInfoList) {
        this.cgiInfoList = cgiInfoList;
    }
}
