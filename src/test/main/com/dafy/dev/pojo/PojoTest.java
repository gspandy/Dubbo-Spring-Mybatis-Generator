package com.dafy.dev.pojo;

import com.dafy.dev.config.PojoConfig;
import com.dafy.dev.data.User;
import com.dafy.dev.generator.PojoFileGenerator;
import org.junit.Test;

/**
 * Created by chunxiaoli on 5/2/17.
 */
public class PojoTest {
    @Test
    public void testCreatePojo(){
        PojoConfig pojoConfig = new PojoConfig();
        pojoConfig.setOutDir("");
        pojoConfig.setPackageName("abc");
        pojoConfig.setSerializable(true);
        pojoConfig.setToString(true);
        new PojoFileGenerator(pojoConfig).generate(User.class,"UserDto");
    }

    @Test
    public void testCreatePojo1(){
        PojoConfig pojoConfig = new PojoConfig();
        pojoConfig.setOutDir("");
        pojoConfig.setPackageName("abc");
        new PojoFileGenerator(pojoConfig).generate(User.class,"UserDto");
    }
}
