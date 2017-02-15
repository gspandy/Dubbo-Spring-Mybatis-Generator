package com.dafy.dev;

import com.dafy.dev.config.DtoConfig;
import com.dafy.dev.generator.DtoGenerator;
import com.dafy.dev.util.CustomProtocolUtil;
import com.dafy.dev.util.PostmanBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
    public void postman() throws FileNotFoundException, JsonProcessingException {

        Map<String,String> headers=new HashMap<>();
        headers.put("Content-Type","application/json");
        headers.put("session_key", UUID.randomUUID().toString());

        Map<String,Object> jsonBody=new  HashMap<>();
        jsonBody.put("account","13200000");
        jsonBody.put("password","123456");
        Map<String,Object> workInfo=new HashMap<>();
        workInfo.put("company","达飞");
        workInfo.put("mobile","110");
        jsonBody.put("work_info",workInfo);
        String contacts[]=new String[]{"a","b","c"};
        jsonBody.put("contacts",contacts);

        PostmanBuilder.builder("达飞商城","好人家")
                .addFolder("账号")
                .addRequest("账号","注册","http://www.baidu.com","POST",headers,new ObjectMapper().writeValueAsString(jsonBody),true)
                .out(new FileOutputStream("target/postman.json"))
                .release();
    }
}
