package com.dafy.dev.generator.api;

import com.dafy.dev.config.Config;
import com.dafy.dev.config.ConfigDefault;
import com.dafy.dev.config.api.ApiConfig;
import com.dafy.dev.generator.CommonGenerator;
import com.dafy.dev.generator.maven.MavenDirUtil;
import com.dafy.dev.util.FileUtil;
import com.dafy.dev.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by chunxiaoli on 5/19/17.
 */
public class ApiModuleGenerator {

    private final Logger logger = LoggerFactory.getLogger(ApiModuleGenerator.class);

    private ApiConfig       apiConfig;
    private CommonGenerator commonGenerator;

    public ApiModuleGenerator(ApiConfig config) {
        this.apiConfig = config;
        commonGenerator=new CommonGenerator();
    }

    @SuppressWarnings("unchecked")
    public void createApiDir(String rootPomPath) {
        logger.info("createApiDir:{}");
        String apiDir = ApiModuleUtil.getApiDir(this.apiConfig);
        //创建maven结构
        MavenDirUtil.createBaseMavenStructure(apiDir);

        for (String sub : Config.API_SUB_DIRS) {
            String subDir = ApiModuleUtil.getApiSourceCodeDir(this.apiConfig) + File.separator + sub;
            FileUtil.createDir(subDir);
        }
        createApiPom(rootPomPath);
    }

    private void createApiPom(String rootPomPath) {
        String module = ApiModuleUtil.getApiModuleName(this.apiConfig);
        String template = this.apiConfig.getServiceApiPomTemplatePath();
        template = StringUtil.isEmpty(template) ? ConfigDefault.POM_TEMPLATE_API : template;
        commonGenerator.createPomFile(rootPomPath, template, ApiModuleUtil.getApiDir(this.apiConfig), module);
    }
}
