package com.dafy.dev.project;

import com.dafy.dev.config.ProjectConfig;
import com.dafy.dev.generator.ProjectGenerator;
import org.junit.Test;

/**
 * Created by chunxiaoli on 5/9/17.
 */
public class ProjectGeneratorTest {

    final static   ProjectConfig projectConfig = new ProjectConfig();
    final static   ProjectGenerator projectGenerator = new ProjectGenerator();

    static {
        projectConfig.setDir("");
        projectConfig.setDir("abc");

        projectGenerator.setConfig(projectConfig);
    }

    @Test
    public void testProjectGenerator(){
        projectGenerator.getProjectDir();
    }


}
