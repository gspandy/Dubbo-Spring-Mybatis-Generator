package com.dafy.dev.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by chunxiaoli on 12/29/16.
 */
public class WebServerInfo {
    @JsonProperty("name")
    private String name;

    @JsonProperty("cgi_json_file")
    private String jsonFile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJsonFile() {
        return jsonFile;
    }

    public void setJsonFile(String jsonFile) {
        this.jsonFile = jsonFile;
    }

    @Override
    public String toString() {
        return "WebServerInfo{" +
                "name='" + name + '\'' +
                ", jsonFile='" + jsonFile + '\'' +
                '}';
    }
}
