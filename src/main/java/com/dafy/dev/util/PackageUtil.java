package com.dafy.dev.util;

import java.io.File;

/**
 * Created by chunxiaoli on 10/13/16.
 */
public class PackageUtil {
    //convert: com.dafy.dev to com/dafy/dev
    public static String convertPackage2Path(String packageName){
        if(packageName==null){
            return null;
        }
        String []arr=packageName.split("\\.");
        return String.join(File.separator,arr);

    }

    public static String getClassName(Class cls){
        return cls.getCanonicalName();
    }
}
