package com.dafy.dev.generator.provider;

import com.dafy.dev.GeneratorContext;
import com.dafy.dev.codegen.ClassLoaderUtil;
import com.dafy.dev.config.Config;
import com.dafy.dev.config.ConfigDefault;
import com.dafy.dev.config.ConfigGenerator;
import com.dafy.dev.config.GlobalConfig;
import com.dafy.dev.config.JavaFileConfig;
import com.dafy.dev.config.MybatisConfig;
import com.dafy.dev.config.provider.ProviderConfig;
import com.dafy.dev.generator.CommonGenerator;
import com.dafy.dev.generator.SpringBootDubboJavaFileGenerator;
import com.dafy.dev.generator.common.JavaFileGenerator;
import com.dafy.dev.generator.maven.MavenDirUtil;
import com.dafy.dev.pojo.TableInfo;
import com.dafy.dev.util.FileUtil;
import com.dafy.dev.util.SourceCodeUtil;
import com.dafy.dev.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.dafy.dev.util.SourceCodeUtil.createService;

/**
 * Created by chunxiaoli on 5/19/17.
 */
public class ProviderModuleGenerator {

    private final Logger logger = LoggerFactory.getLogger(ProviderModuleGenerator.class);

    private ProviderConfig  config;
    private CommonGenerator commonGenerator;

    public ProviderModuleGenerator(ProviderConfig config) {
        this.config = config;
        commonGenerator=new CommonGenerator();
    }

    public void createProviderDir() {
        String providerDir = ProviderUtil.getProviderDir(this.config);
        FileUtil.createDir(providerDir);

        MavenDirUtil.createModuleMavenStructure(providerDir,this.config.getGroupId(), this.name);

        for (String sub : Config.PROVIDER_SUB_DIRS) {
            String subDir = getProviderSourceCodeDir() + File.separator + sub;
            FileUtil.createDir(subDir);
        }

        //createDubboFile(providerDir);

        commonGenerator.createLauncherFile(providerDir, this.config.getName() +
                        "." + this.config.getProviderDirName(),
                this.config.getName()+ "" +
                        SourceCodeUtil.getFirstUppercase(this.config.getProviderDirName()));

        createProviderHookFile();

        String moduleName = this.config.getName();

        // todo
        // createProviderPom();

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

    private void createProviderHookFile() {
        JavaFileConfig cfg = new JavaFileConfig();
        cfg.setJavaFileDoc("Hook");
        cfg.setClassName(getProviderHookClassName());
        cfg.setPackageName(ProviderUtil.getProviderPackage(this.config));
        cfg.setOutDir(MavenDirUtil.getMavenSourceCodeDir(ProviderUtil.getProviderDir(this.config)));
        new JavaFileGenerator(cfg).generateProviderHookFile();
    }

    private String getProviderHookClassName() {
        return SourceCodeUtil.covertClassName(this.config.getName() + "ProviderHook");
    }

    private void createProviderPom(String rootPomPath) {
        String providerDir = ProviderUtil.getProviderDir(this.config);
        String module = ProviderUtil.getProviderModuleName(this.config);
        String template = this.config.getServiceProviderPomTemplatePath();
        template = StringUtil.isEmpty(template) ? ConfigDefault.POM_TEMPLATE_PROVIDER : template;
        commonGenerator.createPomFile(null, rootPomPath, template, providerDir, module);
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

    private void createMybatisConfig(String moduleName, String classNamePrefix, String dir) {

        String propertySourceValue = "classpath:" + getMybatisPropertiesConfigPath(moduleName);

        String mapperScanValue = this.config.getGroupId() + "." + moduleName + "." +
                this.config.getProviderDirName() + "."
                + this.config.getOrmDirName();
        JavaFileConfig cfg = new JavaFileConfig();
        cfg.setJavaFileDoc("mybatis config");
        cfg.setClassName(SourceCodeUtil.convertFieldUppercase(classNamePrefix));
        cfg.setPackageName(ProviderUtil.getProviderPackage(this.config) + ".config");
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
        String mavenBase = MavenDirUtil.getMavenBaseDir(ProviderUtil.getProviderDir(this.config));
        MybatisConfig cfg = generateMybatisConfig(tableItem, ProviderUtil.getProviderPackage(this.config));

        cfg.setProjectDir(mavenBase);

        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(cfg);

        myBatisGenerator.generate();
    }

    public  MybatisConfig generateMybatisConfig(TableInfo table,String packageRoot){
        MybatisConfig cfg= this.config.getMybatisConfig();

        cfg.setDaoPackage(packageRoot+".dao");
        cfg.setMapperPackage(packageRoot+".orm");
        cfg.setModelPackage(packageRoot+".pojo");
        cfg.setMapperXMLPackage("");

        cfg.setTable(table.getTableName());
        cfg.setDomainObjectName(table.getDomainName());
        return cfg;
    }

    private List<TableInfo> getTableList(){
        if(this.config.getMybatisConfig()!=null){
            return this.config.getMybatisConfig().getTableInfoList();
        }
        return new ArrayList<>();
    }

    private String getDomainName(TableInfo t) {
        return StringUtil.isEmpty(t.getDomainName()) ?
                SourceCodeUtil.covertClassName(
                        t.getTableName().startsWith("t_") ? t.getTableName().substring(2,
                                t.getTableName().length()) : t.getTableName()) : t.getDomainName();
    }


}
