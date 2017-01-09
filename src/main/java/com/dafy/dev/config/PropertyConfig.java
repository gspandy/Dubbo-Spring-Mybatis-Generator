package com.dafy.dev.config;

/**
 * Created by chunxiaoli on 10/18/16.
 */
/*
logging.file=/opt/logs/collection/account/debug.log

dubbo.application.name=account-provider
dubbo.application.owner=chunxiao
dubbo.protocol.name=dubbo
dubbo.protocol.port=30182
 */
public class PropertyConfig {
    //注释
    private String desc;
    //生成文件的路径
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
