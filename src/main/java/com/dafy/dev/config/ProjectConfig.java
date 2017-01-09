package com.dafy.dev.config;

import java.util.List;
import java.util.Map;

/**
 * Created by chunxiaoli on 10/12/16.
 */
public class ProjectConfig {

    private String projectName;

    private String dirName;
    //use at pom.xml
    private String groupId;
    private String artifactId;
    private String version;

    private PomConfig pomConfig;

    private String logFile;

    //created pom file config
    private String dir;

    private Map<String,Map<String,List<String>>> services;

    private String logConfigPath;

    private String logConfigDir;

    public Map<String,Map<String,List<String>>> getServices() {
        return services;
    }

    public void setServices(Map<String,Map<String,List<String>>> services) {
        this.services = services;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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
        return "ProjectConfig{" +
                "projectName='" + projectName + '\'' +
                ", dirName='" + dirName + '\'' +
                ", groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", version='" + version + '\'' +
                '}';
    }

    public String getLogFile() {
        return logFile;
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    public String getLogConfigPath() {
        return logConfigPath;
    }

    public void setLogConfigPath(String logConfigPath) {
        this.logConfigPath = logConfigPath;
    }

    public String getLogConfigDir() {
        return logConfigDir;
    }

    public void setLogConfigDir(String logConfigDir) {
        this.logConfigDir = logConfigDir;
    }
}
