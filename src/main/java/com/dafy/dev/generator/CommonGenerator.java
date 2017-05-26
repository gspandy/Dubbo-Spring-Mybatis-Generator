package com.dafy.dev.generator;


import com.dafy.dev.config.CommonConfig;
import com.dafy.dev.config.ConfigDefault;
import com.dafy.dev.config.JavaFileConfig;
import com.dafy.dev.config.MybatisConfig;
import com.dafy.dev.config.PomConfig;
import com.dafy.dev.config.ProjectConfig;
import com.dafy.dev.config.PropertyConfig;
import com.dafy.dev.generator.common.Generator;
import com.dafy.dev.generator.common.PropertiesGenerator;
import com.dafy.dev.generator.maven.MavenDirUtil;
import com.dafy.dev.generator.maven.PomGenerator;
import com.dafy.dev.util.BannerUtil;
import com.dafy.dev.util.FileUtil;
import com.dafy.dev.util.SourceCodeUtil;
import com.dafy.dev.util.StringUtil;
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
public class CommonGenerator implements Generator {

    private static final Logger logger= LoggerFactory.getLogger(CommonGenerator.class);


    private ProjectConfig config;

    private PomConfig pomConfig;

    private  Model model;

    private  MybatisConfig mybatisConfig;


    public CommonGenerator(){
    }

    @SuppressWarnings("unchecked")
    public CommonGenerator(ProjectConfig config){
        checkConfig(config);
        this.pomConfig=createRootPomConfig(config);
        this.config=config;
        InputStream inputStream=getClass().getClassLoader().getResourceAsStream("config.yaml");
        Map<String,Object> map= SourceCodeUtil.parseServiceInterface(inputStream);
        //this.config.setServices((Map<String,Map<String, List<String>>>) map.get("services"));
    }

    public CommonGenerator(CommonConfig config){
        this.pomConfig=createRootPomConfig(config);
        InputStream inputStream=getClass().getClassLoader().getResourceAsStream("config.yaml");
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

    private PomConfig createRootPomConfig(CommonConfig config){
        PomConfig cfg=new PomConfig();
        cfg.setPackaging("pom");
        cfg.setTemplate("template/template-root-pom.xml");
        cfg.setGroupId(config.getGroupId());
        cfg.setArtifactId(config.getArtifactId());
        cfg.setVersion(config.getVersion());
        cfg.setDir(config.getDir());
        cfg.setProjectName(config.getName());
        //子模块
        List<String> modules=new ArrayList<>();
        modules.add("./"+config.getName()+"-api");
        modules.add("./"+config.getName()+"-provider");
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
        String base=MavenDirUtil.getResourceBaseDir(parentDir);
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





    public void createLogConfigFile(String parentDir,String sourceFile){
        String base=MavenDirUtil.getResourceBaseDir(parentDir);
        if(!StringUtil.isEmpty(sourceFile)&&new File(sourceFile).exists()){
            FileUtil.copy(sourceFile,base+File.separator+new File(sourceFile).getName());
        }else { //create default
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(ConfigDefault.LOG_CONFIG_FILE);
            FileUtil.save(inputStream,base+File.separator+"logback.xml");
        }
    }



    public String createPomFile(String template,String dir,String module){
        return createPomFile(null,template,dir,module);
    }

    public String createPomFile(String parentTemplate,String template,String dir,String module){
       return createPomFile(null,parentTemplate,template,dir,module);
    }

    public String createPomFile(CommonConfig commonConfig,List<String>modules,
                                String parentTemplate, String template,String dir,String moduleName){
        //生成pom文件
        PomConfig config= ConfigDefault.getDefaultPomConfig(commonConfig,dir,moduleName);
        config.setTemplate(template);
        config.setParentTemplate(parentTemplate);
        config.setModules(modules);

        PomGenerator generator= new PomGenerator(this.config,config);

        generator.generate();

        return generator.getOutputPomFilePath();
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

    public   String getPackageName(String prefix){
        return this.getConfig().getGroupId()+"."+prefix;
    }

    public static   String getPackageName(CommonConfig config, String prefix){
        return config.getGroupId()+"."+prefix;
    }





    //生成入口文件
    public  void createLauncherFile(String dir, String packageNamePrefix,String launcherName){
        JavaFileConfig cfg= new JavaFileConfig();
        cfg.setJavaFileDoc("测试接口");
        cfg.setClassName(SourceCodeUtil.convertFieldUppercase(launcherName));
        cfg.setPackageName(getPackageName(packageNamePrefix));
        cfg.setOutDir(MavenDirUtil.getMavenSourceCodeDir(dir));
        new SpringBootDubboJavaFileGenerator(cfg).generateSpringLauncherFile();
    }

    //生成入口文件
    public void createWebLauncherFile(String dir, String packageNamePrefix,String launcherName){
        JavaFileConfig cfg= new JavaFileConfig();
        cfg.setJavaFileDoc("Launcher");
        cfg.setClassName(SourceCodeUtil.convertFieldUppercase(launcherName));
        cfg.setPackageName(getPackageName(packageNamePrefix));
        cfg.setOutDir(MavenDirUtil.getMavenSourceCodeDir(dir));
        new SpringBootDubboJavaFileGenerator(cfg).generateSpringWebLauncherFile();
    }



    public String createConfigConstantsFile(String dir, String packageNamePrefix,
                                     String className, List<String>urls){
        JavaFileConfig cfg= new JavaFileConfig();
        cfg.setJavaFileDoc("controller cgi constants");
        cfg.setClassName(SourceCodeUtil.getClassName(className));
        cfg.setPackageName(getPackageName(packageNamePrefix));
        cfg.setOutDir(MavenDirUtil.getMavenSourceCodeDir(dir));
        return new SpringBootDubboJavaFileGenerator(cfg).generateConstantsConfigFile(urls);
    }


    public ProjectConfig getConfig() {
        return config;
    }

    public void setConfig(ProjectConfig config) {
        this.config = config;
    }




    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }


}
