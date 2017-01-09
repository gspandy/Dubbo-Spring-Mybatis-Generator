package com.dafy.dev;

import com.dafy.dev.codegen.ClassLoaderUtil;
import com.dafy.dev.pojo.GlobalConfig;
import com.dafy.dev.pojo.TableInfo;
import com.dafy.dev.util.FileUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by chunxiaoli on 12/27/16.
 */
public class GeneratorContext {

    public static ClassLoader  classLoader;

    public static String utilPackage;

    public static List<String> getTableList(GlobalConfig globalConfig){
        List<TableInfo> list= globalConfig.getMybatisConfig().getTableInfoList();
        return list!=null?list.stream().map(TableInfo::getTableName).collect(Collectors.toList()):null;
    }

    public static String getGroupId(GlobalConfig globalConfig){
        return globalConfig.getGroupId();
    }

    @Override
    public String toString() {
        return "GeneratorContext{}";
    }

    public static void clear() {
        FileUtil.delete(ClassLoaderUtil.tmp);
    }
}
