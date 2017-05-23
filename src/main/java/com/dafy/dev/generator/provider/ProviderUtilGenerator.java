package com.dafy.dev.generator.provider;

import com.dafy.dev.codegen.ClassLoaderUtil;
import com.dafy.dev.config.DtoUtilConfig;
import com.dafy.dev.config.provider.ProviderConfig;
import com.dafy.dev.generator.api.ApiModuleUtil;
import com.dafy.dev.generator.project.DtoUtilFileGenerator;
import com.dafy.dev.pojo.TableInfo;
import com.dafy.dev.util.SourceCodeUtil;

import java.util.List;

/**
 * Created by chunxiaoli on 5/23/17.
 */
public class ProviderUtilGenerator {
    public static void createUtilFiles(List<TableInfo> tableInfos, ProviderConfig config,ClassLoader classLoader) {
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
