package com.dafy.dev.generator.provider;

import com.dafy.dev.config.MybatisConfig;
import com.dafy.dev.config.provider.ProviderConfig;
import com.dafy.dev.generator.maven.MavenDirUtil;
import com.dafy.dev.pojo.TableInfo;
import com.dafy.dev.util.SourceCodeUtil;
import com.dafy.dev.util.StringUtil;

import java.util.List;

/**
 * Created by chunxiaoli on 5/23/17.
 */
public class MBGUtil {

    public static void createMybatisFiles(ProviderConfig config,List<TableInfo> tableList) {
        for (TableInfo tableItem : tableList) {
            createMybatisFile(config,tableItem);
        }
    }

    private static void createMybatisFile(ProviderConfig config, TableInfo tableItem) {
        String mavenBase = MavenDirUtil.getMavenBaseDir(ProviderUtil.getProviderDir(config));
        MybatisConfig cfg = generateMybatisConfig(config,tableItem, ProviderUtil.getProviderPackage(config));

        cfg.setProjectDir(mavenBase);

        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(cfg);

        myBatisGenerator.generate();
    }

    public static MybatisConfig generateMybatisConfig(ProviderConfig config, TableInfo table,
                                                      String packageRoot){
        MybatisConfig cfg= config.getMybatisConfig();

        cfg.setDaoPackage(packageRoot+".dao");
        cfg.setMapperPackage(packageRoot+".orm");
        cfg.setModelPackage(packageRoot+".pojo");
        cfg.setMapperXMLPackage("");

        cfg.setTable(table.getTableName());
        cfg.setDomainObjectName(table.getDomainName());
        return cfg;
    }

    public static String getDomainName(TableInfo t) {
        return StringUtil.isEmpty(t.getDomainName()) ?
                SourceCodeUtil.covertClassName(
                        t.getTableName().startsWith("t_") ? t.getTableName().substring(2,
                                t.getTableName().length()) : t.getTableName()) : t.getDomainName();
    }
}
