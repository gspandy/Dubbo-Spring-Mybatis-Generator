package com.dafy.dev.generator.provider;

import com.dafy.dev.config.PropertyConfig;
import com.dafy.dev.generator.common.PropertiesGenerator;
import com.dafy.dev.generator.maven.MavenDirUtil;
import com.dafy.dev.util.FileUtil;

import java.io.File;

/**
 * Created by chunxiaoli on 5/23/17.
 */
public class SpringBootAutoUtil {
    //spring auto config
    public static void createSpringBootAutoConfigFiles(String parentDir,String fullClassPath) {
        String base = MavenDirUtil.getResourceBaseDir(parentDir);

        FileUtil.createDir(base + File.separator + "META-INF");

        String path = base + File.separator + "META-INF" + File.separator + "spring.factories";
        PropertyConfig config = new PropertyConfig();
        config.setPath(path);

        PropertiesGenerator generator = new PropertiesGenerator(config);

        //todo configurable
        generator.set("org.springframework.boot.autoconfigure.EnableAutoConfiguration",fullClassPath);
        generator.generate();
    }
}
