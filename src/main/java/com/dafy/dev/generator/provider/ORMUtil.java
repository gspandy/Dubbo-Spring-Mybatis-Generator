package com.dafy.dev.generator.provider;

import com.dafy.dev.config.MybatisConfig;
import com.dafy.dev.config.provider.ProviderConfig;
import com.dafy.dev.pojo.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by chunxiaoli on 5/23/17.
 */
public class ORMUtil {

    private final static Logger logger = LoggerFactory.getLogger(ProviderModuleGenerator.class);

    public static void createOrm(ProviderConfig config,String moduleName, String providerDir) {
        MybatisConfigUtil.createMybatisConfig(config,moduleName, moduleName, providerDir);

        //todo
        if (getTableList(config) != null) {
            MBGUtil.createMybatisFiles(config,getTableList(config));
            MybatisConfigUtil.createMybatisPropertiesFile(config,
                    getTableList(config.getMybatisConfig()),
                    ProviderUtil.getProviderDir(config));
        } else {
            logger.warn("table list is null....");
        }

    }



    public static List<String> getTableList(MybatisConfig mybatisConfig){
        List<TableInfo> list= mybatisConfig.getTableInfoList();
        return list!=null?list.stream().map(TableInfo::getTableName).collect(Collectors.toList()):null;
    }




    public static List<TableInfo> getTableList(ProviderConfig config){
        if(config.getMybatisConfig()!=null){
            return config.getMybatisConfig().getTableInfoList();
        }
        return new ArrayList<>();
    }
}
