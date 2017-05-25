package com.dafy.dev.config;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by chunxiaoli on 5/19/17.
 */
public class CommonConfig {


    @JsonProperty("artifact_id")
    private String artifactId;

    private String version;

    private String owner;

    @JsonProperty("name")
    private String name;

    @JsonProperty("output_dir")
    private String dir;

    private String type;

    @JsonProperty("group_id")
    private String groupId;

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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
