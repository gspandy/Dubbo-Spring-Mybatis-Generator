package com.dafy.dev.generator;

import com.dafy.dev.config.PojoConfig;
import com.dafy.dev.parser.JsonToPojo;

import java.io.File;

/**
 * Created by chunxiaoli on 10/17/16.
 */
public class PojoFileGenerator {

    private PojoConfig pojoConfig;

    public PojoFileGenerator(PojoConfig pojoConfig) {
        this.pojoConfig=pojoConfig;
    }


    public void generate(){
        JsonToPojo.fromInputStream(getClass().getClassLoader().getResourceAsStream("test.json"),
                pojoConfig.getPackageName(),new File(this.pojoConfig.getDir()),true,true);
    }


}
