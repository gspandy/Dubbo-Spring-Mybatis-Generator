package com.dafy.dev.test.provider.impl;

import com.dafy.dev.test.api.TestService;
import com.dafy.dev.test.api.dto.BrandDto;
import com.dafy.dev.test.api.dto.ListDto;
import com.dafy.dev.test.provider.dao.BrandDao;
import com.dafy.dev.test.provider.pojo.Brand;
import com.dafy.dev.test.provider.util.BrandDtoUtil;
import java.lang.Integer;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * test RPC interface impl */
@Service
public class TestServiceImpl implements TestService {
  private static final Logger logger = LoggerFactory.getLogger(TestServiceImpl.class);

  @Resource
  private BrandDao brandDao;

  public Integer brandInsert(BrandDto record) {
    Brand pojo=BrandDtoUtil.convertToPojo(record);
    return brandDao.insert(pojo);
  }

  public ListDto brandSearch() {
  }

  public Integer brandInsertSelective(BrandDto record) {
    Brand pojo=BrandDtoUtil.convertToPojo(record);
    return brandDao.insertSelective(pojo);
  }

  public BrandDto brandSelectByPrimaryKey(Integer id) {
    BrandDto dto=new BrandDto();
    Brand brand=brandDao.selectByPrimaryKey(id);
    dto=BrandDtoUtil.convertToDto(brand);
    return dto;
  }

  public Integer brandUpdateByPrimaryKey(BrandDto record) {
    Brand pojo=BrandDtoUtil.convertToPojo(record);
    return brandDao.updateByPrimaryKey(pojo);
  }

  public Integer brandUpdateByPrimaryKeySelective(BrandDto record) {
    Brand pojo=BrandDtoUtil.convertToPojo(record);
    return brandDao.updateByPrimaryKeySelective(pojo);
  }
}
