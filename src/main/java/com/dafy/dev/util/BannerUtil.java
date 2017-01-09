package com.dafy.dev.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chunxiaoli on 10/19/16.
 */
public class BannerUtil {
    private static final Logger logger= LoggerFactory.getLogger(BannerUtil.class);

    public static void log(String msg){
        logger.debug("-------------------------------------------------{}---------------------------------------------------------",msg);
    }
}
