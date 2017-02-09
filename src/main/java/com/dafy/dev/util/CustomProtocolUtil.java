package com.dafy.dev.util;

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
    public static String parseProtocol(InputStream inputStream, OutputStream outputStream) throws IOException {
        if(inputStream==null||outputStream==null){
            throw new IllegalArgumentException("stream can not be null!");
        }
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));

        Map root=new HashMap();
        root.put("cgi_prefix","nothing");
        List controller=new LinkedList();
        root.put("controller_list",controller);

        Map controllerItem=new HashMap();
        controller.add(controllerItem);
        controllerItem.put("name","nothing");
        List cgiList=new LinkedList();
        controllerItem.put("cgi_list",cgiList);

        String module=bufferedReader.readLine();
        if(module!=null){
            Matcher matcher=Pattern.compile("\\w+").matcher(module);
            if(matcher.find()){
                module=matcher.group(0);
            }else {
                module=null;
            }
        }
        for(String line=bufferedReader.readLine();line!=null;line=bufferedReader.readLine()){
            if(!line.startsWith("CGI")) continue;

            Map cgiItem=new HashMap();
            cgiList.add(cgiItem);
            cgiItem.put("desc","nothing");
            cgiItem.put("cgi",line.substring(4,line.length()).trim());
            for(line=bufferedReader.readLine();line!=null&&!line.startsWith("请求");line=bufferedReader.readLine());

            StringBuffer stringBuffer=new StringBuffer();
            for(line=bufferedReader.readLine();line!=null&&!line.startsWith("返回");line=bufferedReader.readLine()){
                stringBuffer.append(line).append("\n");
            }
            cgiItem.put("request",parseString(stringBuffer.toString().toCharArray()));
            stringBuffer=new StringBuffer();
            for(line=bufferedReader.readLine();line!=null&&!line.startsWith("#");line=bufferedReader.readLine()){
                stringBuffer.append(line).append("\n");
            }
            cgiItem.put("response",parseString(stringBuffer.toString().toCharArray()));
        }

        outputStream.write(new ObjectMapper().writeValueAsBytes(root));
        outputStream.flush();

        return module;
    }
    public static Map<String, Map> parseString(char[] str) {
        return parseString(str, new int[]{0, str.length}, Map.class);
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
                while (str[point[0]] != ':')
                    point[0]++;
                point[0]++;
                while (point[0] < point[1]&&str[point[0]]!='}') {
                    parseString(str,point,Void.class);
                    if (str[point[0]] == '"') {
                        point[0]++;
                        root.put(field, parseString(str, point, String.class));
                        continue;
                    } else if ('{' == str[point[0]]) {
                        root.put(field, parseString(str, point, Map.class));
                        continue;
                    } else if ('0' <= str[point[0]] && '9' >= str[point[0]]) {
                        root.put(field, parseString(str, point, BigDecimal.class));
                        continue;
                    } else if ('[' == str[point[0]]) {
                        point[0]++;
                        root.put(field, parseString(str, point, List.class));
                        continue;
                    }else  if (',' == str[point[0]]) {
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
}
