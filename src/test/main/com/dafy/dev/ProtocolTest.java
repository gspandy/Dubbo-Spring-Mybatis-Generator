package com.dafy.dev;

import com.dafy.dev.config.DtoConfig;
import com.dafy.dev.generator.project.DtoGenerator;
import com.dafy.dev.pojo.ProtocolModel;
import com.dafy.dev.util.CustomProtocolUtil;
import com.dafy.dev.util.PostmanBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by m000665 on 2017/2/8.
 */
public class ProtocolTest {

    @Test
    public void postman() throws FileNotFoundException, JsonProcessingException {

        Map<String,String> headers=new HashMap<>();
        headers.put("Content-Type","application/json");
        headers.put("session_key", "{{session_key}}");

        Map<String,Object> jsonBody=new  HashMap<>();
        jsonBody.put("account","13200000");
        jsonBody.put("password","123456");
        Map<String,Object> workInfo=new HashMap<>();
        workInfo.put("company","达飞");
        workInfo.put("mobile","110");
        jsonBody.put("work_info",workInfo);
        String contacts[]=new String[]{"a","b","c"};
        jsonBody.put("contacts",contacts);

        PostmanBuilder.builder("达飞商城")
                .addRequest("账号","注册","http://{{host}}:{{port}}/","POST",headers,new ObjectMapper().writeValueAsString(jsonBody),true)
                .out(new FileOutputStream("target/postman.json"));
    }

    @Test
    public void mall() throws FileNotFoundException {
        File dir=new File("D:/git/dafymall_server/docs/协议");

        PostmanBuilder builder=PostmanBuilder.builder("达飞商城");

        for(File file:dir.listFiles()){
            if(file.isDirectory()||!file.getName().startsWith("app商品")) continue;
            String dst="target/"+file.getName()+".json";
            try (InputStream fin =new FileInputStream(file); FileOutputStream fo = new FileOutputStream(dst)) {

                ProtocolModel protocolModel=CustomProtocolUtil.parseProtocol(fin);
                CustomProtocolUtil.convert(protocolModel,fo);

                DtoConfig dtoConfig = new DtoConfig();
                dtoConfig.setDir("target");
                dtoConfig.setPackageName("com.dafy.mall."+protocolModel.module+".api");
                dtoConfig.setJsonConfigPath(dst);
                dtoConfig.setReqDtoNameSuffix("Req");
                dtoConfig.setResDtoNameSuffix("Resp");
                new DtoGenerator(dtoConfig).generate();

                Map<String,String> headers=new HashMap<>();
                headers.put("Content-Type","application/json");
                headers.put("session_key", "{{session_key}}");

                ObjectMapper objectMapper=new ObjectMapper();

                for(ProtocolModel.CGI cur:protocolModel.cgiList){

                    String folder=null;
                    String prefex;
                    if(file.getName().contains("app")){
                        folder="app";
                        prefex="/dafymall_app/v1";
                    }else {
                        folder="管理后台";
                        prefex="/dafymall_admin/v1";
                    }

                    if(!cur.url.contains("dafymall_")){
                        cur.url=prefex+cur.url;
                    }
                    builder.addRequest(folder,
                            cur.name,
                            "http://{{host}}:{{port}}"+cur.url,
                            "POST",
                            headers,
                            objectMapper.writeValueAsString(cur.request),
                            true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        builder.out(new FileOutputStream("target/达飞商城.json"));
    }

    @Test
    public void pattern(){
        Pattern pattern=Pattern.compile("###([^#]+)###");
        Matcher matcher=pattern.matcher("###订单列表###");
        if(matcher.find()){
           System.out.println(matcher.group(1));
        }
    }

    @Test
    public void json(){
        String str="[{\n" +
                "\n" +
                "    cat_name:\"\",//类目名称\n" +
                "    sort_order:12,//排序号\n" +
                "    second_cat_id:1, //类目id\n" +
                "    third_cat_list:[{\n" +
                "            image:\"\" //展示图片\n" +
                "            cat_name:\"\",//类目名称\n" +
                "            sort_order:12,//排序号\n" +
                "            third_cat_id:1, //三级类目id\n" +
                "     }]\n" +
                "}]";
        char arr[]=str.toCharArray();
        List json=CustomProtocolUtil.parseString(arr,new int[]{0,arr.length},List.class);
    }
}
