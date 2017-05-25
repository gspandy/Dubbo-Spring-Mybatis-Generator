package com.dafy.dev.generator.provider;

import com.dafy.dev.codegen.ClassLoaderUtil;
import com.dafy.dev.config.DtoConfig;
import com.dafy.dev.config.DtoUtilConfig;
import com.dafy.dev.config.provider.ProviderConfig;
import com.dafy.dev.generator.api.ApiModuleUtil;
import com.dafy.dev.generator.project.DtoFileGenerator;
import com.dafy.dev.generator.project.DtoUtilFileGenerator;
import com.dafy.dev.pojo.TableInfo;
import com.dafy.dev.util.SourceCodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * Created by chunxiaoli on 5/23/17.
 */
public class DtoUtil {

    private final static Logger logger = LoggerFactory.getLogger(ProviderModuleGenerator.class);

    public static void createDtoFromPojo(ProviderConfig config, ClassLoader classLoader) {
        String dir = ProviderUtil.getProviderPojoDir(config);
        File f = new File(dir);
        if (f.exists()) {
            File[] files = f.listFiles();
            if(files!=null){
                for (File file : files) {
                    if (file.isFile() && file.getPath().endsWith(".java")) {
                        Class pojoCls = ClassLoaderUtil.loadClass(file.getPath(),classLoader);
                        String apiBaseDir = ApiModuleUtil.getApiBaseDir(config);
                        DtoConfig dtoConfig = new DtoConfig();
                        dtoConfig.setDir(apiBaseDir);
                        dtoConfig.setPackageName(ApiModuleUtil.getApiDtoPackage(config));

                        String daoDir = ProviderUtil.getDaoPackage(config);

                        String targetClassFullName =
                                ApiModuleUtil.getApiDtoPackage(config) + "." + file.getName().replace(".java",
                                        "") + "Dto";

                        new DtoFileGenerator(dtoConfig).generateFromPojoClass(pojoCls, targetClassFullName);
                    }
                }
            }else {
                logger.error("get provider pojo files empty or null...:{}",ProviderUtil.getProviderPojoDir(config));
            }

        }

    }

    public static void createUtilFiles(ProviderConfig config, ClassLoader classLoader,List<TableInfo> tableInfos) {
        for (TableInfo t :tableInfos) {
            String pojoName = SourceCodeUtil.covertClassName(t.getDomainName());
            String pojoClsFullName =ProviderUtil.getPojoPackage(config) + "." + pojoName;
            String dtoClsFullName =
                    ApiModuleUtil.getApiDtoPackage(config) + "." + pojoName + "Dto";

            Class pojoCls = ClassLoaderUtil.load(pojoClsFullName, classLoader);
            Class dtoCls = ClassLoaderUtil.load(dtoClsFullName, classLoader);

            DtoUtilConfig cfg = new DtoUtilConfig();
            cfg.setPackageName(ProviderUtil.getProviderUtilPackage(config));
            cfg.setOutDir(ProviderUtil.getProviderBaseDir(config));

            new DtoUtilFileGenerator(cfg).generateDtoUtilFile(pojoCls, dtoCls);
        }

    }
}
