package com.dafy.dev.provider;

import com.dafy.dev.config.ConfigDefault;
import com.dafy.dev.config.provider.ProviderConfig;
import com.dafy.dev.generator.provider.ApplicationPropertiesUtil;
import com.dafy.dev.generator.provider.MybatisUtil;
import com.dafy.dev.generator.provider.ProviderHookUtil;
import com.dafy.dev.generator.provider.ProviderUtil;
import com.dafy.dev.generator.provider.SpringBootAutoUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by chunxiaoli on 5/23/17.
 */
public class ProviderTest {

    final static ProviderConfig config = new ProviderConfig();

    static {
        config.setDir(".");
        config.setName("test");
        config.setGroupId("com.chunxiaoli.abc");
        config.setApiDirName("api");
        config.setProviderDirName("provider");
        config.setSpringAutoConfigFullClassPath(ConfigDefault.SPRING_AUTO_CONFIG_FULLCLASSPATH);
        //config.setMybatisConfig(ConfigGenerator.generateMybatisConfig());
    }

    @Before
    public void init(){
        ProviderUtil.createDir(config);
    }


    @Test
    public void testProviderGenerator(){

    }

    @Test
    public void testApplicationConfigUtil(){
        ApplicationPropertiesUtil.createApplicationPropertiesFile(config);
    }

    @Test
    public void testProviderHookUtil(){
        ProviderHookUtil.createProviderHookFile(config);
    }

    @Test
    public void testSpringBootAutoUtil(){
        SpringBootAutoUtil.createSpringBootAutoConfigFiles(ProviderUtil.getProviderDir(config),
                config.getSpringAutoConfigFullClassPath());
    }

    @Test
    public void testMybatisUtil(){
        MybatisUtil.createMybatisPropertiesFile(config);
    }
}
