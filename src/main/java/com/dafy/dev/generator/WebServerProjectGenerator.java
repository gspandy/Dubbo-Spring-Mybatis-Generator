package com.dafy.dev.generator;

import com.dafy.dev.config.*;
import com.dafy.dev.pojo.CgiConfig;
import com.dafy.dev.pojo.CgiInfo;
import com.dafy.dev.pojo.ControllerConfig;
import com.dafy.dev.config.GlobalConfig;
import com.dafy.dev.util.FileUtil;
import com.dafy.dev.util.SourceCodeUtil;
import com.dafy.dev.util.StringUtil;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.System.exit;

/**
 * Created by chunxiaoli on 10/26/16.
 */
public class WebServerProjectGenerator implements Generator {

    private static final Logger logger = LoggerFactory.getLogger(ProjectGenerator.class);
    private ProjectGenerator projectGenerator;
    private String           dir;
    private String           project;
    private String           jsonFile;

    private GlobalConfig globalConfig;

    private CgiConfig cgiConfig;

    private DtoGenerator dtoGenerator;

    public WebServerProjectGenerator(String dir, String project, String jsonFile) {
        this.jsonFile = jsonFile;
        this.dir = dir + "-server";
        this.project = project;
        this.projectGenerator = new ProjectGenerator(ConfigDefault.getDefault(this.dir, project));
    }

    public WebServerProjectGenerator(GlobalConfig webConfig) {
        this.globalConfig = webConfig;
        this.jsonFile = webConfig.getCgiJsonFile();
        this.dir = getRootDir();
        this.project = webConfig.getName();
        this.projectGenerator = new ProjectGenerator(
                ConfigGenerator.generateWebProjectConfig(this.globalConfig));
        parseCgiConfig();
    }

    private void parseCgiConfig() {
        final ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        try {
            this.cgiConfig = mapper.readValue(new FileInputStream(this.jsonFile), CgiConfig.class);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("json file parse error :{} please check your :{}", e, this.jsonFile);
            exit(1);
        }
    }

    public void generate() {

        //生成根目录
        this.projectGenerator.createProjectDirStructure();

        FileUtil.createDir(getRootDir());

        String template = this.globalConfig.getWebPomTemplatePath();
        template = StringUtil.isEmpty(template) ? ConfigDefault.POM_TEMPLATE_WEB : template;

        //生成pom文件
        projectGenerator.createPomFile(template, getRootDir(),
                this.projectGenerator.getConfig().getProjectName());

        //创建maven结构
        projectGenerator.createMavenStructure(getRootDir());

        //生成子目录
        createSubDirs(getRootDir());

        createApplicationPropertiesFile(getRootDir());

        //生成launcher文件
        projectGenerator.createWebLauncherFile(getRootDir(),
                this.projectGenerator.getConfig().getProjectName(),
                this.projectGenerator.getConfig().getProjectName());
        //生成dto
        createDtoFromJson();

        String file = createCommonFiles();

        createController(file);

        createLogConfig();
    }

    //返回根目录
    private String getRootDir() {
        return ConfigGenerator.getWebRootDir(this.globalConfig);
    }

    //创建目录结构
    private void createSubDirs(String parentDir) {
        for (String sub : Config.WEB_SERVER_DIRS) {
            String subDir = projectGenerator.getSourceCodeBaseDir(parentDir,
                    this.projectGenerator.getConfig().getProjectName())
                    + File.separator + sub;
            FileUtil.createDir(subDir);
        }
    }

    //根据json协议 生成 dto
    public void createDtoFromJson() {
        DtoConfig dtoConfig = new DtoConfig();
        dtoConfig.setReqDtoNameSuffix(this.globalConfig.getReqDtoNameSuffix());
        dtoConfig.setResDtoNameSuffix(this.globalConfig.getResDtoNameSuffix());
        dtoConfig.setDir(projectGenerator.getMavenSourceCodeDir(dir));
        String packageName = projectGenerator.getBasePackage(project);

        logger.debug("createDtoFromJson:{}", packageName);
        dtoConfig.setPackageName(packageName);
        dtoConfig.setJsonConfigPath(this.jsonFile);
        dtoGenerator = new DtoGenerator(dtoConfig);
        dtoGenerator.generate();
    }



    private List<String> getUrls() {
        return this.cgiConfig.getControllerConfigList().stream().map(ControllerConfig::getCgiInfoList).
                collect(Collectors.toList()).stream().flatMap(Collection::stream).
                collect(Collectors.toList()).stream().map(CgiInfo::getCgi).collect(Collectors.toList());
    }



    //生成appplication.properties
    private void createApplicationPropertiesFile(String parentDir) {
        String base = projectGenerator.getResourceBaseDir(parentDir);
        String path = base + File.separator + "config" + File.separator + "application.properties";

        PropertyConfig config = new PropertyConfig();
        config.setPath(path);

        PropertiesGenerator generator = new PropertiesGenerator(config);

        generator.set("logging.file",
                this.projectGenerator.getConfig().getLogFile() + this.project + ".log");
        generator.set("spring.profiles.active", "test");
        generator.set("spring.dao.exceptiontranslation.enabled", "false");
        generator.set("server.port", "5000");
        generator.generate();

    }

    private void createController(String configClass) {
        /*this.projectGenerator.createControllerFile(getRootDir(),
                getSubPackage(this.globalConfig.getControllerDir()),
                this.projectGenerator.getConfig().getName() + "Controller", getUrls(),
                configClass);*/




        cgiConfig.getControllerConfigList().forEach(item->{

            String className =!StringUtil.isEmpty(item.getName())?
            SourceCodeUtil.covertClassName(item.getName())+"Controller"
            :this.projectGenerator.getConfig().getProjectName() + "Controller";

            String packageNamePrefix = getSubPackage(this.globalConfig.getControllerDir());

            JavaFileConfig cfg = new JavaFileConfig();
            cfg.setJavaFileDoc("Controller");
            cfg.setClassName(SourceCodeUtil.getClassName(className));
            cfg.setPackageName(this.projectGenerator.getPackageName(packageNamePrefix));
            cfg.setOutDir(this.projectGenerator.getMavenSourceCodeDir(dir));

            new ControllerJavaFileGenerator(cfg,dtoGenerator.getDtoConfig()).generateControllerFile(item,configClass);
        });
    }

    private String createCommonFiles() {
        return this.projectGenerator.createConfigConstantsFile(getRootDir(),
                getSubPackage(this.globalConfig.getConstantsDir()),
                "CgiConstants", getUrls());
    }

    private String getSubPackage(String subDir) {
        return this.projectGenerator.getConfig().getProjectName() + "." + subDir;
    }

    private void createLogConfig() {
        this.projectGenerator
                .createLogConfigFile(getRootDir(), this.globalConfig.getLogConfigFilePath());
    }
}
