package com.dafy.dev.pojo;

/**
 * Created by chunxiaoli on 1/3/17.
 */
public class CgiInfo {

    private String desc;

    private String cgi;

    private Object request;

    private Object response;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCgi() {
        return cgi;
    }

    public void setCgi(String cgi) {
        this.cgi = cgi;
    }

    public Object getRequest() {
        return request;
    }

    public void setRequest(Object request) {
        this.request = request;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }
}
