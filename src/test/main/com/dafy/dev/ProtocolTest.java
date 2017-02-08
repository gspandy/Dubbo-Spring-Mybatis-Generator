package com.dafy.dev;

import com.dafy.dev.config.DtoConfig;
import com.dafy.dev.generator.DtoGenerator;
import com.dafy.dev.util.CustomProtocolUtil;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by m000665 on 2017/2/8.
 */
public class ProtocolTest {

    @Test
    public void testJson2Pojo() {
        try {

            try(InputStream fin=ClassLoader.getSystemResourceAsStream("hqx.txt"); FileOutputStream fo=new FileOutputStream("target/hqx.json")){
                CustomProtocolUtil.parseProtocol(fin,fo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        DtoConfig dtoConfig = new DtoConfig();
        dtoConfig.setDir("out/src");
        dtoConfig.setPackageName("com.dafy.collection");
        dtoConfig.setJsonConfigPath("target/hqx.json");
        dtoConfig.setReqDtoNameSuffix("Req");
        dtoConfig.setResDtoNameSuffix("Resp");
        new DtoGenerator(dtoConfig).generate();
    }
}
