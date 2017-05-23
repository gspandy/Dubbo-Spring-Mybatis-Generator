package com.dafy.dev.generator.provider;

import com.dafy.dev.config.provider.ProviderConfig;
import com.dafy.dev.generator.CommonGenerator;
import com.dafy.dev.generator.maven.MavenDirUtil;
import com.dafy.dev.util.SourceCodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by chunxiaoli on 5/19/17.
 */
public class ProviderUtil {

    private static final Logger logger = LoggerFactory.getLogger(ProviderUtil.class);

    public static String getProviderDir(ProviderConfig config) {
        String provider =
                config.getDir() + File.separator + getProviderModuleName(config);
        logger.info("ProviderDir:{}", provider);
        return provider;
    }

    public static  String getProviderModuleName(ProviderConfig config) {
        return config.getName() + "-" + config.getProviderDirName();
    }

    public static  String getProviderSourceCodeDir(ProviderConfig config) {
        return MavenDirUtil.getSourceCodeBaseDirOfGroup(getProviderDir(config),config.getGroupId(),config.getName())
                + File.separator + config.getProviderDirName();
    }

    public static String getProviderPackage(ProviderConfig config) {
        return CommonGenerator.getPackageName(config,config.getName()) + "." + config
                .getProviderDirName();
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

    /*private String getDtoDir() {
        return SourceCodeUtil.convertPackage2Dir(getDtoPackage());
    }

    private String getDtoPackage(ProviderConfig config) {
        return getApiPackage() + "." + config.getDtoDirName();
    }*/
}
