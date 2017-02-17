package com.dafy.dev.test.provider.orm;

import com.dafy.dev.test.provider.pojo.Brand;
import java.util.List;

public interface BrandMapper {
    Integer insert(Brand record);

    Integer insertSelective(Brand record);

    Brand selectByPrimaryKey(Integer id);

    Integer updateByPrimaryKeySelective(Brand record);

    Integer updateByPrimaryKey(Brand record);

    List<Brand> search();
}