package com.dafy.dev.config.dubbo;

/**
 * Created by chunxiaoli on 10/18/16.
 */
public class ProtocolConfig {
    private String name;
    private String owner;
    private String port;

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

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
