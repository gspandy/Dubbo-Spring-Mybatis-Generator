package com.dafy.dev.generator;

import com.dafy.dev.util.FileUtil;
import com.dafy.dev.util.StringUtil;

import java.io.File;
import java.io.InputStream;

/**
 * Created by chunxiaoli on 12/29/16.
 */
public class YamlConfigGenerator {
    public static void generate(String type,String path){
        String template="template/config.yaml";
        path= StringUtil.isEmpty(path)?"./":path;
        type=StringUtil.isEmpty(type)?"rpc":type;
        String name="config.yaml";
        switch (type){
            case "rpc":
                template="template/rpc_config.yaml";
                break;
            case "web":
                template="template/web_config.yaml";
                break;
            case "all":
                template="template/all_config.yaml";
                break;
        }
        InputStream inputStream=YamlConfigGenerator.class.getClassLoader()
                .getResourceAsStream(template);
        FileUtil.save(inputStream,path+ File.separator+name);

        InputStream cgiInputStream=YamlConfigGenerator.class.getClassLoader()
                .getResourceAsStream("template/cgi.json");

        FileUtil.save(cgiInputStream,path+ File.separator+"cgi.json");

        InputStream logback=YamlConfigGenerator.class.getClassLoader()
                .getResourceAsStream("template/logback.xml");

        FileUtil.save(logback,path+ File.separator+"logback.xml");


        InputStream properties=YamlConfigGenerator.class.getClassLoader()
                .getResourceAsStream("template/application.properties");
        FileUtil.save(properties,path+ File.separator+"application.properties");

        InputStream pom=YamlConfigGenerator.class.getClassLoader()
                .getResourceAsStream("template/pom.xml");
        FileUtil.save(pom,path+ File.separator+"pom.xml");
    }
}
