package com.dafy.dev.generator.provider;

import com.dafy.dev.config.JavaFileConfig;
import com.dafy.dev.config.MybatisConfig;
import com.dafy.dev.config.PropertyConfig;
import com.dafy.dev.config.provider.ProviderConfig;
import com.dafy.dev.generator.SpringBootDubboJavaFileGenerator;
import com.dafy.dev.generator.common.PropertiesGenerator;
import com.dafy.dev.generator.maven.MavenDirUtil;
import com.dafy.dev.pojo.TableInfo;
import com.dafy.dev.util.SourceCodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by chunxiaoli on 5/23/17.
 */
public class MybatisUtil {

    private final static Logger logger = LoggerFactory.getLogger(MybatisUtil.class);

    //生成 config/module-mybatis-config.properties文件
    public static void createMybatisPropertiesFile(ProviderConfig config) {
        List<String> tables = getTableList(config.getMybatisConfig());
        String base = MavenDirUtil.getResourceBaseDir(ProviderUtil.getProviderDir(config));
        String path = base + File.separator + getMybatisPropertiesConfigPath(config.getName());
        PropertyConfig propertyConfig = new PropertyConfig();
        propertyConfig.setPath(path);

        PropertiesGenerator generator = new PropertiesGenerator(propertyConfig);

        /*com.dafy.mybatis.typeAlias.account=com.dafy.collection.account.provider.pojo
        com.dafy.mybatis.mapper.account=mybatis/AccountMapper.xml*/

        generator.set("com.dafy.mybatis.typeAlias." + config.getName(), ProviderUtil.getProviderPackage(config) + "." +
                config.getPojoDirName());


        if(tables!=null){
            for (String table : tables) {
                String domain = SourceCodeUtil
                        .uppercase(table.startsWith("t_") ? table.substring(2, table.length()) : table, true);
                generator.set("com.dafy.mybatis.mapper." + domain,
                        "mybatis/" + ProviderUtil.getMapperXmlName(config,domain));
            }
        }else {
            logger.error("table list is null or empty,please check your mybatis config");
        }


        generator.generate();
    }

    private static String getMybatisPropertiesConfigPath(String module) {
        return module + "-mybatis-config.properties";
    }

    public static void createMybatisConfig(ProviderConfig config,String moduleName, String classNamePrefix, String dir) {

        String propertySourceValue = "classpath:" + getMybatisPropertiesConfigPath(moduleName);

        String mapperScanValue = config.getGroupId() + "." + moduleName + "." +
                config.getProviderDirName() + "."
                + config.getOrmDirName();
        JavaFileConfig cfg = new JavaFileConfig();
        cfg.setJavaFileDoc("mybatis config");
        cfg.setClassName(SourceCodeUtil.convertFieldUppercase(classNamePrefix));
        cfg.setPackageName(ProviderUtil.getProviderPackage(config) + ".config");
        cfg.setOutDir(MavenDirUtil.getMavenSourceCodeDir(dir));
        new SpringBootDubboJavaFileGenerator(cfg)
                .generateMybatisConfigFile(propertySourceValue, mapperScanValue);
    }

    public static void createOrm(ProviderConfig config,String moduleName, String providerDir) {
        createMybatisConfig(config,moduleName, moduleName, providerDir);
        //todo
        if (getTableList(config) != null) {
            MBGUtil.createMybatisFiles(config,getTableList(config));
            MybatisUtil.createMybatisPropertiesFile(config);
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
            List<TableInfo> ret= config.getMybatisConfig().getTableInfoList();
            for (TableInfo t : ret) {
                t.setDomainName(MBGUtil.getDomainName(t));
            }
            return ret;
        }
        return new ArrayList<>();
    }
}
