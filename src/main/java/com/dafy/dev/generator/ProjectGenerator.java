package com.dafy.dev.generator;


import com.dafy.dev.config.*;
import com.dafy.dev.util.*;
import org.apache.maven.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chunxiaoli on 10/12/16.
 *
 *  通用java maven项目生成器
 */
public class ProjectGenerator implements Generator{

    private static final Logger logger= LoggerFactory.getLogger(ProjectGenerator.class);


    private ProjectConfig config;

    private PomConfig pomConfig;

    private  Model model;

    private  MybatisConfig mybatisConfig;


    public ProjectGenerator(){
    }

    @SuppressWarnings("unchecked")
    public ProjectGenerator(ProjectConfig config){
        checkConfig(config);
        this.pomConfig=createRootPomConfig(config);
        this.config=config;
        InputStream inputStream=getClass().getClassLoader().getResourceAsStream("config.yaml");
        Map<String,Object> map= SourceCodeUtil.parseServiceInterface(inputStream);
        //this.config.setServices((Map<String,Map<String, List<String>>>) map.get("services"));
    }

    private void parseConfig(String configPath){

    }

    private void checkConfig(ProjectConfig config) {
        if(config==null){
            logger.error("config is null");
        }
        assert config != null;

        if(config.getDir()==null){
            logger.error("dir is null");
        }
    }

    private PomConfig createRootPomConfig(ProjectConfig config){
        PomConfig cfg=new PomConfig();
        cfg.setPackaging("pom");
        cfg.setTemplate("template/template-root-pom.xml");
        cfg.setGroupId(config.getGroupId());
        cfg.setArtifactId(config.getArtifactId());
        cfg.setVersion(config.getVersion());
        cfg.setDir(config.getDir());
        cfg.setProjectName(config.getProjectName());
        //子模块
        List<String> modules=new ArrayList<>();
        modules.add("./"+config.getProjectName()+"-api");
        modules.add("./"+config.getProjectName()+"-provider");
        cfg.setModules(modules);
        return cfg;
    }


    @Override
    public void generate() {
        createProjectDirStructure();
        PomGenerator generator= new PomGenerator(null,this.pomConfig);
        generator.generate();
        this.model=generator.getModel();
        logger.debug("generate start config:{}",config);
    }





    private void createPropertiesFile(String parentDir) {
        String base=getResourceBaseDir(parentDir);
        PropertyConfig config= new PropertyConfig();
        config.setPath(base);
        PropertiesGenerator generator=new PropertiesGenerator(config);
        generator.generate();
    }


    public void createProjectDirStructure(){
        BannerUtil.log("createProjectDirStructure");
        String rootDir=config.getDir();
        /*if(FileUtil.fileExists(rootDir)){
            logger.error("dir:{} is exists please check your config",new File(config.getDir()).getAbsoluteFile());
            System.exit(1);
        }*/
        createProjectRootDir();
        //createSubModuleDir();
    }

    private void createProjectRootDir(){
        BannerUtil.log("createProjectRootDir");
        FileUtil.createDir(config.getDir());
    }

    public String getProjectDir(){
        return this.config.getDir();
    }



    //maven project structure
    public void createMavenStructure(String parentDir,String module){
        FileUtil.createDir(getSourceCodeBaseDir(parentDir,module));
        createResources(parentDir);
    }

    //maven project structure
    public void createMavenStructure(String parentDir){
        createMavenStructure(parentDir,null);
    }

    private void createResources(String parentDir){
        logger.debug("createResources...:{}",parentDir);
        String base=getResourceBaseDir(parentDir);

        for (String sub: Config.RESOURCES_SUB_DIRS) {
            String subDir=base+File.separator+sub;
            FileUtil.createDir(subDir);
        }
        //createPropertiesFile(parentDir);
    }

    public void createLogConfigFile(String parentDir,String sourceFile){
        String base=getResourceBaseDir(parentDir);
        if(!StringUtil.isEmpty(sourceFile)&&new File(sourceFile).exists()){
            FileUtil.copy(sourceFile,base+File.separator+new File(sourceFile).getName());
        }else { //create default
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(ConfigDefault.LOG_CONFIG_FILE);
            FileUtil.save(inputStream,base+File.separator+"logback.xml");
        }
    }

    public String getPackageName(String prefix){
        return this.config.getGroupId()+"."+prefix;
    }

    public String getMavenSRCDir(String parentDir){
        return parentDir+Config.MAVEN_SRC_DIR_PREFIX;
    }

    public String getMavenTestBaseDir(String parentDir){
        return parentDir+Config.MAVEN_SRC_DIR_PREFIX+File.separator+"test";
    }

    public String getMavenTestSourceCodeDir(String parentDir){
        return getMavenTestBaseDir(parentDir)+File.separator+"java"+File.separator;
    }

    public String getMavenTestResourceDir(String parentDir){
        return getMavenTestBaseDir(parentDir)+File.separator+"resources";
    }

    public String getMavenBaseDir(String parentDir){
        return parentDir+Config.MAVEN_MAIN_DIR_PREFIX;
    }

    //maven java目录 src/main/java
    public String getMavenSourceCodeDir(String parentDir){

        String dir=getMavenBaseDir(parentDir)+File.separator+"java"+File.separator;
        logger.debug("maven code dir:{}",dir);
        return dir;
    }

    //源代码根目录 src/main/java/com.dafy.dev
    public String getSourceCodeBaseDir(String parentDir){
        String baseDir=getMavenSourceCodeDir(parentDir)+ getBasePackageDir("");
        return baseDir;
    }

    //源代码根目录 src/main/java/com.dafy.dev
    public String getSourceCodeBaseDir(String parentDir,String moduleDir){
        String baseDir=getMavenSourceCodeDir(parentDir)+ getBasePackageDir(moduleDir);
        return baseDir;
    }

    //资源根目录 src/main/java/resources
    public String getResourceBaseDir(String parentDir){
        String resourceDir=getMavenBaseDir(parentDir)+File.separator+"resources";
        return resourceDir;
    }

    public String getBasePackageDir(String dir){
        return PackageUtil.convertPackage2Path(this.config.getGroupId()+(StringUtil.isEmpty(dir)?"":"."+dir));
    }

    public String getBaseServerPackageDir(String dir){
        return PackageUtil.convertPackage2Path(this.config.getGroupId()+(".server."+dir));
    }

    public String getBasePackage(String dir){
        return this.config.getGroupId()+(StringUtil.isEmpty(dir)?"":"."+dir);
    }
    public String getBaseServerPackage(String dir){
        return this.config.getGroupId()+(".server."+dir);
    }

    public String createPomFile(String template,String dir,String module){
        return createPomFile(null,template,dir,module);
    }

    public String createPomFile(String parentTemplate,String template,String dir,String module){
       return createPomFile(null,parentTemplate,template,dir,module);
    }

    public String createPomFile(List<String>modules,String parentTemplate,
                                String template,String dir,String moduleName){
        //生成pom文件
        PomConfig config= ConfigDefault.getDefaultPomConfig(this.config,dir,moduleName);
        config.setTemplate(template);
        config.setParentTemplate(parentTemplate);
        config.setModules(modules);

        PomGenerator generator= new PomGenerator(this.config,config);

        generator.generate();

        return generator.getOutputPomFilePath();
    }

    public String createRootPomFile(List<String>modules,String parentTemplate,String template,
                                    String dir,String module){
        //生成pom文件
        PomConfig config= ConfigDefault.getDefaultPomConfig(this.config,dir,module);
        config.setTemplate(template);
        config.setParentTemplate(parentTemplate);
        config.setModules(modules);
        config.setPackaging("pom");


        return createPomFile(config);
    }



    public String createPomFile(PomConfig config){

        PomGenerator generator= new PomGenerator(this.config,config);
        generator.generate();
        return generator.getOutputPomFilePath();
    }



    //生成入口文件
    public void createLauncherFile(String dir, String packageNamePrefix,String launcherName){
        JavaFileConfig cfg= new JavaFileConfig();
        cfg.setJavaFileDoc("测试接口");
        cfg.setClassName(SourceCodeUtil.convertFieldUppercase(launcherName));
        cfg.setPackageName(getPackageName(packageNamePrefix));
        cfg.setOutDir(getMavenSourceCodeDir(dir));
        new SpringBootDubboJavaFileGenerator(cfg).generateSpringLauncherFile();
    }

    //生成入口文件
    public void createWebLauncherFile(String dir, String packageNamePrefix,String launcherName){
        JavaFileConfig cfg= new JavaFileConfig();
        cfg.setJavaFileDoc("Launcher");
        cfg.setClassName(SourceCodeUtil.convertFieldUppercase(launcherName));
        cfg.setPackageName(getPackageName(packageNamePrefix));
        cfg.setOutDir(getMavenSourceCodeDir(dir));
        new SpringBootDubboJavaFileGenerator(cfg).generateSpringWebLauncherFile();
    }

    //生成controller文件

    /**
     *
     * @param dir
     * @param packageNamePrefix
     * @param className
     * @param urls
     * @param configClass requestMapping 里面引用的配置常量类
     */
    public String createControllerFile(String dir, String packageNamePrefix,
                                     String className, List<String>urls, String configClass){
        JavaFileConfig cfg= new JavaFileConfig();
        cfg.setJavaFileDoc("Launcher");
        cfg.setClassName(SourceCodeUtil.getClassName(className));
        cfg.setPackageName(getPackageName(packageNamePrefix));
        cfg.setOutDir(getMavenSourceCodeDir(dir));
        return new SpringBootDubboJavaFileGenerator(cfg).generateControllerFile(urls,configClass);
    }



    public String createConfigConstantsFile(String dir, String packageNamePrefix,
                                     String className, List<String>urls){
        JavaFileConfig cfg= new JavaFileConfig();
        cfg.setJavaFileDoc("controller cgi constants");
        cfg.setClassName(SourceCodeUtil.getClassName(className));
        cfg.setPackageName(getPackageName(packageNamePrefix));
        cfg.setOutDir(getMavenSourceCodeDir(dir));
        return new SpringBootDubboJavaFileGenerator(cfg).generateConstantsConfigFile(urls);
    }


    public ProjectConfig getConfig() {
        return config;
    }

    public void setConfig(ProjectConfig config) {
        this.config = config;
    }

    public PomConfig getPomConfig() {
        return pomConfig;
    }

    public void setPomConfig(PomConfig pomConfig) {
        this.pomConfig = pomConfig;
    }



    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public MybatisConfig getMybatisConfig() {
        return mybatisConfig;
    }

    public void setMybatisConfig(MybatisConfig mybatisConfig) {
        this.mybatisConfig = mybatisConfig;
    }
}
