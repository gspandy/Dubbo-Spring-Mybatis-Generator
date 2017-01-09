package com.dafy.dev.config;

import com.dafy.dev.pojo.TableInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by chunxiaoli on 10/18/16.
 */
public class MybatisConfig {
    private String projectDir;

    @JsonProperty("db_type")
    private String dbType;
    private String host;
    private String port;
    private String database;
    private String table;
    private String encoding;

    private String username;
    private String password;

    private String domainObjectName;

    private String daoOutputDir;
    private String modelOutputDir;
    private String mapperOutputDir;

    private String mapperXMLOutputDir;

    private String daoPackage;
    private String modelPackage;

    private String mapperXMLPackage;

    private String mapperPackage;

    @JsonProperty("connector_jar_path")
    private String connectorJarPath;

    @JsonProperty("tables")
    private List<TableInfo> tableInfoList;

    public String getProjectDir() {
        return projectDir;
    }

    public void setProjectDir(String projectDir) {
        this.projectDir = projectDir;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDomainObjectName() {
        return domainObjectName;
    }

    public void setDomainObjectName(String domainObjectName) {
        this.domainObjectName = domainObjectName;
    }

    public String getDaoOutputDir() {
        return daoOutputDir;
    }

    public void setDaoOutputDir(String daoOutputDir) {
        this.daoOutputDir = daoOutputDir;
    }

    public String getModelOutputDir() {
        return modelOutputDir;
    }

    public void setModelOutputDir(String modelOutputDir) {
        this.modelOutputDir = modelOutputDir;
    }

    public String getMapperXMLOutputDir() {
        return mapperXMLOutputDir;
    }

    public void setMapperXMLOutputDir(String mapperXMLOutputDir) {
        this.mapperXMLOutputDir = mapperXMLOutputDir;
    }

    public String getDaoPackage() {
        return daoPackage;
    }

    public void setDaoPackage(String daoPackage) {
        this.daoPackage = daoPackage;
    }

    public String getModelPackage() {
        return modelPackage;
    }

    public void setModelPackage(String modelPackage) {
        this.modelPackage = modelPackage;
    }

    public String getMapperXMLPackage() {
        return mapperXMLPackage;
    }

    public void setMapperXMLPackage(String mapperXMLPackage) {
        this.mapperXMLPackage = mapperXMLPackage;
    }

    public String getConnectorJarPath() {
        return connectorJarPath;
    }

    public void setConnectorJarPath(String connectorJarPath) {
        this.connectorJarPath = connectorJarPath;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getMapperOutputDir() {
        return mapperOutputDir;
    }

    public void setMapperOutputDir(String mapperOutputDir) {
        this.mapperOutputDir = mapperOutputDir;
    }

    public String getMapperPackage() {
        return mapperPackage;
    }

    public void setMapperPackage(String mapperPackage) {
        this.mapperPackage = mapperPackage;
    }

    public List<TableInfo> getTableInfoList() {
        return tableInfoList;
    }

    public void setTableInfoList(List<TableInfo> tableInfoList) {
        this.tableInfoList = tableInfoList;
    }

    @Override
    public String toString() {
        return "MybatisConfig{" +
                "projectDir='" + projectDir + '\'' +
                ", dbType='" + dbType + '\'' +
                ", host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", database='" + database + '\'' +
                ", table='" + table + '\'' +
                ", encoding='" + encoding + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", domainObjectName='" + domainObjectName + '\'' +
                ", daoOutputDir='" + daoOutputDir + '\'' +
                ", modelOutputDir='" + modelOutputDir + '\'' +
                ", mapperOutputDir='" + mapperOutputDir + '\'' +
                ", mapperXMLOutputDir='" + mapperXMLOutputDir + '\'' +
                ", daoPackage='" + daoPackage + '\'' +
                ", modelPackage='" + modelPackage + '\'' +
                ", mapperXMLPackage='" + mapperXMLPackage + '\'' +
                ", mapperPackage='" + mapperPackage + '\'' +
                ", connectorJarPath='" + connectorJarPath + '\'' +
                ", tableInfoList=" + tableInfoList +
                '}';
    }
}
