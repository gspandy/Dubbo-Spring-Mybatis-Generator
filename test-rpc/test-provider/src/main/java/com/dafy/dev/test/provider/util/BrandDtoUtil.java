package com.dafy.dev.test.provider.util;

import com.dafy.dev.test.api.dto.BrandDto;
import com.dafy.dev.test.provider.pojo.Brand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrandDtoUtil {
  private static final Logger logger = LoggerFactory.getLogger(BrandDtoUtil.class);

  public static BrandDto convertToDto(Brand obj) {
    logger.debug("pojo:{}",obj);
    BrandDto ret=new BrandDto();
    ret.setId(obj.getId());
    ret.setBrandName(obj.getBrandName());
    ret.setBrandLogo(obj.getBrandLogo());
    ret.setBrandDesc(obj.getBrandDesc());
    ret.setSiteUrl(obj.getSiteUrl());
    ret.setSortOrder(obj.getSortOrder());
    ret.setIsShow(obj.getIsShow());
    ret.setState(obj.getState());
    ret.setCreateTime(obj.getCreateTime());
    ret.setUpdateTime(obj.getUpdateTime());
    return ret;
  }

  public static Brand convertToPojo(BrandDto obj) {
    logger.debug("pojo:{}",obj);
    Brand ret=new Brand();
    ret.setId(obj.getId());
    ret.setBrandName(obj.getBrandName());
    ret.setBrandLogo(obj.getBrandLogo());
    ret.setBrandDesc(obj.getBrandDesc());
    ret.setSiteUrl(obj.getSiteUrl());
    ret.setSortOrder(obj.getSortOrder());
    ret.setIsShow(obj.getIsShow());
    ret.setState(obj.getState());
    ret.setCreateTime(obj.getCreateTime());
    ret.setUpdateTime(obj.getUpdateTime());
    return ret;
  }
}
