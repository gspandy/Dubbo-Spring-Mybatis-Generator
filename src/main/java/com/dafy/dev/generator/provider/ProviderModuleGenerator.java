package com.dafy.dev.generator.provider;

import com.dafy.dev.GeneratorContext;
import com.dafy.dev.codegen.ClassLoaderUtil;
import com.dafy.dev.config.Config;
import com.dafy.dev.config.ConfigDefault;
import com.dafy.dev.config.provider.ProviderConfig;
import com.dafy.dev.generator.CommonGenerator;
import com.dafy.dev.generator.maven.MavenDirUtil;
import com.dafy.dev.pojo.TableInfo;
import com.dafy.dev.util.FileUtil;
import com.dafy.dev.util.SourceCodeUtil;
import com.dafy.dev.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static com.dafy.dev.util.SourceCodeUtil.createService;

/**
 * Created by chunxiaoli on 5/19/17.
 */
public class ProviderModuleGenerator {

    private final Logger logger = LoggerFactory.getLogger(ProviderModuleGenerator.class);

    private ProviderConfig  config;
    private CommonGenerator commonGenerator;
    private ClassLoader     classLoader;

    public ProviderModuleGenerator(ProviderConfig config) {
        this.config = config;
        commonGenerator=new CommonGenerator();
    }


    private void createDir(){
        String providerDir = ProviderUtil.getProviderDir(this.config);
        FileUtil.createDir(providerDir);

        MavenDirUtil.createModuleMavenStructure(providerDir,this.config.getGroupId(),this.config.getName());

        for (String sub : Config.PROVIDER_SUB_DIRS) {
            String subDir = ProviderUtil.getProviderSourceCodeDir(this.config) + File.separator + sub;
            FileUtil.createDir(subDir);
        }
    }

    public void createProviderDir() {


        //createDubboFile(providerDir);

        createDir();

        commonGenerator.createLauncherFile(ProviderUtil.getProviderDir(this.config), this.config.getName() +
                        "." + this.config.getProviderDirName(),
                this.config.getName()+ "" +
                        SourceCodeUtil.getFirstUppercase(this.config.getProviderDirName()));

        ProviderHookUtil.createProviderHookFile(this.config);

        String moduleName = this.config.getName();

        // todo
        //createProviderPom();

        for (TableInfo t : ORMUtil.getTableList(config)) {
            t.setDomainName(MBGUtil.getDomainName(t));
        }

        ORMUtil.createOrm(config,moduleName, ProviderUtil.getProviderDir(this.config));

        //load denpendencies...
        //this.classLoader = ClassLoaderUtil.loadAllClass(new String[]{getProviderOrmDir(),getProviderPojoDir()});
        this.classLoader = ClassLoaderUtil.loadAllClass(getDir());

        if (ORMUtil.getTableList(config) != null) {

            DaoUtil.createDaoFiles(this.config,ORMUtil.getTableList(config),this.classLoader);

            DtoUtil.createDtoFromPojo(this.config,this.classLoader);

            this.classLoader = ClassLoaderUtil.loadAllClass(getDir());

            ProviderUtilGenerator.createUtilFiles(ORMUtil.getTableList(config),this.config,this.classLoader);
        }

        //update classloader
        this.classLoader = ClassLoaderUtil.loadAllClass(getDir());

        GeneratorContext.classLoader = this.classLoader;

        GeneratorContext.utilPackage = ProviderUtil.getProviderUtilPackage(this.config);

        createService();


        ApplicationPropertiesUtil.createApplicationPropertiesFile(this.config);

        SpringBootAutoUtil.createSpringBootAutoConfigFiles(ProviderUtil.getProviderDir(this.config),
                this.config.getSpringAutoConfigFullClassPath());

        createLogConfig();

        this.classLoader = ClassLoaderUtil.loadAllClass(getDir());

        UnitTestUtil.createTest(this.config,ProviderHookUtil.getProviderHookFullName(config),this.classLoader);

    }


    //todo
    private String getDir() {
        return this.config.getDir();
    }



    private void createProviderPom(String rootPomPath) {
        String providerDir = ProviderUtil.getProviderDir(this.config);
        String module = ProviderUtil.getProviderModuleName(this.config);
        String template = this.config.getServiceProviderPomTemplatePath();
        template = StringUtil.isEmpty(template) ? ConfigDefault.POM_TEMPLATE_PROVIDER : template;
        commonGenerator.createPomFile(null, rootPomPath, template, providerDir, module);
    }


    private void createLogConfig() {
        this.commonGenerator.createLogConfigFile(ProviderUtil.getProviderDir(this.config), this.config.getLogConfigFilePath());
    }


}
