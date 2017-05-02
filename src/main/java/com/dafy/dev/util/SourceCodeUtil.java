package com.dafy.dev.util;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chunxiaoli on 10/13/16.
 */
public class SourceCodeUtil {
    public static boolean createService() {
        return true;
    }

    public static Map<String, Object> parseServiceInterface(InputStream inputStream) {
        Yaml yaml = new Yaml();
        return (Map<String, Object>) yaml
                .load(inputStream);
    }

    public static String covertFieldName(String input){
        String first = input.substring(0, 1).toLowerCase();
        return first+input.substring(1,input.length());
    }

    public static String covertClassName(String input){
        return getFirstUppercase(input);
    }


    /**
     * 转换成员变量为驼峰形式
     *
     * @param input
     * @return
     */
    public static String convertFieldUppercase(String input) {
        return uppercase(input, true);
    }

    public static String convertMethodUppercase(String input) {
        return uppercase(input, false);
    }


    /**
     * 转换下划线成驼峰形式 user_name=>userName|user_name=>UserName
     *
     * @param input          输入的字符串
     * @param firstLowerCase //首字母是否小写
     * @return
     */
    public static String uppercase(String input, boolean firstLowerCase) {

        String ret = "";


        String parts[] = input.split("_");

        if (parts.length > 1) {
            for (int i = 0; i < parts.length; i++) {
                String w = "";
                String first = (i == 0 && firstLowerCase) ? parts[i].substring(0, 1) : parts[i].substring(0, 1).toUpperCase();
                w = first;
                if (parts[i].length() > 1) {
                    w += parts[i].substring(1, parts[i].length());
                }
                ret += w;
            }
        } else {//只有一个单词
            if (!firstLowerCase) {
                ret = getFirstUppercase(input);
            } else {
                ret = input;
            }
        }

        return ret;
    }

    public static String getFirstUppercase(String input) {

        String firstLetterToUpperCase = input.substring(0, 1).toUpperCase();
        if (input.length() > 1) {
            firstLetterToUpperCase += input.substring(1, input.length());
        }
        return firstLetterToUpperCase;
    }

    //按驼峰分词
    public static List<String> partipation(String s){
        List<String>parts=new ArrayList<>();
        List<Integer> indexs=new ArrayList<>();
        for (int i=0;i<s.length();i++){
            char c= s.charAt(i);
            boolean up=Character.isUpperCase(c);
            if(i>0&&up){
                indexs.add(i);
            }
        }
        if(indexs.size()>0){
            int start=0;
            for(int j=0;j<indexs.size();j++){
                int index=indexs.get(j);
                String sub=s.substring(start,index);
                parts.add(sub);
                start=index;
            }
            String sub=s.substring(start,s.length());
            parts.add(sub);
        }else {
            parts.add(s);
        }
        return parts;
    }

    public static String upperCaseToJson(String s){
        List<String> parts=partipation(s);
        String ret="";
        for (int i=0;i<parts.size();i++){
            String word=parts.get(i);
            ret+=(word.toLowerCase()+(i==parts.size()-1?"":"_"));
        }
        return ret;
    }

    public static String jsontoUpperCase(String s){
        return uppercase(s,false);
    }

    public static String convertPackage2Dir(String packageName) {
        return packageName.replace(".", File.separator);
    }

    public static String covertToConstants(String input){
        String first = input.substring(0, 1).toLowerCase();
        return first+input.substring(1,input.length());
    }

    public static String getClassName(String fullClassName) {
        int idx = fullClassName.lastIndexOf(".");
        return fullClassName.substring(idx + 1, fullClassName.length());
    }

    public static String getPackageName(String fullClassName) {
        int idx = fullClassName.lastIndexOf(".");
        return fullClassName.substring(0, idx);
    }

    public static String getGetterName(String fieldName){
        String prefix="get";
        return prefix+ SourceCodeUtil.convertMethodUppercase(fieldName);
    }

    public static String getSetterName(String fieldName){
        return "set"+SourceCodeUtil.convertMethodUppercase(fieldName);
    }


}
