package com.dafy.dev.pojo;

import java.util.List;
import java.util.Map;

/**
 * Created by m000665 on 2017/2/16.
 */
public class ProtocolModel {

    public String module;
    public List<CGI> cgiList;

    public static class CGI {
        public String url;
        public String name;
        public Map    request;
        public Map    response;
    }

}
