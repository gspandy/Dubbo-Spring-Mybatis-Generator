package com.dafy.dev.test.provider.dao;

import com.dafy.dev.test.provider.orm.BrandMapper;
import com.dafy.dev.test.provider.pojo.Brand;
import java.lang.Integer;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * dao */
@Component
public class BrandDao {
  private static final Logger logger = LoggerFactory.getLogger(BrandDao.class);

  @Resource
  private BrandMapper brandMapper;

  public Integer insert(Brand record) {
    return brandMapper.insert(record);
  }

  public List search() {
    return brandMapper.search();
  }

  public Integer insertSelective(Brand record) {
    return brandMapper.insertSelective(record);
  }

  public Brand selectByPrimaryKey(Integer id) {
    return brandMapper.selectByPrimaryKey(id);
  }

  public Integer updateByPrimaryKey(Brand record) {
    return brandMapper.updateByPrimaryKey(record);
  }

  public Integer updateByPrimaryKeySelective(Brand record) {
    return brandMapper.updateByPrimaryKeySelective(record);
  }
}
