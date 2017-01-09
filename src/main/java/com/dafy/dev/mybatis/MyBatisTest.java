package com.dafy.dev.mybatis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyBatisTest{

    private static final Logger logger = LoggerFactory.getLogger(MyBatisTest.class);




    public static void main(String args[]){
        new MyBatisTest().generateCode();
    }


    public void generateCode() {

        GeneratorConfig generatorConfig = new GeneratorConfig();


        generatorConfig.setProjectFolder("src/main/java");

        generatorConfig.setComment(true);
        //generatorConfig.setOffsetLimit(true);
        generatorConfig.setTableName("t_task");

        generatorConfig.setModelPackageTargetFolder("./");
        generatorConfig.setMappingXMLTargetFolder("./");
        generatorConfig.setDaoTargetFolder("./");

        generatorConfig.setDaoPackage("com.dafy.dev.dao");
        generatorConfig.setMappingXMLPackage("com.dafy.dev.mapper");
        generatorConfig.setModelPackage("com.dafy.dev.pojo");

        generatorConfig.setConnectorJarPath("mysql-connector-java-5.1.36.jar");

        DatabaseConfig databaseConfig=new DatabaseConfig();

        MybatisGeneratorBridge bridge = new MybatisGeneratorBridge();
        bridge.setGeneratorConfig(generatorConfig);
        databaseConfig.setDbType("MySQL");
        databaseConfig.setHost("10.8.15.15");
        databaseConfig.setPort("3306");
        databaseConfig.setUsername("root");
        databaseConfig.setPassword("root");
        databaseConfig.setSchema("collection360");
        databaseConfig.setEncoding("utf-8");
        bridge.setDatabaseConfig(databaseConfig);

        try {
            bridge.generate();
        } catch (Exception e) {
           logger.debug("e:{}",e.getMessage());
           e.printStackTrace();
        }
    }


}
