package com.dafy.dev.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by chunxiaoli on 12/28/16.
 */
public class TableInfo {
    //实际表名

    @JsonProperty("table_name")
    private String tableName;

    //实体名
    @JsonProperty("domain_name")
    private String domainName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    @Override
    public String toString() {
        return "TableInfo{" +
                "tableName='" + tableName + '\'' +
                ", domainName='" + domainName + '\'' +
                '}';
    }
}
