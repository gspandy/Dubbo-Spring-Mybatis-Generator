package com.dafy.dev.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by m000665 on 2017/2/15.
 */
public class PostmanModel {
    public String id;
    public String name;
    public String description="";
    public long timestamp;
    public long owner;

    @JsonProperty("public")
    public boolean isPublic;

    public List<Request> requests;
    public List<String> order;
    public List<Folder> folders;

    public static class Folder{
        public String id;
        public String name;
        public String description="";
        public List<String> order;
        public long owner;
        public String collectionId;
    }

    public static class Request{
        public String id;
        public String headers;
        public String url;
        public String preRequestScript;
        public Map pathVariables=new HashMap();
        public String method;
        public List data=new LinkedList();
        public String dataMode="raw";
        public String tests;
        public String currentHelper="normal";
        public long time;
        public String name;
        public String description="";
        public String collectionId;
        public List responses=new LinkedList();
        public String rawModeData;
        public Map helperAttributes;
    }
}
