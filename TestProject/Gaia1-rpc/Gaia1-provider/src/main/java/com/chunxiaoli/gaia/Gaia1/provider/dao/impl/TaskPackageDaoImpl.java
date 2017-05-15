package com.chunxiaoli.gaia.Gaia1.provider.dao.impl;

import com.chunxiaoli.gaia.Gaia1.provider.orm.TaskPackageMapper;
import com.chunxiaoli.gaia.Gaia1.provider.pojo.TaskPackage;
import java.lang.Integer;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * dao impl */
@Component
public class TaskPackageDaoImpl {
  private static final Logger logger = LoggerFactory.getLogger(TaskPackageDaoImpl.class);

  @Resource
  private TaskPackageMapper taskPackageMapper;

  public Integer insert(TaskPackage record) {
    return taskPackageMapper.insert(record);
  }

  public Integer insertSelective(TaskPackage record) {
    return taskPackageMapper.insertSelective(record);
  }

  public TaskPackage selectByPrimaryKey(Integer id) {
    return taskPackageMapper.selectByPrimaryKey(id);
  }

  public Integer updateByPrimaryKey(TaskPackage record) {
    return taskPackageMapper.updateByPrimaryKey(record);
  }

  public Integer updateByPrimaryKeySelective(TaskPackage record) {
    return taskPackageMapper.updateByPrimaryKeySelective(record);
  }
}
