package com.dafy.dev.util;

import com.dafy.dev.pojo.GlobalConfig;
import com.dafy.dev.pojo.WebConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by chunxiaoli on 10/18/16.
 */
public class ConfigUtil {




    public static Map merge(Map<String,Object> source, Map<String,Object> target){
        Map<String,Object> ret=new LinkedHashMap<>(target);

        source.forEach((k,v)->{
            if(!target.containsKey(k)){
                ret.put(k,v);
            }
        });
        return ret;
    }

    public static Map<String, Object> parseYamlConfig(InputStream inputStream) {
        Yaml yaml = new Yaml();
        return (Map<String, Object>) yaml
                .load(inputStream);
    }

    public static GlobalConfig parseConfig(InputStream inputStream) {

        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            return mapper.readValue(inputStream, GlobalConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static WebConfig parseWebConfig(InputStream inputStream) {

        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            return mapper.readValue(inputStream, WebConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
