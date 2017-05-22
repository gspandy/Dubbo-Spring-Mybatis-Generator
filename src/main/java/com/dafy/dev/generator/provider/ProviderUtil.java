package com.dafy.dev.generator.provider;

import com.dafy.dev.config.provider.ProviderConfig;
import com.dafy.dev.generator.CommonGenerator;
import com.dafy.dev.generator.maven.MavenDirUtil;
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
}
