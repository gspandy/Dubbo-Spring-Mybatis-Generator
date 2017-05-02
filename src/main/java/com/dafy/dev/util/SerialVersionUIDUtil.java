package com.dafy.dev.util;

import java.util.Random;

/**
 * Created by chunxiaoli on 5/2/17.
 */
public class SerialVersionUIDUtil {
    public static long  generate(){
        return -new Random().nextLong();
    }
}
