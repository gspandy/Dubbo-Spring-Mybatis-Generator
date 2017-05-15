package com.chunxiaoli.gaia.Gaia1.provider;

import java.lang.Runnable;
import java.lang.String;
import java.lang.System;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 测试接口 */
@SpringBootApplication
public class Gaia1ProviderLauncher {
  private static final Logger logger = LoggerFactory.getLogger(Gaia1ProviderLauncher.class);

  public static void main(String[] args) {
    logger.debug("Gaia1ProviderLauncher start:{}",args);
    System.out.println("Gaia1ProviderLauncher start.....");
    ThreadPoolTaskScheduler threadPoolTaskScheduler =new ThreadPoolTaskScheduler();
    threadPoolTaskScheduler.initialize();
    threadPoolTaskScheduler.setPoolSize(1);
    threadPoolTaskScheduler.scheduleAtFixedRate(new Runnable() {
      public void run() {
      }
    },new Date(),60*1000L);
    SpringApplication springApplication=new SpringApplication(Gaia1ProviderLauncher.class);
    springApplication.addListeners(new ApplicationPidFileWriter());
    springApplication.run(args);
  }
}
