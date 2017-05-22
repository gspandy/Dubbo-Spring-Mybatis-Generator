package com.dafy.dev.generator.maven;

import com.dafy.dev.config.Config;
import com.dafy.dev.util.FileUtil;
import com.dafy.dev.util.PackageUtil;
import com.dafy.dev.util.StringUtil;
import com.dafy.dev.util.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.File;

/**
 * Created by chunxiaoli on 5/18/17.
 */
public class MavenDirUtil {

    private static final Logger logger = LoggerFactory.getLogger(MavenDirUtil.class);

    public static String getMavenTestBaseDir(String parentDir) {
        return parentDir + Config.MAVEN_SRC_DIR_PREFIX + File.separator + "test";
    }

    public static String getMavenTestSourceCodeDir(String parentDir) {
        return getMavenTestBaseDir(parentDir) + File.separator + "java" + File.separator;
    }

    public static String getMavenTestResourceDir(String parentDir) {
        return getMavenTestBaseDir(parentDir) + File.separator + "resources";
    }

    //parentDir/src/main
    public static String getMavenBaseDir(String parentDir) {
        return parentDir + Config.MAVEN_MAIN_DIR_PREFIX;
    }

    //maven java目录 parentDir/src/main/java
    public static String getMavenSourceCodeDir(String parentDir) {
        String dir = getMavenBaseDir(parentDir) + File.separator + "java" + File.separator;
        logger.debug("maven code dir:{}", dir);
        return dir;
    }

    //源代码根目录 parentDir/src/main/java/com/dafy/dev
    public static String getSourceCodeBaseDirOfGroup(String parentDir,String groupId) {
        Assert.isTrue(ValidateUtil.isGroupId(groupId));
        return getSourceCodeBaseDirOfGroup(parentDir, groupId, "");
    }

    //源代码根目录 parentDir/src/main/java/group/moduleDir
    public static String getSourceCodeBaseDirOfGroup(String parentDir,String groupId, String moduleDir) {
        Assert.isTrue(ValidateUtil.isGroupId(groupId));
        String baseDir = getMavenSourceCodeDir(parentDir) + getBasePackageDir(groupId,moduleDir);
        return baseDir;
    }

    //资源根目录 parentDir/src/main/java/resources
    public static String getResourceBaseDir(String parentDir) {
        String resourceDir = getMavenBaseDir(parentDir) + File.separator + "resources";
        return resourceDir;
    }

    // groupId/subDir
    public static String getBasePackageDir(String groupId,String subDir) {
        Assert.isTrue(ValidateUtil.isGroupId(groupId));
        return PackageUtil
                .convertPackage2Path(groupId + (StringUtil.isEmpty(subDir) ? "" : "." + subDir));
    }



    //com.dafy.dev.subDir
    public static String getBasePackage(String groupId,String subDir) {
        Assert.isTrue(ValidateUtil.isGroupId(groupId));
        return groupId + (StringUtil.isEmpty(subDir) ? "" : "." + subDir);
    }



    //maven project structure
    public static void createBaseMavenStructure(String parentDir) {
        FileUtil.createDir(getMavenSourceCodeDir(parentDir));
        createResources(parentDir);
    }

    //maven project structure
    public static void createModuleMavenStructure(String parentDir,String group,String module) {
        FileUtil.createDir(getSourceCodeBaseDirOfGroup(parentDir,group, module));
        createResources(parentDir);
    }

    private static void createResources(String parentDir) {
        logger.debug("createResources...:{}", parentDir);
        String base = getResourceBaseDir(parentDir);

        for (String sub : Config.RESOURCES_SUB_DIRS) {
            String subDir = base + File.separator + sub;
            FileUtil.createDir(subDir);
        }
        //createPropertiesFile(parentDir);
    }


    public static String getBaseServerPackageDir(String groupId,String subDir) {
        Assert.isTrue(ValidateUtil.isGroupId(groupId));
        return PackageUtil.convertPackage2Path(groupId + (".server." + subDir));
    }

    public static String getBaseServerPackage( String groupId,String subDir) {
        Assert.isTrue(ValidateUtil.isGroupId(groupId));
        return groupId + (".server." + subDir);
    }
}
