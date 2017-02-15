package com.dafy.dev;

import com.dafy.dev.config.DtoConfig;
import com.dafy.dev.generator.DtoGenerator;
import com.dafy.dev.pojo.PostmanModel;
import com.dafy.dev.util.CustomProtocolUtil;
import com.dafy.dev.util.PostmanBuilder;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

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

    @Test
    public void postman() throws FileNotFoundException {
        PostmanBuilder.builder("达飞商城","好人家")
                .addFolder("账号")
                .addRequest("账号","注册","http://www.baidu.com","POST",new HashMap<>(),new HashMap<>())
                .out(new FileOutputStream("target/postman.json"))
                .release();
    }
}
