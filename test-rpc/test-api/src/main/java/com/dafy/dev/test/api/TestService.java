package com.dafy.dev.test.api;

import com.dafy.dev.test.api.dto.BrandDto;
import com.dafy.dev.test.api.dto.ListDto;
import java.lang.Integer;

/**
 * test RPC interface */
public interface TestService {
  Integer brandInsert(BrandDto record);

  ListDto brandSearch();

  Integer brandInsertSelective(BrandDto record);

  BrandDto brandSelectByPrimaryKey(Integer id);

  Integer brandUpdateByPrimaryKey(BrandDto record);

  Integer brandUpdateByPrimaryKeySelective(BrandDto record);
}
