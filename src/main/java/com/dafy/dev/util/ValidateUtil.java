package com.dafy.dev.util;

import com.dafy.dev.pojo.GlobalConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chunxiaoli on 12/29/16.
 */
public class ValidateUtil {
    public static List<String> validateConfig(GlobalConfig config){
        List<String> msgs=new ArrayList<>();
        /*if(!FileUtil.checkPermission(config.getDir())){
            msgs.add(config.getDir()+" permission deny");
        }*/
        return msgs;
    }
}
