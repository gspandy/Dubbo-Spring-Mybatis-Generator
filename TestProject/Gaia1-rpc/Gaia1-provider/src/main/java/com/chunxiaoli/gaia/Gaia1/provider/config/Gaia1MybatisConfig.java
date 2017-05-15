package com.chunxiaoli.gaia.Gaia1.provider.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * mybatis config */
@Configuration
@PropertySource("classpath:Gaia1-mybatis-config.properties")
@MapperScan("com.chunxiaoli.gaia.Gaia1.provider.orm")
public class Gaia1MybatisConfig {
}
