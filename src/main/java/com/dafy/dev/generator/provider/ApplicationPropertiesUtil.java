package com.dafy.dev.generator.provider;

import com.dafy.dev.config.provider.ProviderConfig;
import com.dafy.dev.generator.maven.MavenDirUtil;
import com.dafy.dev.util.FileUtil;
import com.dafy.dev.util.StringUtil;

import java.io.File;
import java.io.InputStream;

/**
 * Created by chunxiaoli on 5/23/17.
 */
public class ApplicationPropertiesUtil {

    //生成appplication.properties
    public static void createApplicationPropertiesFile(ProviderConfig config) {

        String templatePath = config.getApplicationPropertiesTemplatePath();
        String base = MavenDirUtil.getResourceBaseDir(ProviderUtil.getProviderDir(config));
        String path = base + File.separator + ProviderUtil.getResourceConfigDir(config) +
                File.separator + "application.properties";
        if (!StringUtil.isEmpty(templatePath)) {
            FileUtil.copy(templatePath, path);
        } else {
            InputStream inputStream = ApplicationPropertiesUtil.class.getClassLoader().
                    getResourceAsStream("template/application.properties");
            FileUtil.save(inputStream, path);
        }

    }
}
