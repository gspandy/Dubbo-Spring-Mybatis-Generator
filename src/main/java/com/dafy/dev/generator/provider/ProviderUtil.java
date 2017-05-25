package com.dafy.dev.generator.provider;

import com.dafy.dev.config.Config;
import com.dafy.dev.config.ConfigDefault;
import com.dafy.dev.config.provider.ProviderConfig;
import com.dafy.dev.generator.CommonGenerator;
import com.dafy.dev.generator.api.ApiModuleUtil;
import com.dafy.dev.generator.maven.MavenDirUtil;
import com.dafy.dev.util.FileUtil;
import com.dafy.dev.util.SourceCodeUtil;
import com.dafy.dev.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by chunxiaoli on 5/19/17.
 */
public class ProviderUtil {

    private static final Logger logger = LoggerFactory.getLogger(ProviderUtil.class);


    public static void createDir(ProviderConfig config){
        String providerDir = ProviderUtil.getProviderDir(config);
        FileUtil.createDir(providerDir);

        MavenDirUtil.createModuleMavenStructure(providerDir,config.getGroupId(),config.getName());

        for (String sub : Config.PROVIDER_SUB_DIRS) {
            String subDir = ProviderUtil.getProviderSourceCodeDir(config) + File.separator + sub;
            FileUtil.createDir(subDir);
        }
    }

    public static String getProviderDir(ProviderConfig config) {
        String provider = config.getDir() + File.separator + getProviderModuleName(config);
        logger.info("ProviderDir:{}", provider);
        return provider;
    }


    public static String getResourceConfigDir(ProviderConfig config) {
        return (StringUtil.isEmpty(config.getConfigDirName())?
                ConfigDefault.GLOBAL_DIR_CONFIG:config.getConfigDirName());
    }

    public static  String getProviderModuleName(ProviderConfig config) {
        return config.getName() + "-" + getProviderDirName(config);
    }

    public static  String getProviderSourceCodeDir(ProviderConfig config) {
        return MavenDirUtil.getSourceCodeBaseDirOfGroup(getProviderDir(config),config.getGroupId(),config.getName())
                + File.separator + getProviderDirName(config);
    }

    public static String getProviderPackage(ProviderConfig config) {
        return CommonGenerator.getPackageName(config,config.getName()) + "." + getProviderDirName(config);
    }

    public static String getProviderOrmDir(ProviderConfig config) {
        return getProviderBaseDir(config) + SourceCodeUtil.convertPackage2Dir(getOrmPackage(config));
    }

    public static String getProviderBaseDir(ProviderConfig config) {
        return MavenDirUtil.getMavenSourceCodeDir(getProviderDir(config));
    }

    public static String getOrmPackage(ProviderConfig config) {
        return getProviderPackage(config) + "." + config.getOrmDirName();
    }

    public static String getProviderUtilPackage(ProviderConfig config) {
        return getProviderPackage(config) + ".util";
    }

    public static String getProviderPojoDir(ProviderConfig config) {
        return getProviderBaseDir(config) + SourceCodeUtil.convertPackage2Dir(getPojoPackage(config));
    }


    public static String getDaoPackage(ProviderConfig config) {
        return getProviderPackage(config) + "." + config.getDaoDirName();
    }

    public static String getPojoPackage(ProviderConfig config) {
        return getProviderPackage(config) + "." + config.getPojoDirName();
    }

    public static String getMapperXmlName(ProviderConfig config,String tableName) {
        String mapperPostName = SourceCodeUtil
                .getFirstUppercase(config.getMapperXmlFilePost());
        return SourceCodeUtil.uppercase(tableName, false) + mapperPostName + ".xml";
    }

    private static String getProviderDirName(ProviderConfig config){
        return (StringUtil.isEmpty(config.getProviderDirName())?
                ConfigDefault.GLOBAL_DIR_PROVIDER:config.getProviderDirName());
    }

    public static String getProviderDaoDir(ProviderConfig config) {
        return getProviderBaseDir(config) + SourceCodeUtil.convertPackage2Dir(getDaoPackage(config));
    }

    public static String getServiceName(ProviderConfig config) {
        return SourceCodeUtil.covertClassName(config.getName() + SourceCodeUtil.getFirstUppercase(
                config.getServiceFilePost()));
    }

    private String getDtoDir(ProviderConfig config) {
        return SourceCodeUtil.convertPackage2Dir(getDtoPackage(config));
    }

    public static String getDtoPackage(ProviderConfig config) {
        return ApiModuleUtil.getApiPackage(config) + "." + config.getDtoDirName();
    }

}
