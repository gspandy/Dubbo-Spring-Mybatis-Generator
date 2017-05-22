package com.dafy.dev.generator.api;

import com.dafy.dev.config.api.ApiConfig;
import com.dafy.dev.generator.CommonGenerator;
import com.dafy.dev.generator.maven.MavenDirUtil;
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


}
