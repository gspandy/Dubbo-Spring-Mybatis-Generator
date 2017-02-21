package com.dafy.dev.generator;

import com.dafy.dev.config.PropertyConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by chunxiaoli on 10/18/16.
 */
public class PropertiesGenerator {

    private PropertyConfig propertyConfig;

    private Properties properties;



    public PropertiesGenerator(PropertyConfig config){
        this.propertyConfig=config;
        this.properties=new Properties();
        if(new File(config.getPath()).exists()){
            try {
                this.properties.load(new FileInputStream(config.getPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public void generate(){
        write();
    }

    public void set(String key,String value){
        properties.setProperty(key,value);
    }


    private void write(){
        String path=propertyConfig.getPath();
        try {
            properties.store(new FileOutputStream(new File(path)),propertyConfig.getDesc()!=null?propertyConfig.getDesc():"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
