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

        try (InputStream fin = ClassLoader.getSystemResourceAsStream("hqx.txt");
                FileOutputStream fo = new FileOutputStream("target/hqx.json")) {
            String module=CustomProtocolUtil.parseProtocol(fin, fo);

            DtoConfig dtoConfig = new DtoConfig();
            dtoConfig.setDir("target");
            dtoConfig.setPackageName("com.dafy."+module);
            dtoConfig.setJsonConfigPath("target/hqx.json");
            dtoConfig.setReqDtoNameSuffix("Req");
            dtoConfig.setResDtoNameSuffix("Resp");
            new DtoGenerator(dtoConfig).generate();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
