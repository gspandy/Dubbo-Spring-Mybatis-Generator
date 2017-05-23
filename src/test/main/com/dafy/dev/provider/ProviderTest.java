package com.dafy.dev.provider;

import com.dafy.dev.config.provider.ProviderConfig;
import com.dafy.dev.generator.provider.ApplicationPropertiesUtil;
import org.junit.Test;

/**
 * Created by chunxiaoli on 5/23/17.
 */
public class ProviderTest {

    final static ProviderConfig config = new ProviderConfig();

    static {
        config.setDir(".");
        config.setName("test");
        config.setGroupId("com.chunxiaoli.abc");
    }


    @Test
    public void testProviderGenerator(){

    }

    @Test
    public void testApplicationConfigUtil(){
        ApplicationPropertiesUtil.createApplicationPropertiesFile(config);
    }
}
