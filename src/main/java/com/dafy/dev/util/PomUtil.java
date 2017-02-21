package com.dafy.dev.util;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chunxiaoli on 10/28/16.
 */
public class PomUtil {

    public static Model read(InputStream inputStream) {
        try {

            return new MavenXpp3Reader().read(inputStream);

        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }
}
