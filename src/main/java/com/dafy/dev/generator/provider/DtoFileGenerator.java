package com.dafy.dev.generator.provider;

import com.dafy.dev.config.DtoConfig;
import com.dafy.dev.config.PojoConfig;
import com.dafy.dev.generator.common.Generator;
import com.dafy.dev.generator.common.PojoFileGenerator;
import com.dafy.dev.parser.JsonToPojo;
import com.dafy.dev.pojo.CgiConfig;
import com.dafy.dev.pojo.CgiInfo;
import com.dafy.dev.pojo.ControllerConfig;
import com.dafy.dev.util.SourceCodeUtil;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import static java.lang.System.exit;

/**
 * Created by chunxiaoli on 10/20/16.
 */
public class DtoFileGenerator implements Generator {

    private static final Logger logger = LoggerFactory.getLogger(DtoFileGenerator.class);

    private DtoConfig dtoConfig;

    public DtoFileGenerator(DtoConfig dtoConfig) {
        this.dtoConfig = dtoConfig;
    }

    public DtoConfig getDtoConfig() {
        return dtoConfig;
    }

    public void setDtoConfig(DtoConfig dtoConfig) {
        this.dtoConfig = dtoConfig;
    }

    @Override
    public void generate() {
        generateDtoFromCgiJson(this.dtoConfig.getJsonConfigPath());
    }

    public void generateDtoFromCgiJson(String jsonFile) {
        CgiConfig cgiConfig = parseCgiConfig(jsonFile);
        if (cgiConfig == null) {
            logger.error("parse json error");
            return;
        }
        List<ControllerConfig> list = cgiConfig.getControllerConfigList();

        for(ControllerConfig config:list){
            List<CgiInfo> cgis= config.getCgiInfoList();
            for (CgiInfo cgiInfo:cgis){
                String cgiName = cgiInfo.getCgi();

                String requestDtoName= SourceCodeUtil.jsontoUpperCase(cgiName)+this.dtoConfig.getReqDtoNameSuffix();
                String responseDtoName=SourceCodeUtil.jsontoUpperCase(cgiName)+this.dtoConfig.getResDtoNameSuffix();

                String requestDtoFullName=dtoConfig.getRequestDtoPackage()+requestDtoName;
                String responseDtoFullName=dtoConfig.getResponseDtoPackage()+responseDtoName;


                if(cgiInfo.getRequest()!=null){
                    String request = new Gson().toJson(cgiInfo.getRequest());
                    JsonToPojo.fromJsonNode(new Gson().fromJson(request,JsonElement.class),requestDtoFullName,
                            new File(this.dtoConfig.getDir()),true,true,true);
                }
                if(cgiInfo.getResponse()!=null){
                    String response = new Gson().toJson(cgiInfo.getResponse());
                    JsonToPojo.fromJsonNode(new Gson().fromJson(response,JsonElement.class),responseDtoFullName,
                            new File(this.dtoConfig.getDir()),true,true,true);
                }


            }
        }

    }

    private CgiConfig parseCgiConfig(String jsonFile) {
        final ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        try {
            return mapper.readValue(new FileInputStream(jsonFile), CgiConfig.class);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("json file parse error :{} please check your :{}", e, jsonFile);
            exit(1);
        }
        return null;
    }

    public void generateFromPojoClass(Class sourceCls, String targetClassFullName) {
        PojoConfig pojoConfig = new PojoConfig();
        pojoConfig.setOutDir(dtoConfig.getDir());
        pojoConfig.setPackageName(dtoConfig.getPackageName());
        pojoConfig.setToString(true);
        new PojoFileGenerator(pojoConfig).generate(sourceCls, targetClassFullName);

    }

}
