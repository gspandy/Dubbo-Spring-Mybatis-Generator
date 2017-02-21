package com.dafy.dev.config;

import java.util.List;

/**
 * Created by chunxiaoli on 10/26/16.
 */
public class ServiceConfig {

    private String serviceInterfaceName;
    private String serviceInterfaceImplName;

    private String dtoPackageName;

    private String pojoPackageName;

    private Class dtoUtilCls;





    public Class getDtoUtilCls() {
        return dtoUtilCls;
    }

    public void setDtoUtilCls(Class dtoUtilCls) {
        this.dtoUtilCls = dtoUtilCls;
    }

    private List<Class> daoList;

    private List<ServiceDaoInfo> daoInfos;

    public List<Class> getDaoList() {
        return daoList;
    }

    public void setDaoList(List<Class> daoList) {
        this.daoList = daoList;
    }

    public String getServiceInterfaceName() {
        return serviceInterfaceName;
    }

    public void setServiceInterfaceName(String serviceInterfaceName) {
        this.serviceInterfaceName = serviceInterfaceName;
    }

    public String getServiceInterfaceImplName() {
        return serviceInterfaceImplName;
    }

    public void setServiceInterfaceImplName(String serviceInterfaceImplName) {
        this.serviceInterfaceImplName = serviceInterfaceImplName;
    }

    public String getDtoPackageName() {
        return dtoPackageName;
    }

    public void setDtoPackageName(String dtoPackageName) {
        this.dtoPackageName = dtoPackageName;
    }

    public List<ServiceDaoInfo> getDaoInfos() {
        return daoInfos;
    }

    public void setDaoInfos(List<ServiceDaoInfo> daoInfos) {
        this.daoInfos = daoInfos;
    }

    public String getPojoPackageName() {
        return pojoPackageName;
    }

    public void setPojoPackageName(String pojoPackageName) {
        this.pojoPackageName = pojoPackageName;
    }

    public static class ServiceDaoInfo{
        private Class dao;
        private Class pojo;

        public Class getDao() {
            return dao;
        }

        public void setDao(Class dao) {
            this.dao = dao;
        }

        public Class getPojo() {
            return pojo;
        }

        public void setPojo(Class pojo) {
            this.pojo = pojo;
        }
    }
}
