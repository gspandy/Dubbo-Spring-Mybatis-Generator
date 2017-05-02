package com.dafy.dev.config;

/**
 * Created by chunxiaoli on 10/12/16.
 */
public class Config {



    public static final String[] WEB_SERVER_DIRS=new String[]{
            "dto/request",
            "dto/response",
            "controller",
            "common",
            "config",
            "constants",
            "util"
    };
    public static final String[] API_SUB_DIRS=new String[]{
            "dto/request",
            "dto/response",
            "common",
            "config",
            "constants",
            "util"
    };

    public static final String[] PROVIDER_SUB_DIRS=new String[]{
            "dto/request",
            "dto/response",
            "common",
            "config",
            "constants",
            "util",
            "pojo",
            "dao/impl",
            "orm",
            "impl",
    };

    public static final String[] RESOURCES_SUB_DIRS =new String[]{
            "bin",
            "config",
            "dubbo",
            "mybatis"
    };

    public static final String MAVEN_SRC_DIR_PREFIX ="/src";
    public static final String  MAVEN_MAIN_DIR_PREFIX=MAVEN_SRC_DIR_PREFIX+"/main";
}
