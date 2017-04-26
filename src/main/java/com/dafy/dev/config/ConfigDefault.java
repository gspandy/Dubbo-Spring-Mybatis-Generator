package com.dafy.dev.config;

import com.dafy.dev.config.dubbo.ApplicationConfig;
import com.dafy.dev.config.dubbo.ProtocolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chunxiaoli on 10/12/16.
 */
public class ConfigDefault {

    private static final Logger logger= LoggerFactory.getLogger(ConfigDefault.class);



    public static final String  GLOBAL_GROUP_ID="com.example.www";
    public static final String  GLOBAL_ARTIFACT_ID="test";
    public static final String  GLOBAL_VERSION="1.0.0-SNAPSHOT";

    public static final String  GLOBAL_ROOT_DIR="./TestProject";

    public static final String  GLOBAL_CONSTANTS_DIR="constants";
    public static final String  GLOBAL_CONTROLLER_DIR="controller";

    public static final String  GLOBAL_LOG_FILE="/opt/logs/debug.log";
    public static final String  GLOBAL_DIR_API="api";
    public static final String  GLOBAL_DIR_PROVIDER="provider";
    public static final String  GLOBAL_DIR_DTO="dto";
    public static final String  GLOBAL_DIR_DAO="dao";
    public static final String  GLOBAL_DIR_ORM="orm";
    public static final String  GLOBAL_DIR_POJO="pojo";
    public static final String  GLOBAL_DIR_MODEL="pojo";
    public static final String  GLOBAL_DIR_CONFIG="config";

    public static final String  GLOBAL_MAPPER_XML_FILE_POST="mapper";

    public static final String  GLOBAL_SERVICE_FILE_POST="service";

    public static final String  GLOBAL_DTO_REQ_SUFFIX="ReqDto";
    public static final String  GLOBAL_DTO_RES_SUFFIX="ResDto";


    public static final String  MYBATIS_DBTYPE="MySql";
    public static final String  MYBATIS_HOST="127.0.0.1";
    public static final String  MYBATIS_PORT="3306";
    public static final String  MYBATIS_USER="root";
    public static final String  MYBATIS_PASSWORD="";
    public static final String  MYBATIS_DATABASE="";
    public static final String  MYBATIS_ENCODING="utf-8";

    public static final String  MYBATIS_MODEL_OUTPUT_DIR="./java";
    public static final String  MYBATIS_MAPPER_XML_OUTPUT_DIR="./resources/mybatis";
    public static final String  MYBATIS_DAO_OUTPUT_DIR="./java";
    public static final String  MYBATIS_MAPPER_OUTPUT_DIR="./java";

    public static final String  MYBATIS_MAPPER_PACKAGE="./orm";
    public static final String  MYBATIS_DAO_PACKAGE="./dao";
    public static final String  MYBATIS_MAPPER_XML_PACKAGE="";
    public static final String  MYBATIS_MODEL_PACKAGE="./pojo";

    public static final String  POM_TEMPLATE_PROVIDER="template/template-provider-pom.xml";
    public static final String  POM_TEMPLATE_API="template/template-api-pom.xml";
    public static final String  POM_TEMPLATE_ROOT="template/template-root-pom.xml";

    public static final String  POM_TEMPLATE_WEB="template/template-web-server-pom.xml";
    public static final String  LOG_CONFIG_FILE="template/logback.xml";
    public static final String  LOG_CONFIG_DIR="";

    public static final String  SPRING_AUTO_CONFIG_FULLCLASSPATH="com.dafy.dev.config.MybatisAutoConfiguration";








    public static ProjectConfig getDefault(final String dir,final String project){
        return getDefault(dir,project,
                "com.dafy.onecollection",
                "/opt/logs/"+project+"/debug.log");
    }

    public static ProjectConfig getDefault(final String dir,final String project,String groupId,String logFile){
        final ProjectConfig config=new ProjectConfig();
        config.setGroupId(groupId);
        config.setArtifactId(project);
        config.setProjectName(project);
        config.setDirName(project);
        config.setDir(dir);
        config.setLogFile(logFile);
        return config;
    }

    public static PomConfig getDefaultPomConfig(ProjectConfig config,String dir,String module){
        PomConfig pomConfig=new PomConfig();
        pomConfig.setGroupId(config.getGroupId());
        pomConfig.setArtifactId(module);
        pomConfig.setVersion(config.getVersion());
        pomConfig.setProjectName(module);
        pomConfig.setDirName(module);
        pomConfig.setDir(dir);
        return pomConfig;
    }




    public static DubboConfig getDefaultDubboConfig(){
        final ApplicationConfig applicationConfig=new ApplicationConfig();
        final ProtocolConfig protocolConfig=new ProtocolConfig();
        applicationConfig.setName("demo");
        applicationConfig.setOwner("chunxiao");
        protocolConfig.setName("dubbo");
        protocolConfig.setPort("21812");

        final DubboConfig config=new DubboConfig();
        config.setApplicationConfig(applicationConfig);
        config.setProtocolConfig(protocolConfig);

        return config;
    }


}
