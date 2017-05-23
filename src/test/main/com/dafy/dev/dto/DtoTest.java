package com.dafy.dev.dto;

import com.dafy.dev.config.DtoConfig;
import com.dafy.dev.generator.project.DtoFileGenerator;
import org.junit.Test;

import static com.dafy.dev.config.ConfigDefault.GLOBAL_DTO_REQ_SUFFIX;
import static com.dafy.dev.config.ConfigDefault.GLOBAL_DTO_RES_SUFFIX;

/**
 * Created by chunxiaoli on 5/2/17.
 */
public class DtoTest {
    @Test
    public void testCreateDtoFromJsonFile(){
        String json="app.json";
        DtoConfig dtoConfig = new DtoConfig();
        dtoConfig.setDir("./");
        dtoConfig.setPackageName("abc");
        dtoConfig.setReqDtoNameSuffix(GLOBAL_DTO_REQ_SUFFIX);
        dtoConfig.setResDtoNameSuffix(GLOBAL_DTO_RES_SUFFIX);

        new DtoFileGenerator(dtoConfig).generateDtoFromCgiJson(json);
    }
}
