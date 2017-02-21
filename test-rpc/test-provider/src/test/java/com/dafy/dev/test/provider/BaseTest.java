package com.dafy.dev.test.provider;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * test case */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(
    classes = TestProviderHook.class
)
@Transactional
@TransactionConfiguration(
    defaultRollback = true
)
public abstract class BaseTest {
  private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
}
