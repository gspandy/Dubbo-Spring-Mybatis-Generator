package com.dafy.dev.generator.provider;

import com.dafy.dev.config.ConfigDefault;
import com.dafy.dev.config.PomConfig;
import com.dafy.dev.config.api.ApiConfig;
import com.dafy.dev.config.provider.ProviderConfig;
import com.dafy.dev.generator.CommonGenerator;
import com.dafy.dev.generator.api.ApiModuleUtil;
import com.dafy.dev.generator.maven.PomGenerator;
import com.dafy.dev.util.StringUtil;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chunxiaoli on 5/26/17.
 */
public class PomUtil {

    private void createRootPom(ProviderConfig config){
        List<String> modules = new ArrayList<>();
        modules.add(ApiModuleUtil.getApiModuleName(config));
        modules.add(ProviderUtil.getProviderModuleName(config));
        //create root pom

        String template = config.getServiceParentPomTemplatePath();

        template = StringUtil.isEmpty(template) ? ConfigDefault.POM_TEMPLATE_ROOT : template;

       new CommonGenerator().createRootPomFile(modules, null, template, config.getDir(), config.getName());

    }

    public static void createProviderPom(ProviderConfig config, String rootPomPath) {
        String providerDir = ProviderUtil.getProviderDir(config);
        String module = ProviderUtil.getProviderModuleName(config);
        String template = config.getServiceProviderPomTemplatePath();
        template = StringUtil.isEmpty(template) ? ConfigDefault.POM_TEMPLATE_PROVIDER : template;
        new CommonGenerator().createPomFile(config,null, rootPomPath, template, providerDir, module);
    }

    public  static void createApiPom(ApiConfig config, String rootPomPath){

        String module = ApiModuleUtil.getApiModuleName(config);

        String template = config.getServiceApiPomTemplatePath();
        template = StringUtil.isEmpty(template) ? ConfigDefault.POM_TEMPLATE_API : template;
        new CommonGenerator().createPomFile(config,null,rootPomPath, template, ApiModuleUtil.getApiDir(config), module);
    }



    public void createPomFile(ProviderConfig config,List<String> modules, String parentTemplate,
                              String template, String dir, String moduleName) {
        //生成pom文件
        PomConfig cfg = ConfigDefault.getDefaultPomConfig(config, dir, moduleName);
        cfg.setTemplate(template);
        cfg.setParentTemplate(parentTemplate);
        cfg.setModules(modules);

        PomGenerator generator = new PomGenerator(cfg);

        Model model = generator.build().getModel();

        Dependency dependency = new Dependency();

        //增加api 依赖
        try {
            Model api = com.dafy.dev.util.PomUtil.read(new FileInputStream(""));
            assert api != null;
            if (api.getParent() != null) {
                dependency.setGroupId(api.getParent().getGroupId());
            } else if (api.getGroupId() != null) {
                dependency.setGroupId(api.getGroupId());
            } else {
            }

            dependency.setArtifactId(api.getArtifactId());
            dependency.setVersion(api.getVersion());

            model.addDependency(dependency);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //输出文件
        generator.write();
    }

    public static String getApiPomFile(PomConfig config){
        return config.getDir() + File.separator + "pom.xml";
    }
}
