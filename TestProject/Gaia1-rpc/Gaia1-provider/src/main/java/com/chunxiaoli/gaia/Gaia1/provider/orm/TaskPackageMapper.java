package com.chunxiaoli.gaia.Gaia1.provider.orm;

import com.chunxiaoli.gaia.Gaia1.provider.pojo.TaskPackage;

public interface TaskPackageMapper {
    Integer insert(TaskPackage record);

    Integer insertSelective(TaskPackage record);

    TaskPackage selectByPrimaryKey(Integer id);

    Integer updateByPrimaryKeySelective(TaskPackage record);

    Integer updateByPrimaryKey(TaskPackage record);
}