package com.dafy.dev.config;

import java.util.List;

/**
 * Created by chunxiaoli on 10/12/16.
 */
public class PomConfig {




    private String projectName;

    private String dirName;

    //use at pom.xml
    private String groupId;
    private String artifactId;
    private String version;


    //created pom file config
    private String dir;

    private List<String> modules;

    private String packaging;

    //pom template as the default config
    private String template;

    //parent template pom
    private String parentTemplate;


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

    public List<String> getModules() {
        return modules;
    }

    public void setModules(List<String> modules) {
        this.modules = modules;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public String getParentTemplate() {
        return parentTemplate;
    }

    public void setParentTemplate(String parentTemplate) {
        this.parentTemplate = parentTemplate;
    }

    @Override
    public String toString() {
        return "PomConfig{" +
                "projectName='" + projectName + '\'' +
                ", dirName='" + dirName + '\'' +
                ", groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", version='" + version + '\'' +
                ", dir='" + dir + '\'' +
                ", modules=" + modules +
                '}';
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
