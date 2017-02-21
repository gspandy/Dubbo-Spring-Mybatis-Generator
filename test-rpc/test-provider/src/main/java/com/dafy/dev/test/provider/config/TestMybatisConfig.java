package com.dafy.dev.test.provider.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * mybatis config */
@Configuration
@PropertySource("classpath:test-mybatis-config.properties")
@MapperScan("com.dafy.dev.test.provider.orm")
public class TestMybatisConfig {
}
