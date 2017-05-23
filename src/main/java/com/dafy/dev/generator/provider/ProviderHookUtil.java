package com.dafy.dev.generator.provider;

import com.dafy.dev.config.JavaFileConfig;
import com.dafy.dev.config.provider.ProviderConfig;
import com.dafy.dev.generator.common.JavaFileGenerator;
import com.dafy.dev.generator.maven.MavenDirUtil;
import com.dafy.dev.util.SourceCodeUtil;

/**
 * Created by chunxiaoli on 5/23/17.
 */
public class ProviderHookUtil {
    public static void createProviderHookFile(ProviderConfig config) {
        JavaFileConfig cfg = new JavaFileConfig();
        cfg.setJavaFileDoc("Hook");
        cfg.setClassName(getProviderHookClassName(config));
        cfg.setPackageName(ProviderUtil.getProviderPackage(config));
        cfg.setOutDir(MavenDirUtil.getMavenSourceCodeDir(ProviderUtil.getProviderDir(config)));
        new JavaFileGenerator(cfg).generateProviderHookFile();
    }

    private static String getProviderHookClassName(ProviderConfig config) {
        return SourceCodeUtil.covertClassName(config.getName() + "ProviderHook");
    }

    public static String getProviderHookFullName(ProviderConfig config) {
        return ProviderUtil.getProviderPackage(config) + "." + getProviderHookClassName(config);
    }

}
