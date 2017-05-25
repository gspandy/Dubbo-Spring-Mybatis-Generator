package com.dafy.dev.provider;

import com.dafy.dev.codegen.ClassLoaderUtil;
import com.dafy.dev.config.ConfigDefault;
import com.dafy.dev.config.ConfigGenerator;
import com.dafy.dev.config.GlobalConfig;
import com.dafy.dev.config.MybatisConfig;
import com.dafy.dev.config.provider.ProviderConfig;
import com.dafy.dev.generator.provider.ApplicationPropertiesUtil;
import com.dafy.dev.generator.provider.DaoUtil;
import com.dafy.dev.generator.provider.DtoUtil;
import com.dafy.dev.generator.provider.MybatisUtil;
import com.dafy.dev.generator.provider.ProviderHookUtil;
import com.dafy.dev.generator.provider.ProviderUtil;
import com.dafy.dev.generator.provider.ServiceUtil;
import com.dafy.dev.generator.provider.SpringBootAutoUtil;
import com.dafy.dev.pojo.TableInfo;
import com.dafy.dev.util.ConfigUtil;
import com.dafy.dev.util.ObjectConvertUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by chunxiaoli on 5/23/17.
 */
public class ProviderTest {
    private static final Logger logger = LoggerFactory.getLogger(ProviderTest.class);

    static ProviderConfig config = new ProviderConfig();
    static GlobalConfig globalConfig;

    static MybatisConfig mybatisConfig=new MybatisConfig();

    static {


        try {
            final InputStream file =
                    new FileInputStream(
                            "/Users/chunxiaoli/IdeaProjects/Dubbo-Spring-Mybatis-Generator/test.yaml");
            globalConfig = ObjectConvertUtil
                    .merge(ConfigGenerator.getDefaultGloabal(), ConfigUtil.parseConfig(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        config.setDir(".");
        config.setName("test");
        config.setGroupId("com.chunxiaoli.abc");
        config.setApiDirName("api");
        config.setProviderDirName("provider");
        config.setMapperXmlFilePost("mapper");
        config.setSpringAutoConfigFullClassPath(ConfigDefault.SPRING_AUTO_CONFIG_FULLCLASSPATH);
        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName("t_task");
        config.setMybatisConfig(ConfigGenerator.generateMybatisConfig(globalConfig.getMybatisConfig(), tableInfo,
                ProviderUtil.getProviderPackage(config)));

        logger.info("{}",config);
        logger.info("{}",ConfigGenerator.getDefault());
        config = ObjectConvertUtil.merge(config, ConfigGenerator.getDefault());
        logger.info("{}",config);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.convertValue(config,Map.class);
        logger.info("----config----:{}",map);

    }

    @Before
    public void init() {
        ProviderUtil.createDir(config);
    }

    @Test
    public void testProviderGenerator() {

    }

    @Test
    public void testApplicationConfigUtil() {
        ApplicationPropertiesUtil.createApplicationPropertiesFile(config);
    }

    @Test
    public void testProviderHookUtil() {
        ProviderHookUtil.createProviderHookFile(config);
    }

    @Test
    public void testSpringBootAutoUtil() {
        SpringBootAutoUtil.createSpringBootAutoConfigFiles(ProviderUtil.getProviderDir(config),
                config.getSpringAutoConfigFullClassPath());
    }

    @Test
    public void testMybatisUtil() {
        MybatisUtil.createMybatisPropertiesFile(config);
    }


    @Test
    public void createOrm() throws Exception {
        MybatisUtil.createOrm(config, config.getName(), ProviderUtil.getProviderDir(config));
    }

    @Test
    public void createDaoFiles() throws Exception {
        MybatisUtil.createOrm(config, config.getName(), ProviderUtil.getProviderDir(config));
        ClassLoader classLoader = ClassLoaderUtil.loadAllClass(config.getDir());
        DaoUtil.createDaoFiles(config,MybatisUtil.getTableList(config),classLoader);
    }

    @Test
    public void createDtoFromPojo() throws Exception {
        MybatisUtil.createOrm(config, config.getName(), ProviderUtil.getProviderDir(config));
        ClassLoader classLoader = ClassLoaderUtil.loadAllClass(config.getDir());
        DtoUtil.createDtoFromPojo(config,classLoader);
    }

    @Test
    public void createUtilFiles() throws Exception {
        ClassLoader classLoader = ClassLoaderUtil.loadAllClass(config.getDir());
        DtoUtil.createUtilFiles(config,classLoader,MybatisUtil.getTableList(config));
    }

    @Test
    public void createService() throws Exception {
        MybatisUtil.createOrm(config, config.getName(), ProviderUtil.getProviderDir(config));
        ClassLoader classLoader = ClassLoaderUtil.loadAllClass(config.getDir());
        DaoUtil.createDaoFiles(config,MybatisUtil.getTableList(config),classLoader);
        DtoUtil.createDtoFromPojo(config,classLoader);
        classLoader = ClassLoaderUtil.loadAllClass(config.getDir());
        DtoUtil.createUtilFiles(config,classLoader,MybatisUtil.getTableList(config));
        classLoader = ClassLoaderUtil.loadAllClass(config.getDir());
        ServiceUtil.createService(config,classLoader);
    }
}
