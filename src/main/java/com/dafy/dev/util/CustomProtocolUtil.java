package com.dafy.dev.util;

import com.dafy.dev.pojo.ProtocolModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by m000665 on 2017/2/7.
 */
public class CustomProtocolUtil {
    public static ProtocolModel parseProtocol(InputStream inputStream) throws IOException {
        if(inputStream==null){
            throw new IllegalArgumentException("stream can not be null!");
        }
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));

        ProtocolModel protocolModel=new ProtocolModel();

        String module=bufferedReader.readLine();
        if(module!=null){
            Matcher matcher=Pattern.compile("\\w+").matcher(module);
            if(matcher.find()){
                module=matcher.group(0);
            }else {
                module=null;
            }
        }

        protocolModel.module=module;
        protocolModel.cgiList=new LinkedList<>();

        Pattern pattern=Pattern.compile("###([^\\s#]+)");

        for(String line=bufferedReader.readLine();line!=null;){

            ProtocolModel.CGI curCgi=new ProtocolModel.CGI();

            while (line!=null&&!line.trim().startsWith("CGI")){
                Matcher matcher=pattern.matcher(line);
                if (matcher.find()){
                    curCgi.name=matcher.group(1);
                    break;
                }
                line=bufferedReader.readLine();
            }

            while (!line.trim().startsWith("CGI")){
                line=bufferedReader.readLine();
            }
            if(line==null) break;

            curCgi.url=line.split(":")[1].trim();

            Matcher matcher=Pattern.compile("[\\w\\d/_]+").matcher(curCgi.url);
            if(matcher.find()){
                curCgi.url=matcher.group();
            }
            if(!curCgi.url.startsWith("/")) curCgi.url="/"+curCgi.url;

            for(line=bufferedReader.readLine();line!=null&&!line.trim().startsWith("请求");line=bufferedReader.readLine());

            StringBuffer stringBuffer=new StringBuffer();
            for(line=bufferedReader.readLine();line!=null&&!line.trim().startsWith("返回");line=bufferedReader.readLine()){
                stringBuffer.append(line).append("\n");
            }

            curCgi.request=parseString(stringBuffer.toString().toCharArray());

            stringBuffer=new StringBuffer();
            for(line=bufferedReader.readLine();line!=null&&!line.trim().startsWith("#");line=bufferedReader.readLine()){
                stringBuffer.append(line).append("\n");
            }

            Map<String, Object> resp= parseString(stringBuffer.toString().toCharArray());
            if(resp!=null&&resp.keySet().size()==4&&resp.containsKey("code")
                    &&resp.containsKey("sub_code")&&resp.containsKey("msg")&&resp.containsKey("data")){
                if(resp.get("data") instanceof Map){
                    curCgi.response= (Map) resp.get("data");
                }
            }
            else {
                curCgi.response=resp;
            }

            protocolModel.cgiList.add(curCgi);
        }

        return protocolModel;
    }

    public static void convert(ProtocolModel protocolModel,OutputStream outputStream) throws Exception {
        if(protocolModel==null||outputStream==null){
            throw new IllegalArgumentException("param can not be null!");
        }

        Map root=new HashMap();
        root.put("cgi_prefix","nothing");
        List controller=new LinkedList();
        root.put("controller_list",controller);

        Map controllerItem=new HashMap();
        controller.add(controllerItem);
        controllerItem.put("name","nothing");
        List cgiList=new LinkedList();
        controllerItem.put("cgi_list",cgiList);

        for(ProtocolModel.CGI cur:protocolModel.cgiList){

            Map cgiItem=new HashMap();
            cgiList.add(cgiItem);
            cgiItem.put("desc","nothing");

            String[] cgiArr=cur.url.split("/");
            StringBuffer cgi=new StringBuffer();
            for(int i=cgiArr.length-2;i<cgiArr.length;i++){
                if(i<0) continue;
                cgi.append(cgiArr[i]);
                if(i<cgiArr.length-1) cgi.append("_");
            }

            cgiItem.put("cgi",cgi.toString());
            cgiItem.put("request",cur.request==null||cur.request.isEmpty()?null:cur.request);
            cgiItem.put("response",cur.response==null||cur.response.isEmpty()?null:cur.response);

        }
        outputStream.write(new ObjectMapper().writeValueAsBytes(root));
        outputStream.flush();


    }
    public static Map<String, Object> parseString(char[] str) {
        try{
            return parseString(str, new int[]{0, str.length}, Map.class);
        }catch (Throwable e){
            e.printStackTrace();
        }
        return null;
    }

    private static <T> T parseString(char[] str, int[] point, Class<T> type) {

        if (type == String.class) {
            StringBuffer strBuffer = new StringBuffer();
            while (point[0] < point[1] && str[point[0]] != '"')
                strBuffer.append(str[point[0]++]);
            point[0]++;
            parseString(str,point,Void.class);
            return (T) (strBuffer.toString());
        }
        if (type == BigDecimal.class) {
            StringBuffer strBuffer = new StringBuffer();
            while (point[0] < point[1] && '0' <= str[point[0]] &&'9' >= str[point[0]])
                strBuffer.append(str[point[0]++]);
            parseString(str,point,Void.class);
            BigDecimal bigDecimal=new BigDecimal(strBuffer.toString());
            return (T) new BigDecimal(strBuffer.toString());
        }
        if (type == Map.class) {
            Map<String, Object> root = new HashMap<String, Object>();
            String field;
            parseString(str,point,Void.class);
            while (point[0] < point[1] && '{' != str[point[0]])
                point[0]++;
            while (point[0] < point[1]&&str[point[0]]!='}') {
                point[0]++;
                parseString(str,point,Void.class);
                while (point[0] < point[1] && !Pattern.matches("[\\w_]", str[point[0]] + ""))
                    point[0]++;
                StringBuffer fieldBuffer = new StringBuffer();
                while (point[0] < point[1] && Pattern.matches("[\\w\\d_]", str[point[0]] + ""))
                    fieldBuffer.append(str[point[0]++]);
                field = fieldBuffer.toString();
                parseString(str,point,Void.class);
                while (point[0] < point[1]&&str[point[0]] != ':')
                    point[0]++;
                point[0]++;
                while (point[0] < point[1]&&str[point[0]]!='}') {
                    int last=point[0];
                    parseString(str,point,Void.class);
                    if (str[point[0]] == '"') {
                        point[0]++;
                        root.put(field, parseString(str, point, String.class));
                        continue;
                    } else if ('{' == str[point[0]]) {
                        root.put(field, parseString(str, point, Map.class));
                        continue;
                    } else if ('0' <= str[point[0]] && '9' >= str[point[0]]) {
                        root.put(field,typeNumber(field, parseString(str, point, BigDecimal.class)));
                        continue;
                    } else if ('[' == str[point[0]]) {
                        point[0]++;
                        root.put(field, parseString(str, point, List.class));
                        continue;
                    }else if('，' == str[point[0]]||',' == str[point[0]]||last!=point[0]){
                        break;
                    }
                    point[0]++;
                }
            }
            point[0]++;
            parseString(str,point,Void.class);
            return (T) root;
        }
        if (type == List.class) {
            List objectList = new ArrayList();
            while (point[0] < point[1] && str[point[0]] != ']') {
                parseString(str,point,Void.class);
                if (str[point[0]] == '"') {
                    point[0]++;
                    objectList.add( parseString(str, point, String.class));
                    continue;
                } else if ('{' == str[point[0]]) {
                    objectList.add( parseString(str, point, Map.class));
                    continue;
                } else if ('0' <= str[point[0]] && '9' >= str[point[0]]) {
                    objectList.add(parseString(str, point, BigDecimal.class));
                    continue;
                } else if ('[' == str[point[0]]) {
                    point[0]++;
                    objectList.add( parseString(str, point, List.class));
                    continue;
                }
                point[0]++;
            }
            point[0]++;
            parseString(str,point,Void.class);
            return (T) objectList;
        }else if(type==Void.class){
            while (point[0]<point[1]&&Pattern.matches("\\s", str[point[0]] + "")) point[0]++;
            if(point[0]<point[1]&&str[point[0]]=='/'){
                while (point[0]<point[1]&&str[point[0]]!='\n') point[0]++;
                parseString(str,point,Void.class);
            }
        }
        return null;
    }

    private static BigDecimal typeNumber(String name,BigDecimal value){
        if(name.endsWith("_time")){
            return new BigDecimal(Long.MAX_VALUE);
        }
        if(name.endsWith("status")){
            return new BigDecimal(1);
        }

        return value;
    }
}
