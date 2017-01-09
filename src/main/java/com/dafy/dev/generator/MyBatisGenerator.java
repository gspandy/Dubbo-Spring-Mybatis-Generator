package com.dafy.dev.generator;

import com.dafy.dev.config.MybatisConfig;
import com.dafy.dev.mybatis.DatabaseConfig;
import com.dafy.dev.mybatis.GeneratorConfig;
import com.dafy.dev.mybatis.MybatisGeneratorBridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chunxiaoli on 10/18/16.
 */
public class MyBatisGenerator {

    private final Logger logger= LoggerFactory.getLogger(MyBatisGenerator.class);

    private MybatisConfig mybatisConfig;

    public MyBatisGenerator(MybatisConfig config){
        this.mybatisConfig=config;
    }

    public void generate(){
        GeneratorConfig generatorConfig = new GeneratorConfig();


        generatorConfig.setProjectFolder(this.mybatisConfig.getProjectDir());

        generatorConfig.setComment(true);
        //generatorConfig.setOffsetLimit(true);
        generatorConfig.setTableName(this.mybatisConfig.getTable());
        generatorConfig.setDomainObjectName(this.mybatisConfig.getDomainObjectName());

        generatorConfig.setModelPackageTargetFolder(this.mybatisConfig.getModelOutputDir());
        generatorConfig.setMappingXMLTargetFolder(this.mybatisConfig.getMapperXMLOutputDir());
        generatorConfig.setDaoTargetFolder(this.mybatisConfig.getMapperOutputDir());

        generatorConfig.setDaoPackage(this.mybatisConfig.getMapperPackage());
        generatorConfig.setMappingXMLPackage(this.mybatisConfig.getMapperXMLPackage());
        generatorConfig.setModelPackage(this.mybatisConfig.getModelPackage());

        //generatorConfig.setConnectorJarPath(this.mybatisConfig.getConnectorJarPath());

        DatabaseConfig databaseConfig=new DatabaseConfig();

        MybatisGeneratorBridge bridge = new MybatisGeneratorBridge();
        bridge.setGeneratorConfig(generatorConfig);

        databaseConfig.setDbType(this.mybatisConfig.getDbType());
        databaseConfig.setHost(this.mybatisConfig.getHost());
        databaseConfig.setPort(this.mybatisConfig.getPort());
        databaseConfig.setUsername(this.mybatisConfig.getUsername());
        databaseConfig.setPassword(this.mybatisConfig.getPassword());
        databaseConfig.setSchema(this.mybatisConfig.getDatabase());
        databaseConfig.setEncoding(this.mybatisConfig.getEncoding());

        bridge.setDatabaseConfig(databaseConfig);

        try {
            bridge.generate();
        } catch (Exception e) {
            logger.debug("e:{}",e.getMessage());
            e.printStackTrace();
        }
    }
}
