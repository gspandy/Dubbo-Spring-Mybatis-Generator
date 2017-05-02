package com.dafy.dev.generator;

import com.dafy.dev.config.DtoConfig;
import com.dafy.dev.config.PojoConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chunxiaoli on 10/20/16.
 */
public class DtoFileGenerator implements Generator{


    private static final Logger logger = LoggerFactory.getLogger(DtoFileGenerator.class);


    private DtoConfig dtoConfig;

    public DtoFileGenerator(DtoConfig dtoConfig){
        this.dtoConfig=dtoConfig;
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

    public void generateDtoFromCgiJson(String jsonFile){




    }


    public void generateFromPojoClass(Class sourceCls,String targetClassFullName){
        PojoConfig pojoConfig = new PojoConfig();
        pojoConfig.setOutDir(dtoConfig.getDir());
        pojoConfig.setPackageName(dtoConfig.getPackageName());
        new PojoFileGenerator(pojoConfig).generate(sourceCls,targetClassFullName);

    }



}
