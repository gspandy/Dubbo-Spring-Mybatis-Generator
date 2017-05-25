package com.dafy.dev.project;

import com.dafy.dev.config.ConfigGenerator;
import com.dafy.dev.config.GlobalConfig;
import com.dafy.dev.config.ProjectConfig;
import com.dafy.dev.generator.ProjectGenerator;
import com.dafy.dev.generator.dubbo.DubboRPCProjectGenerator;
import com.dafy.dev.util.ObjectConvertUtil;
import org.junit.Test;

/**
 * Created by chunxiaoli on 5/9/17.
 */
public class ProjectGeneratorTest {

    final static   ProjectConfig projectConfig = new ProjectConfig();
    final static   ProjectGenerator projectGenerator = new ProjectGenerator();

    static {
        projectConfig.setDir("");
        projectConfig.setDir("abc");
        projectGenerator.setConfig(projectConfig);
    }

    @Test
    public void testProjectGenerator(){
        projectGenerator.getProjectDir();
    }

    @Test
    public void testCreateApi(){
        GlobalConfig config = new GlobalConfig();
        config.setDir("./");
        config.setName("tt");
        config.setPojoDirName("a");
        config.setGroupId("com.chunxiao.hh");

        GlobalConfig merge = ObjectConvertUtil.merge(config, ConfigGenerator.getDefaultGloabal());

        new DubboRPCProjectGenerator(merge).createApiDir();
        new DubboRPCProjectGenerator(merge).createProviderDir();
    }


}
