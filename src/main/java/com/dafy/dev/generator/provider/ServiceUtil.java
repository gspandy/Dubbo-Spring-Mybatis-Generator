package com.dafy.dev.generator.provider;

import com.dafy.dev.codegen.ClassLoaderUtil;
import com.dafy.dev.config.JavaFileConfig;
import com.dafy.dev.config.ServiceConfig;
import com.dafy.dev.config.provider.ProviderConfig;
import com.dafy.dev.generator.SpringBootDubboJavaFileGenerator;
import com.dafy.dev.generator.api.ApiModuleUtil;
import com.dafy.dev.generator.maven.MavenDirUtil;
import com.dafy.dev.generator.project.ServiceFileGenerator;
import com.dafy.dev.pojo.TableInfo;
import com.dafy.dev.util.CodeGenUtil;
import com.dafy.dev.util.SourceCodeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chunxiaoli on 5/25/17.
 */
public class ServiceUtil {

    //生成api service
    public static void createService(ProviderConfig config, ClassLoader classLoader) {
        JavaFileConfig cfg = new JavaFileConfig();
        cfg.setJavaFileDoc(config.getName() + " RPC interface");
        cfg.setClassName(ProviderUtil.getServiceName(config));
        cfg.setPackageName(ApiModuleUtil.getApiPackage(config));
        cfg.setOutDir(MavenDirUtil.getMavenSourceCodeDir(ApiModuleUtil.getApiDir(config)));

        List<ServiceConfig.ServiceDaoInfo> list = getDaoInfoList(config, classLoader);

        ServiceConfig serviceConfig = new ServiceConfig();

        serviceConfig.setDtoPackageName(ProviderUtil.getDtoPackage(config));
        //serviceConfig.setDaoList(daoCls);
        serviceConfig.setDaoInfos(list);

        final String utilPackage = ProviderUtil.getProviderUtilPackage(config);

        new ServiceFileGenerator(cfg, serviceConfig)
                .generateServiceInterfaceFile(classLoader, utilPackage);

        JavaFileConfig implCfg = new JavaFileConfig();
        implCfg.setJavaFileDoc(config.getName() + " RPC interface impl");
        implCfg.setClassName(ProviderUtil.getServiceName(config) + "Impl");
        implCfg.setPackageName(ProviderUtil.getProviderPackage(config) + ".impl");
        implCfg.setOutDir(MavenDirUtil.getMavenSourceCodeDir(ProviderUtil.getProviderDir(config)));

        String serviceFullName = ApiModuleUtil.getApiServicePackage(config) + "." + ProviderUtil
                .getServiceName(config);

        new ServiceFileGenerator(implCfg, serviceConfig)
                .generateServiceImplFile(serviceFullName, classLoader, utilPackage);
    }

    //生成serviceImpl文件
    private void createServiceFiles(ProviderConfig config) {
        String baseDir = ProviderUtil.getProviderBaseDir(config);

        //service
        String servicePackage = ApiModuleUtil.getApiPackage(config);

        String serviceImplPackage = ProviderUtil.getProviderPackage(config) + ".impl";

        String serviceImplClassName = ProviderUtil.getServiceName(config) + "Impl";

        String serviceInterface = "";

        JavaFileConfig serviceDaoCfg = new JavaFileConfig();
        serviceDaoCfg.setJavaFileDoc("rpc service impl");
        serviceDaoCfg.setClassName(serviceImplClassName);
        serviceDaoCfg.setPackageName(serviceImplPackage);

        serviceDaoCfg.setOutDir(baseDir);

        List<String> daos = new ArrayList<>();

        for (TableInfo item : MybatisUtil.getTableList(config)) {
            String table = item.getTableName();
            String entity = SourceCodeUtil.jsontoUpperCase(
                    table.startsWith("t_") ? table.substring(2, table.length()) : table);
            daos.add(DaoUtil.getDaoFullName(config, entity));
        }

        new SpringBootDubboJavaFileGenerator(serviceDaoCfg)
                .generateServiceImplFile(servicePackage + "." + ProviderUtil.getServiceName(config),
                        serviceImplPackage + "." + serviceImplClassName, daos);

    }

    public static List<ServiceConfig.ServiceDaoInfo> getDaoInfoList(ProviderConfig config,
                                                                    ClassLoader classLoader) {

       /* if (this.serviceDaoInfos != null) {
            return this.serviceDaoInfos;
        }*/
        List<ServiceConfig.ServiceDaoInfo> serviceDaoInfos = new ArrayList<>();
        List<Class> daoList = CodeGenUtil
                .loadClassFromDir(ProviderUtil.getProviderDaoDir(config), classLoader);
        daoList.forEach(item -> {
            String name = item.getSimpleName();
            if (name.endsWith("Dao")) {
                name = name.replace("Dao", "");
                Class pojo = ClassLoaderUtil
                        .loadClass(ProviderUtil.getProviderPojoDir(config) + File.separator + name
                                        + ".java",
                                classLoader);
                ServiceConfig.ServiceDaoInfo info = new ServiceConfig.ServiceDaoInfo();
                info.setDao(item);
                info.setPojo(pojo);
                serviceDaoInfos.add(info);
            }
        });

        return serviceDaoInfos;
    }
}
