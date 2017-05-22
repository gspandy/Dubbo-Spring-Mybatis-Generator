package com.dafy.dev.generator.maven;

import com.dafy.dev.config.PomConfig;
import com.dafy.dev.config.ProjectConfig;
import com.dafy.dev.util.StringUtil;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chunxiaoli on 10/12/16.
 */
public class PomGenerator {


    private static final Logger logger = LoggerFactory.getLogger(PomGenerator.class);

    private MavenXpp3Writer mavenXpp3Writer;
    private MavenXpp3Reader mavenXpp3Reader;
    private PomConfig config;
    private ProjectConfig parentConfig;

    private Model parent;

    private Model defaultConfig;

    private String outputPomFilePath;

    private Model model;

    public PomGenerator(ProjectConfig projectConfig, PomConfig config) {
        this.parentConfig = projectConfig;
        this.config = config;
        this.mavenXpp3Writer = new MavenXpp3Writer();
        this.mavenXpp3Reader = new MavenXpp3Reader();
        this.outputPomFilePath = config.getDir() + File.separator + "pom.xml";
        generateConfigFromTemplate();
    }

    public void generate() {
        build();
        write();
    }

    public PomGenerator build() {
        this.model = new Model();

        generateMain(model);

        generateParent(model);

        generateDependencies(model);

        generateModules(model);
        return this;
    }

    private void generateConfigFromTemplate() {
        InputStream in = getPomTemplateInputStream(this.config.getTemplate());
        InputStream parentIn = geParentPomTemplateInputStream(this.config.getParentTemplate());
        try {
            this.defaultConfig = mavenXpp3Reader.read(in);
            if (parentIn != null) {
                this.parent = mavenXpp3Reader.read(parentIn);
                this.parent.setGroupId(this.config.getGroupId());
            }

        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
    }


    private void generateParent(Model model) {
        if (this.parent != null) {
            Parent parent = new Parent();
            parent.setArtifactId(this.parent.getArtifactId());
            parent.setGroupId(this.parent.getGroupId());
            parent.setVersion(this.parent.getVersion());
            model.setParent(parent);
        }
    }


    //生成主pom文件
    private void generateMain(Model model) {
        if (this.parent == null) {
            model.setGroupId(config.getGroupId());
        }
        model.setArtifactId(config.getArtifactId());
        model.setVersion(config.getVersion());
        model.setModelVersion("4.0.0");
        if (config.getPackaging() != null) {
            model.setPackaging(config.getPackaging());
        }

    }

    //生成子模块
    private void generateModules(Model model) {
        if (this.config.getModules() != null) {
            List<String> modules = new ArrayList<>();
            for (String module : this.config.getModules()) {
                modules.add(module);
            }
            model.setModules(modules);
        } else {
            logger.info("no modules...");
        }

    }

    private void generateDependencies(Model model) {
        List<Dependency> dependencies = this.defaultConfig.getDependencies();
        for (Dependency dependency : dependencies) {
            model.addDependency(dependency);
        }
    }

    public void write() {
        OutputStream out = getPomFileOutputStream();
        try {
            mavenXpp3Writer.write(out, model);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private OutputStream getPomFileOutputStream() {
        OutputStream outputStream = null;
        logger.debug("pom file path:{}", outputPomFilePath);
        try {
            outputStream = new FileOutputStream(outputPomFilePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return outputStream;
    }

    private InputStream getPomTemplateInputStream(String template) {
        if (!StringUtil.isEmpty(template)) {
            InputStream inputStream = null;
            try {
                inputStream=new FileInputStream(template);
            } catch (FileNotFoundException e) {
                //e.printStackTrace();
                logger.warn("pom template not found,try to use default...");
                inputStream = getClass().getClassLoader().getResourceAsStream(template);
            }
            return inputStream;
        }
        return null;
    }

    private InputStream geParentPomTemplateInputStream(String template) {
        if (!StringUtil.isEmpty(template)) {
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(template);
            } catch (FileNotFoundException e) {
                //e.printStackTrace();
                logger.warn("pom template not found,try to use default...");
                inputStream = getClass().getClassLoader().getResourceAsStream(template);
            }
            return inputStream;
        }
        return null;
    }

    public String getOutputPomFilePath() {
        return outputPomFilePath;
    }

    public Model getModel() {
        return model;
    }
}
