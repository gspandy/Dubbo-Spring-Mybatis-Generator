package com.dafy.dev.parser;

import com.dafy.dev.config.JavaFileConfig;
import com.dafy.dev.config.PojoConfig;
import com.dafy.dev.config.PomConfig;
import com.dafy.dev.config.ProjectConfig;
import org.codehaus.plexus.util.xml.pull.XmlPullParser;

/**
 * Created by chunxiaoli on 10/18/16.
 */
public class XmlConfigParser {

    public ProjectConfig parseProjectConfig(XmlPullParser parser, boolean strict){
        ProjectConfig config=new ProjectConfig();


        return config;
    }

    public PojoConfig parsePojoConfig(XmlPullParser parser, boolean strict){
        PojoConfig config=new PojoConfig();
        return config;
    }

    public JavaFileConfig parseJavaFileConfig(XmlPullParser parser, boolean strict){
        JavaFileConfig config=new JavaFileConfig();
        return config;
    }

    public PomConfig parsePomConfig(XmlPullParser parser, boolean strict){
        PomConfig config=new PomConfig();
        return config;
    }

}
