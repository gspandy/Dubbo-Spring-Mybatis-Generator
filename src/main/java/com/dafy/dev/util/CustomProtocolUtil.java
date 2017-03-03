package com.dafy.dev.util;

import com.dafy.dev.pojo.ProtocolModel;
import com.fasterxml.jackson.databind.ObjectMapper;

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
        System.out.println(module);
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
            System.out.println(line);
            ProtocolModel.CGI curCgi=new ProtocolModel.CGI();

            while (line!=null&&!line.trim().startsWith("CGI")){
                Matcher matcher=pattern.matcher(line);
                if (matcher.find()){
                    curCgi.name=matcher.group(1);
                    break;
                }
                line=bufferedReader.readLine();
                System.out.println(line);
            }

            while (!line.trim().startsWith("CGI")){
                line=bufferedReader.readLine();
                System.out.println(line);
            }
            if(line==null) break;

            curCgi.url=line.split(":")[1].trim();

            Matcher matcher=Pattern.compile("[\\w\\d/_]+").matcher(curCgi.url);
            if(matcher.find()){
                curCgi.url=matcher.group();
            }
            if(!curCgi.url.startsWith("/")) curCgi.url="/"+curCgi.url;

            for(line=bufferedReader.readLine();line!=null&&!line.trim().startsWith("请求");line=bufferedReader.readLine()){
                System.out.println(line);
            }
            System.out.println(line);

            StringBuffer stringBuffer=new StringBuffer();
            for(line=bufferedReader.readLine();line!=null&&!line.trim().startsWith("返回");line=bufferedReader.readLine()){
                stringBuffer.append(line).append("\n");
                System.out.println(line);
            }
            System.out.println(line);

            if(!stringBuffer.toString().trim().isEmpty()){
                try{
                    char[] req=stringBuffer.toString().toCharArray();
                    curCgi.request=parseString(req,new int[]{0,req.length},Map.class);
                }catch (Throwable e){
                    e.printStackTrace();
                }
            }

            stringBuffer=new StringBuffer();
            for(line=bufferedReader.readLine();line!=null&&!line.trim().startsWith("#");line=bufferedReader.readLine()){
                stringBuffer.append(line).append("\n");
                System.out.println(line);
            }

            if(!stringBuffer.toString().trim().isEmpty()){
                char[] res=stringBuffer.toString().toCharArray();
                try{
                    Map resp= parseString(res,new int[]{0,res.length},Map.class);
                    if(resp!=null&&resp.keySet().size()==4&&resp.containsKey("code")
                            &&resp.containsKey("sub_code")&&resp.containsKey("msg")&&resp.containsKey("data")){
                        curCgi.response= resp.get("data");
                    }
                }catch (Throwable e){
                    try{
                        curCgi.response= parseString(res,new int[]{0,res.length},List.class);
                    }catch (Throwable a){
                        e.printStackTrace();
                    }

                }
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

           cgiItem.put("response",cur.response==null?null:cur.response);

        }
        outputStream.write(new ObjectMapper().writeValueAsBytes(root));
        outputStream.flush();


    }

    public static <T> T parseString(char[] str, int[] point, Class<T> type) {

        if (type == String.class) {
            if(str[point[0]]!='"'){
                throw new IllegalArgumentException(new String(str,0,point[0]));
            }

            point[0]++;
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
            return (T) new BigDecimal(strBuffer.toString());
        }
        if (type == Map.class) {
            parseString(str,point,Void.class);

            Map<String, Object> root = new HashMap<String, Object>();
            String field;
//            while (point[0] < point[1] && '{' != str[point[0]])
//                point[0]++;

            if(str[point[0]]!='{'){
                throw new IllegalArgumentException(new String(str,0,point[0]));
            }
            point[0]++;

            while (point[0] < point[1]&&str[point[0]]!='}') {

                parseString(str,point,Void.class);

                if(!Pattern.matches("[\\w_]", str[point[0]] + "")&&'"'!=str[point[0]]){
                    throw new IllegalArgumentException(new String(str,0,point[0]));
                }

//                while (point[0] < point[1] && !Pattern.matches("[\\w_]", str[point[0]] + ""))
//                {
//                    point[0]++;
//                }
                boolean standard=false;
                if('"'==str[point[0]]){
                    point[0]++;
                    standard=true;
                }

                StringBuffer fieldBuffer = new StringBuffer();
                while (point[0] < point[1] && Pattern.matches("[\\w\\d_]", str[point[0]] + ""))
                {
                    fieldBuffer.append(str[point[0]++]);
                }
                field = fieldBuffer.toString();

                if(standard) {
                    if('"'!=str[point[0]]){
                        throw new IllegalArgumentException(new String(str,0,point[0]));
                    }
                    point[0]++;
                }

                parseString(str,point,Void.class);

//                while (point[0] < point[1]&&str[point[0]] != ':')
//                    point[0]++;
                if(':'!=str[point[0]]){
                    throw new IllegalArgumentException(new String(str,0,point[0]));
                }

                point[0]++;
                parseString(str,point,Void.class);
                int last=point[0];
                while (point[0] < point[1]&&str[point[0]]!='}'&& '，' != str[point[0]]&& ',' != str[point[0]]&&last==point[0]) {
                    last=point[0];
//                    parseString(str,point,Void.class);
                    if (str[point[0]] == '"') {
                        root.put(field, parseString(str, point, String.class));
                        continue;
                    } else if ('{' == str[point[0]]) {
                        root.put(field, parseString(str, point, Map.class));
                        continue;
                    } else if ('0' <= str[point[0]] && '9' >= str[point[0]]) {
                        root.put(field,typeNumber(field, parseString(str, point, BigDecimal.class)));
                        continue;
                    } else if ('[' == str[point[0]]) {
                        root.put(field, parseString(str, point, List.class));
                        continue;
                    }
//                    else if('，' == str[point[0]]||',' == str[point[0]]||last!=point[0]){
//                        break;
//                    }

                    throw new IllegalArgumentException(new String(str,0,point[0]));

                }

                if('，' == str[point[0]]||',' == str[point[0]]){
                    point[0]++;
                    parseString(str,point,Void.class);
                }
            }

            if('}'!=str[point[0]]){
                throw new IllegalArgumentException(new String(str,0,point[0]));
            }

            point[0]++;
            parseString(str,point,Void.class);
            return (T) root;
        }
        if (type == List.class) {
            parseString(str,point,Void.class);

            if('['!=str[point[0]]){
                throw new IllegalArgumentException(new String(str,0,point[0]));
            }
            point[0]++;

            List objectList = new ArrayList();
            while (point[0] < point[1] && str[point[0]] != ']') {
                parseString(str,point,Void.class);

                if (str[point[0]] == '"') {
                    objectList.add( parseString(str, point, String.class));
                    continue;
                } else if ('{' == str[point[0]]) {
                    objectList.add( parseString(str, point, Map.class));
                    continue;
                } else if ('0' <= str[point[0]] && '9' >= str[point[0]]) {
                    objectList.add(parseString(str, point, BigDecimal.class));
                    continue;
                } else if ('[' == str[point[0]]) {
                    objectList.add( parseString(str, point, List.class));
                    continue;
                }
                if(',' == str[point[0]]||',' == str[point[0]]){
                    point[0]++;
                    continue;
                }
                throw new IllegalArgumentException(new String(str,0,point[0]));
            }
            if(']'!=str[point[0]]){
                throw new IllegalArgumentException(new String(str,0,point[0]));
            }
            point[0]++;
            parseString(str,point,Void.class);
            return (T) objectList;
        }else if(type==Void.class){
            while (point[0]<point[1]&&Pattern.matches("\\s", str[point[0]] + "")) point[0]++;
            if(point[0]<point[1]-1&&str[point[0]]=='/'&&str[point[0]+1]=='/'){
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
