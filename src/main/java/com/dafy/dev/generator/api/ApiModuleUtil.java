package com.dafy.dev.generator.api;

import com.dafy.dev.config.api.ApiConfig;
import com.dafy.dev.generator.CommonGenerator;
import com.dafy.dev.generator.maven.MavenDirUtil;
import com.dafy.dev.util.SourceCodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by chunxiaoli on 5/19/17.
 */
public class ApiModuleUtil {

    private static final Logger logger = LoggerFactory.getLogger(ApiModuleUtil.class);

    public static String getApiDir(ApiConfig config) {
        String api = config.getDir() + File.separator + getApiModuleName(config);
        logger.info("apiDir:{}", api);
        return api;
    }

    public static String getApiModuleName(ApiConfig config) {
        return config.getName() + "-" + config.getApiDirName();
    }

    public static String getApiBaseDir(ApiConfig config) {
        return MavenDirUtil.getMavenSourceCodeDir(getApiDir(config));
    }

    public static String getApiSourceCodeDir(ApiConfig config) {
        String dir = MavenDirUtil.getSourceCodeBaseDirOfGroup(getApiDir(config),config.getGroupId(),config.getName()) + File.separator
                + config.getApiDirName();
        logger.info("ApiSourceCodeDir:{}", dir);
        return dir;
    }

    public static String getApiPackage(ApiConfig config) {
        return CommonGenerator.getPackageName(config,config.getName()) + "." + config.getDir();
    }

    public static String getApiDtoDir(ApiConfig config) {
        return getApiBaseDir(config) + File.separator + config.getDtoDirName() + File.separator
                + "response";
    }

    public static String getApiDtoPackage(ApiConfig config) {
        return getApiPackage(config) + "." + config.getDtoDirName();
    }

    public static String getApiServicePackage(ApiConfig config) {
        return getApiPackage(config);
    }

    private static String getServiceName(ApiConfig config) {
        return SourceCodeUtil.covertClassName(config.getName() + SourceCodeUtil.getFirstUppercase(
                config.getServiceFilePost()));
    }



    public static String getServiceFullClassName(ApiConfig config) {
        return ApiModuleUtil.getApiPackage(config) + "." + getServiceName(config);
    }

    public static String getServicePath(ApiConfig config) {
        return MavenDirUtil.getMavenSourceCodeDir(ApiModuleUtil.getApiDir(config)) + File.separator
                + SourceCodeUtil.convertPackage2Dir(getServiceFullClassName(config)) + ".java";
    }




}
