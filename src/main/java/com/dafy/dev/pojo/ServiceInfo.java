package com.dafy.dev.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by chunxiaoli on 12/29/16.
 */
public class ServiceInfo {
    @JsonProperty("name")
    private String          name;

    private String          owner;

    @JsonProperty("tables")
    private String tables;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTables() {
        return tables;
    }

    public void setTables(String tables) {
        this.tables = tables;
    }
}
