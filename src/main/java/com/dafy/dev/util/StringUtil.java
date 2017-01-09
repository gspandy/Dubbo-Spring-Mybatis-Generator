package com.dafy.dev.util;

/**
 * Created by chunxiaoli on 10/13/16.
 */
public class StringUtil {
    public static String captize(String s){
        String first=s.substring(0,1);
        return first.toUpperCase()+s.substring(1,s.length());
    }

    public static boolean isEmpty(String s){
        return s==null||s.isEmpty();
    }


}
