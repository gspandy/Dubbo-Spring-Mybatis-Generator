package com.dafy.dev.generator.provider;

import com.dafy.dev.codegen.ClassLoaderUtil;
import com.dafy.dev.config.JavaFileConfig;
import com.dafy.dev.config.provider.ProviderConfig;
import com.dafy.dev.generator.SpringBootDubboJavaFileGenerator;
import com.dafy.dev.generator.api.ApiModuleUtil;
import com.dafy.dev.generator.maven.MavenDirUtil;
import com.dafy.dev.util.FileUtil;
import com.dafy.dev.util.SourceCodeUtil;

import java.io.File;

/**
 * Created by chunxiaoli on 5/23/17.
 */
public class UnitTestUtil {


    //生成测试文件
    public static void createTest(ProviderConfig config,String providerHookFullName,ClassLoader classLoader) {
        String rootDir =ProviderUtil.getProviderDir(config);
        String sourceCodeDir = MavenDirUtil.getMavenTestSourceCodeDir(rootDir);
        FileUtil.createDir(MavenDirUtil.getMavenTestBaseDir(rootDir));
        FileUtil.createDir(MavenDirUtil.getMavenTestResourceDir(rootDir));
        FileUtil.createDir(sourceCodeDir);

        String packageName = ProviderUtil.getProviderPackage(config);

        String baseTestClass = "BaseTest";

        JavaFileConfig serviceDaoCfg = new JavaFileConfig();
        serviceDaoCfg.setJavaFileDoc("test case");
        serviceDaoCfg.setClassName(baseTestClass);
        serviceDaoCfg.setPackageName(packageName);

        serviceDaoCfg.setOutDir(sourceCodeDir);

        new SpringBootDubboJavaFileGenerator(serviceDaoCfg)
                .generateBaseTestCaseFile(packageName + "." + baseTestClass,providerHookFullName);

        String serviceFullClassName = ApiModuleUtil.getServiceFullClassName(config);

        String servicePath = MavenDirUtil.getMavenSourceCodeDir(ApiModuleUtil.getApiDir(config)) + File.separator
                + SourceCodeUtil.convertPackage2Dir(serviceFullClassName) + ".java";

        Class serviceCls = ClassLoaderUtil.loadClass(servicePath,classLoader);

        new SpringBootDubboJavaFileGenerator(serviceDaoCfg)
                .generateTestCaseFile(packageName + "." + baseTestClass,
                        serviceFullClassName, serviceCls);

    }
}
