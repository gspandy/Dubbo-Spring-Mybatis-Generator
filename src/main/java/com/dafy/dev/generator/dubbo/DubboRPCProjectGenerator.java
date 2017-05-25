package com.dafy.dev.generator.dubbo;

import com.dafy.dev.GeneratorContext;
import com.dafy.dev.codegen.ClassLoaderUtil;
import com.dafy.dev.config.*;
import com.dafy.dev.generator.ProjectGenerator;
import com.dafy.dev.generator.SpringBootDubboJavaFileGenerator;
import com.dafy.dev.generator.common.Generator;
import com.dafy.dev.generator.common.JavaFileGenerator;
import com.dafy.dev.generator.common.PropertiesGenerator;
import com.dafy.dev.generator.maven.MavenDirUtil;
import com.dafy.dev.generator.maven.PomGenerator;
import com.dafy.dev.generator.project.DaoFileGenerator;
import com.dafy.dev.generator.project.DtoFileGenerator;
import com.dafy.dev.generator.project.DtoUtilFileGenerator;
import com.dafy.dev.generator.project.MyBatisGenerator;
import com.dafy.dev.generator.project.ServiceFileGenerator;
import com.dafy.dev.pojo.TableInfo;
import com.dafy.dev.util.BannerUtil;
import com.dafy.dev.util.CodeGenUtil;
import com.dafy.dev.util.FileUtil;
import com.dafy.dev.util.PomUtil;
import com.dafy.dev.util.SourceCodeUtil;
import com.dafy.dev.util.StringUtil;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chunxiaoli on 10/26/16.
 */
public class DubboRPCProjectGenerator implements Generator {

    private static final Logger logger = LoggerFactory.getLogger(ProjectGenerator.class);

    private ProjectGenerator projectGenerator;

    private DubboConfig dubboConfig;

    private String name;

    private ProjectConfig config;

    private String apiPomFile;


    private GlobalConfig globalConfig;

    private ClassLoader classLoader;


    private List<Class> daoList;

    private List<ServiceConfig.ServiceDaoInfo> serviceDaoInfos;

    private String rootPomPath;

    public DubboRPCProjectGenerator(GlobalConfig globalConfig) {
        validateConfig(globalConfig);
        this.globalConfig = globalConfig;
        this.name = globalConfig.getName();
        this.dubboConfig = ConfigDefault.getDefaultDubboConfig();
        this.config = ConfigGenerator.generateProjectConfig(this.globalConfig);
        this.projectGenerator = new ProjectGenerator(this.config);

    }

    //todo
    private void validateConfig(GlobalConfig config) {

    }

    @Override
    public void generate() {
        this.projectGenerator.createProjectDirStructure();
        createSubModuleDir();
        logger.debug("generate start config:{}", projectGenerator.getConfig());
    }

    private void createSubModuleDir() {
        BannerUtil.log("createSubModuleDir");
        createRootPom();
        createApiDir();
        createProviderDir();
    }

    private void createRootPom(){
        List<String> modules = new ArrayList<>();
        modules.add(getApiModuleName());
        modules.add(getProviderModuleName());
        //create root pom

        String template = this.globalConfig.getServiceParentPomTemplatePath();

        template = StringUtil.isEmpty(template) ? ConfigDefault.POM_TEMPLATE_ROOT : template;

        this.rootPomPath = projectGenerator.createRootPomFile(modules, null, template,
                projectGenerator.getConfig().getDir(),
                projectGenerator.getConfig().getProjectName());

        logger.info("pom :{}", rootPomPath);
    }

    @SuppressWarnings("unchecked")
    public void createApiDir() {
        logger.info("createApiDir:{}");
        String apiDir = getApiDir();
        //FileUtil.createDir(apiDir);
        //创建maven结构
        MavenDirUtil.createBaseMavenStructure(apiDir);

        for (String sub : Config.API_SUB_DIRS) {
            String subDir = getApiSourceCodeDir() + File.separator + sub;
            FileUtil.createDir(subDir);
        }

        createApiPom();
    }

    private void createApiPom(){

        String module = getApiModuleName();

        String template = this.globalConfig.getServiceApiPomTemplatePath();
        template = StringUtil.isEmpty(template) ? ConfigDefault.POM_TEMPLATE_API : template;

        this.apiPomFile = projectGenerator
                .createPomFile(this.rootPomPath, template, getApiDir(), module);
    }

    //生成api service
    private void createService() {
        JavaFileConfig cfg = new JavaFileConfig();
        cfg.setJavaFileDoc(this.name + " RPC interface");
        cfg.setClassName(getServiceName());
        cfg.setPackageName(getApiPackage());
        cfg.setOutDir(MavenDirUtil.getMavenSourceCodeDir(getApiDir()));

        //new JavaFileGenerator(cfg).generateInterface();
        //List<Class>daoCls=getDaoList();

        List<ServiceConfig.ServiceDaoInfo> list = getDaoInfoList();

        ServiceConfig serviceConfig = new ServiceConfig();

        serviceConfig.setDtoPackageName(getDtoPackage());
        //serviceConfig.setDaoList(daoCls);
        serviceConfig.setDaoInfos(list);


        new ServiceFileGenerator(cfg, serviceConfig).generateServiceInterfaceFile(this.classLoader,GeneratorContext.utilPackage);

        JavaFileConfig implCfg = new JavaFileConfig();
        implCfg.setJavaFileDoc(this.name + " RPC interface impl");
        implCfg.setClassName(getServiceName() + "Impl");
        implCfg.setPackageName(getProviderPackage() + ".impl");
        implCfg.setOutDir(MavenDirUtil.getMavenSourceCodeDir(getProviderDir()));

        String serviceFullName = getApiServicePackage() + "." + getServiceName();

        new ServiceFileGenerator(implCfg, serviceConfig).generateServiceImplFile(serviceFullName,this.classLoader,GeneratorContext.utilPackage);
    }

    private String getApiSourceCodeDir() {
        String dir = MavenDirUtil.getSourceCodeBaseDirOfGroup(getApiDir(),this.config.getGroupId(), this.name) + File.separator
                + this.globalConfig.getApiDirName();
        logger.info("ApiSourceCodeDir:{}", dir);
        return dir;
    }

    private String getApiModuleName() {
        return projectGenerator.getConfig().getProjectName() + "-" + this.globalConfig
                .getApiDirName();
    }

    private String getApiDir() {
        String api = projectGenerator.getConfig().getDir() + File.separator + getApiModuleName();
        logger.info("apiDir:{}", api);
        return api;
    }



    public void createProviderDir() {
        String providerDir = getProviderDir();
        FileUtil.createDir(providerDir);

        MavenDirUtil.createModuleMavenStructure(providerDir,this.config.getGroupId(), this.name);

        for (String sub : Config.PROVIDER_SUB_DIRS) {
            String subDir = getProviderSourceCodeDir() + File.separator + sub;
            FileUtil.createDir(subDir);
        }

        createDubboFile(providerDir);

        projectGenerator.createLauncherFile(providerDir, this.name +
                        "." + this.globalConfig.getProviderDirName(),
                this.name + "" +
                        SourceCodeUtil.getFirstUppercase(this.globalConfig.getProviderDirName()));

        createProviderHookFile();

        String moduleName = this.name;

        createProviderPom();

        for (TableInfo t : getTableList()) {
            t.setDomainName(getDomainName(t));
        }

        createOrm(moduleName, providerDir);

        //load denpendencies...
        //this.classLoader = ClassLoaderUtil.loadAllClass(new String[]{getProviderOrmDir(),getProviderPojoDir()});
        this.classLoader = ClassLoaderUtil.loadAllClass(getDir());

        if (getTableList() != null) {

            createDaoFiles();

            createDtoFromPojo();

            this.classLoader = ClassLoaderUtil.loadAllClass(getDir());

            createUtilFiles();
        }

        //update classloader
        this.classLoader = ClassLoaderUtil.loadAllClass(getDir());

        GeneratorContext.classLoader = this.classLoader;

        GeneratorContext.utilPackage = getProviderUtilPackage();

        createService();

        //createServiceFiles();

        createApplicationPropertiesFile(getProviderDir());

        createSpringBootAutoConfigFiles(getProviderDir());

        createLogConfig();

        this.classLoader = ClassLoaderUtil.loadAllClass(getDir());

        createTest();

    }

    private String getDomainName(TableInfo t) {
        return StringUtil.isEmpty(t.getDomainName()) ?
                SourceCodeUtil.covertClassName(
                        t.getTableName().startsWith("t_") ? t.getTableName().substring(2,
                                t.getTableName().length()) : t.getTableName()) : t.getDomainName();
    }

    private void createUtilFiles() {
        for (TableInfo t :getTableList()) {
            String pojoName = SourceCodeUtil.covertClassName(t.getDomainName());
            String pojoClsFullName = getPojoPackage() + "." + pojoName;
            String dtoClsFullName = getDtoPackage() + "." + pojoName + "Dto";

            Class pojoCls = ClassLoaderUtil.load(pojoClsFullName, this.classLoader);
            Class dtoCls = ClassLoaderUtil.load(dtoClsFullName, this.classLoader);

            DtoUtilConfig config = new DtoUtilConfig();
            config.setPackageName(getProviderUtilPackage());
            config.setOutDir(getProviderBaseDir());

            new DtoUtilFileGenerator(config).generateDtoUtilFile(pojoCls, dtoCls);
        }

    }

    private void createLogConfig() {
        this.projectGenerator
                .createLogConfigFile(getProviderDir(), this.globalConfig.getLogConfigFilePath());
    }

    private void createOrm(String moduleName, String providerDir) {
        createMybatisConfig(moduleName, moduleName, providerDir);

        //todo
        if (getTableList() != null) {
            createMybatisFiles(getTableList());
            createMybatisPropertiesFile(getProviderDir());
        } else {
            logger.warn("table list is null....");
        }

    }

    private void createProviderPom() {
        String providerDir = getProviderDir();
        String module = getProviderModuleName();
        String template = this.globalConfig.getServiceProviderPomTemplatePath();
        template = StringUtil.isEmpty(template) ? ConfigDefault.POM_TEMPLATE_PROVIDER : template;
        createPomFile(null, this.rootPomPath, template, providerDir, module);
    }

    public void createPomFile(List<String> modules, String parentTemplate,
                              String template, String dir, String moduleName) {
        //生成pom文件
        PomConfig cfg = ConfigDefault.getDefaultPomConfig(this.config, dir, moduleName);
        cfg.setTemplate(template);
        cfg.setParentTemplate(parentTemplate);
        cfg.setModules(modules);

        PomGenerator generator = new PomGenerator(this.config, cfg);

        Model model = generator.build().getModel();

        Dependency dependency = new Dependency();

        //增加api 依赖
        try {
            Model api = PomUtil.read(new FileInputStream(this.apiPomFile));
            assert api != null;
            if (api.getParent() != null) {
                dependency.setGroupId(api.getParent().getGroupId());
            } else if (api.getGroupId() != null) {
                dependency.setGroupId(api.getGroupId());
            } else {
                logger.error("group not specify .....");
            }

            dependency.setArtifactId(api.getArtifactId());
            dependency.setVersion(api.getVersion());

            model.addDependency(dependency);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //输出文件
        generator.write();
    }

    //生成测试文件
    private void createTest() {
        String rootDir = getProviderDir();
        String sourceCodeDir = MavenDirUtil.getMavenTestSourceCodeDir(rootDir);
        FileUtil.createDir(MavenDirUtil.getMavenTestBaseDir(rootDir));
        FileUtil.createDir(MavenDirUtil.getMavenTestResourceDir(rootDir));
        FileUtil.createDir(sourceCodeDir);

        String packageName = getProviderPackage();

        String baseTestClass = "BaseTest";

        JavaFileConfig serviceDaoCfg = new JavaFileConfig();
        serviceDaoCfg.setJavaFileDoc("test case");
        serviceDaoCfg.setClassName(baseTestClass);
        serviceDaoCfg.setPackageName(packageName);

        serviceDaoCfg.setOutDir(sourceCodeDir);

        new SpringBootDubboJavaFileGenerator(serviceDaoCfg)
                .generateBaseTestCaseFile(packageName + "." + baseTestClass,
                        getProviderHookFullName());

        String serviceFullClassName = getApiPackage() + "." + getServiceName();

        String servicePath = MavenDirUtil.getMavenSourceCodeDir(getApiDir()) + File.separator
                + SourceCodeUtil.convertPackage2Dir(serviceFullClassName) + ".java";

        Class serviceCls = ClassLoaderUtil.loadClass(servicePath, this.classLoader);

        new SpringBootDubboJavaFileGenerator(serviceDaoCfg)
                .generateTestCaseFile(packageName + "." + baseTestClass,
                        serviceFullClassName, serviceCls);

    }

    private void createDubboFile(String parentDir) {
        String base = MavenDirUtil.getResourceBaseDir(parentDir);
        this.dubboConfig.setOutputPath(base + File.separator + "/dubbo");
        DubboConfigGenerator generator = new DubboConfigGenerator(this.dubboConfig);
        generator.generate();
    }

    //return test-provider/src/main/java/com/dafy/dev/module/provider/


    private String getApiPackage() {
        return projectGenerator.getPackageName(this.name) + "." + this.globalConfig.getApiDirName();
    }


    private void createMybatisConfig(String moduleName, String classNamePrefix, String dir) {

        String propertySourceValue = "classpath:" + getMybatisPropertiesConfigPath(moduleName);

        String mapperScanValue = this.config.getGroupId() + "." + moduleName + "." +
                this.globalConfig.getProviderDirName() + "."
                + this.globalConfig.getOrmDirName();
        JavaFileConfig cfg = new JavaFileConfig();
        cfg.setJavaFileDoc("mybatis config");
        cfg.setClassName(SourceCodeUtil.convertFieldUppercase(classNamePrefix));
        cfg.setPackageName(getProviderPackage() + ".config");
        cfg.setOutDir(MavenDirUtil.getMavenSourceCodeDir(dir));
        new SpringBootDubboJavaFileGenerator(cfg)
                .generateMybatisConfigFile(propertySourceValue, mapperScanValue);
    }

    private String getMybatisPropertiesConfigPath(String module) {
        return module + "-mybatis-config.properties";
    }

    private void createMybatisFiles(List<TableInfo> tableList) {
        for (TableInfo tableItem : tableList) {
            createMybatisFile(tableItem);
        }
    }

    private void createMybatisFile(TableInfo tableItem) {
        String mavenBase = MavenDirUtil.getMavenBaseDir(getProviderDir());
        MybatisConfig cfg = ConfigGenerator.generateMybatisConfig(this.globalConfig.getMybatisConfig(),
                tableItem, getProviderPackage());

        cfg.setProjectDir(mavenBase);

        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(cfg);

        myBatisGenerator.generate();
    }

    private String getDtoDir() {
        return SourceCodeUtil.convertPackage2Dir(getDtoPackage());
    }

    private String getDtoPackage() {
        return getApiPackage() + "." + this.globalConfig.getDtoDirName();
    }

    private String getApiDtoDir() {
        return getApiBaseDir() + File.separator + globalConfig.getDtoDirName() + File.separator
                + "response";
    }

    private void createDtoFromPojo() {
        String dir = getProviderPojoDir();
        File f = new File(dir);
        if (f.exists()) {
            File[] files = f.listFiles();
            if(files!=null){
                for (File file : files) {
                    if (file.isFile() && file.getPath().endsWith(".java")) {
                        Class pojoCls = ClassLoaderUtil.loadClass(file.getPath(), this.classLoader);
                        String apiBaseDir = getApiBaseDir();
                        DtoConfig dtoConfig = new DtoConfig();
                        dtoConfig.setDir(apiBaseDir);
                        dtoConfig.setPackageName(getDtoPackage());

                        String daoDir = getDaoPackage();

                        String targetClassFullName =
                                getApiDtoPackage() + "." + file.getName().replace(".java", "") + "Dto";

                        new DtoFileGenerator(dtoConfig).generateFromPojoClass(pojoCls, targetClassFullName);
                    }
                }
            }else {
                logger.error("get provider pojo files empty or null...:{}",getProviderPojoDir());
            }

        }

    }

    public List<Class> getDaoList() {
        if (this.daoList != null) {
            return this.daoList;
        }
        this.daoList = loadClassFromDir(getProviderDaoDir());
        return this.daoList;
    }

    public List<ServiceConfig.ServiceDaoInfo> getDaoInfoList() {

        if (this.serviceDaoInfos != null) {
            return this.serviceDaoInfos;
        }
        this.serviceDaoInfos = new ArrayList<>();
        this.daoList = loadClassFromDir(getProviderDaoDir());
        this.daoList.forEach(item -> {
            String name = item.getSimpleName();
            if (name.endsWith("Dao")) {
                name = name.replace("Dao", "");
                Class pojo = ClassLoaderUtil
                        .loadClass(getProviderPojoDir() + File.separator + name + ".java",
                                this.classLoader);
                ServiceConfig.ServiceDaoInfo info = new ServiceConfig.ServiceDaoInfo();
                info.setDao(item);
                info.setPojo(pojo);
                serviceDaoInfos.add(info);
            }
        });

        return serviceDaoInfos;
    }

    //生成appplication.properties
    private void createApplicationPropertiesFile(String parentDir) {

        String templatePath = this.globalConfig.getApplicationPropertiesTemplatePath();
        String base = MavenDirUtil.getResourceBaseDir(parentDir);
        String path = base + File.separator + this.globalConfig.getConfigDirName() +
                File.separator + "application.properties";
        if (!StringUtil.isEmpty(templatePath)) {
            FileUtil.copy(templatePath, path);
        } else {
            InputStream inputStream = this.getClass().getClassLoader().
                    getResourceAsStream("template/application.properties");
            FileUtil.save(inputStream, path);
        }

    }
    //生成 config/module-mybatis-config.properties文件
    private void createMybatisPropertiesFile(String parentDir) {
        String base = MavenDirUtil.getResourceBaseDir(parentDir);
        String path = base + File.separator + getMybatisPropertiesConfigPath(this.name);
        PropertyConfig config = new PropertyConfig();
        config.setPath(path);

        PropertiesGenerator generator = new PropertiesGenerator(config);

        /*com.dafy.mybatis.typeAlias.account=com.dafy.collection.account.provider.pojo
        com.dafy.mybatis.mapper.account=mybatis/AccountMapper.xml*/

        generator.set("com.dafy.mybatis.typeAlias." + this.name, getProviderPackage() + "." +
                this.globalConfig.getPojoDirName());


        List<String> tables = GeneratorContext.getTableList(this.globalConfig);
        if(tables!=null){
            for (String table : tables) {
                String domain = SourceCodeUtil
                        .uppercase(table.startsWith("t_") ? table.substring(2, table.length()) : table, true);
                generator.set("com.dafy.mybatis.mapper." + domain,
                        "mybatis/" + getMapperXmlName(domain));
            }
        }else {
            logger.error("table list is null or empty,please check your mybatis config");
        }


        generator.generate();
    }

    //spring auto config
    private void createSpringBootAutoConfigFiles(String parentDir) {
        String base = MavenDirUtil.getResourceBaseDir(parentDir);

        FileUtil.createDir(base + File.separator + "META-INF");

        String path = base + File.separator + "META-INF" + File.separator + "spring.factories";
        PropertyConfig config = new PropertyConfig();
        config.setPath(path);

        PropertiesGenerator generator = new PropertiesGenerator(config);

        //todo configurable
        generator.set("org.springframework.boot.autoconfigure.EnableAutoConfiguration",
                globalConfig.getSpringAutoConfigFullClassPath());
        generator.generate();
    }

    private String getMapperXmlName(String tableName) {
        String mapperPostName = SourceCodeUtil
                .getFirstUppercase(this.globalConfig.getMapperXmlFilePost());
        return SourceCodeUtil.uppercase(tableName, false) + mapperPostName + ".xml";
    }

    private String getApiDtoPackage() {
        return getApiPackage() + "." + this.globalConfig.getDtoDirName();
    }

    private String getDaoPackage() {
        return getProviderPackage() + "." + this.globalConfig.getDaoDirName();
    }

    private String getPojoPackage() {
        return getProviderPackage() + "." + this.globalConfig.getPojoDirName();
    }

    private String getProviderUtilPackage() {
        return getProviderPackage() + ".util";
    }

    private String getOrmPackage() {
        return getProviderPackage() + "." + this.globalConfig.getOrmDirName();
    }

    private String getDaoFullName(String domainName) {
        String entity = SourceCodeUtil.getFirstUppercase(domainName);
        String daoInterfaceName =
                entity + SourceCodeUtil.getFirstUppercase(this.globalConfig.getDaoDirName());
        logger.debug("daoInterfaceName:{}", daoInterfaceName);

        return getProviderPackage() + "." + this.globalConfig.getDaoDirName() + "."
                + daoInterfaceName;
    }

    private String getDir() {
        return new File(getApiDir()).getParent();
    }

    private String getApiBaseDir() {
        return MavenDirUtil.getMavenSourceCodeDir(getApiDir());
    }

    //
    private String getProviderBaseDir() {
        return MavenDirUtil.getMavenSourceCodeDir(getProviderDir());
    }

    private String getProviderBasePackageDir() {
        return getProviderBaseDir() + SourceCodeUtil.convertPackage2Dir(getProviderPackage());
    }

    private String getApiBasePackageDir() {
        return getApiBaseDir() + SourceCodeUtil.convertPackage2Dir(getApiPackage());
    }

    private String getProviderPojoDir() {
        return getProviderBaseDir() + SourceCodeUtil.convertPackage2Dir(getPojoPackage());
    }

    private String getProviderOrmDir() {
        return getProviderBaseDir() + SourceCodeUtil.convertPackage2Dir(getOrmPackage());
    }

    private String getProviderDaoDir() {
        return getProviderBaseDir() + SourceCodeUtil.convertPackage2Dir(getDaoPackage());
    }

    private void createDaoFiles() {
        for (TableInfo t : getTableList()) {
            createDaoFile(t.getDomainName());
        }
    }


    private List<TableInfo> getTableList(){
        if(this.globalConfig.getMybatisConfig()!=null){
            return this.globalConfig.getMybatisConfig().getTableInfoList();
        }
        return new ArrayList<>();
    }


    //生成dao
    private void createDaoFile(String domainName) {
        List<Class> classList = loadClassFromDir(getProviderOrmDir());
        classList.forEach(cls -> {
            List<MethodInfo> methodInfos = CodeGenUtil.getMethods(cls);
            doCreateDaoFile(domainName, methodInfos);
        });
    }

    private List<Class> loadClassFromDir(String dir) {
        List<Class> classes = new ArrayList<>();
        File files[] = new File(dir).listFiles();
        if (files != null && files.length > 0) {
            for (File f : files) {
                if (FileUtil.isJavaFile(f)) {
                    Class cls = ClassLoaderUtil.loadClass(f.getPath(), this.classLoader);
                    if(cls!=null){
                        classes.add(cls);
                    }else {
                        logger.error("load class {} error",f.getAbsoluteFile());
                    }
                }
            }
        }
        return classes;
    }

    private void doCreateDaoFile(String domainName, List<MethodInfo> methodInfos) {
        String entity = SourceCodeUtil.getFirstUppercase(domainName);

        String daoPackage = getDaoPackage();
        String daoImplPackage = daoPackage + ".impl";

        String baseDir = getProviderBaseDir();

        String daoInterfaceName =
                entity + SourceCodeUtil.getFirstUppercase(this.globalConfig.getDaoDirName());

        JavaFileConfig daoCfg = new JavaFileConfig();

        daoCfg.setJavaFileDoc("dao");
        daoCfg.setClassName(daoInterfaceName);
        daoCfg.setPackageName(daoPackage);
        daoCfg.setOutDir(baseDir);
        daoCfg.setMethodInfos(methodInfos);

        JavaFileConfig daoImplCfg = new JavaFileConfig();
        daoImplCfg.setJavaFileDoc("dao impl");
        daoImplCfg.setClassName(entity + "DaoImpl");
        daoImplCfg.setPackageName(daoImplPackage);
        daoImplCfg.setOutDir(baseDir);
        daoImplCfg.setMethodInfos(methodInfos);

        String mapperSuffix = SourceCodeUtil
                .getFirstUppercase(this.globalConfig.getMapperXmlFilePost());

        String mapperFile = getProviderOrmDir() + File.separator +
                SourceCodeUtil.getFirstUppercase(entity) + mapperSuffix + ".java";

        DaoConfig daoConfig = new DaoConfig();

        daoConfig.setMapper(ClassLoaderUtil.loadClass(mapperFile, this.classLoader));

        DaoConfig daoConfigImpl = new DaoConfig();

        daoConfigImpl.setMapper(ClassLoaderUtil.loadClass(mapperFile, this.classLoader));

        doCreateDaoFile(daoCfg, daoConfig);

        doCreateDaoFile(daoImplCfg, daoConfigImpl);

    }

    private void doCreateDaoFile(JavaFileConfig javaFileConfig, DaoConfig daoConfig) {
        new DaoFileGenerator(javaFileConfig, daoConfig).generateDaoImpl();
    }

    private String getServiceName() {
        return SourceCodeUtil.covertClassName(this.name + SourceCodeUtil.getFirstUppercase(
                this.globalConfig.getServiceFilePost()));
    }

    //生成serviceImpl文件
    private void createServiceFiles() {
        String baseDir = getProviderBaseDir();

        //service
        String servicePackage = getApiPackage();

        String serviceImplPackage = getProviderPackage() + ".impl";

        String serviceImplClassName = getServiceName() + "Impl";

        String serviceInterface = "";

        JavaFileConfig serviceDaoCfg = new JavaFileConfig();
        serviceDaoCfg.setJavaFileDoc("rpc service impl");
        serviceDaoCfg.setClassName(serviceImplClassName);
        serviceDaoCfg.setPackageName(serviceImplPackage);

        serviceDaoCfg.setOutDir(baseDir);

        List<String> daos = new ArrayList<>();

        for (TableInfo item : getTableList()) {
            String table = item.getTableName();
            String entity = SourceCodeUtil.jsontoUpperCase(
                    table.startsWith("t_") ? table.substring(2, table.length()) : table);
            daos.add(getDaoFullName(entity));
        }

        new SpringBootDubboJavaFileGenerator(serviceDaoCfg)
                .generateServiceImplFile(servicePackage + "." + getServiceName(),
                        serviceImplPackage + "." + serviceImplClassName, daos);

    }

    private String getApiServicePackage() {
        return getApiPackage();
    }

    private String getProviderHookFullName() {
        return getProviderPackage() + "." + getProviderHookClassName();
    }

    private String getProviderHookClassName() {
        return SourceCodeUtil.covertClassName(this.name + "ProviderHook");
    }

    private void createProviderHookFile() {
        JavaFileConfig cfg = new JavaFileConfig();
        cfg.setJavaFileDoc("Hook");
        cfg.setClassName(getProviderHookClassName());
        cfg.setPackageName(getProviderPackage());
        cfg.setOutDir(MavenDirUtil.getMavenSourceCodeDir(getProviderDir()));
        new JavaFileGenerator(cfg).generateProviderHookFile();
    }


    private  String getProviderDir() {
        String provider =
                projectGenerator.getConfig().getDir() + File.separator + getProviderModuleName();
        logger.info("ProviderDir:{}", provider);
        return provider;
    }

    private  String getProviderModuleName() {
        return projectGenerator.getConfig().getProjectName() + "-" + this.globalConfig
                .getProviderDirName();
    }

    public  String getProviderSourceCodeDir() {
        return MavenDirUtil
                .getSourceCodeBaseDirOfGroup(getProviderDir(),this.config.getGroupId(),this.name)
                + File.separator + this.globalConfig.getProviderDirName();
    }

    public  String getProviderPackage() {
        return projectGenerator.getPackageName(this.name) + "." + this.globalConfig
                .getProviderDirName();
    }

}
