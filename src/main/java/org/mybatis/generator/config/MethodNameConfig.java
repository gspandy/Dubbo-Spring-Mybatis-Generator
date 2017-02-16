package org.mybatis.generator.config;

/**
 * Created by chunxiaoli on 2/16/17.
 */
public class MethodNameConfig {
    private String insertName;
    private String updateName;
    private String deleteName;
    private String selectName;

    public String getInsertName() {
        return insertName;
    }

    public void setInsertName(String insertName) {
        this.insertName = insertName;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public String getDeleteName() {
        return deleteName;
    }

    public void setDeleteName(String deleteName) {
        this.deleteName = deleteName;
    }

    public String getSelectName() {
        return selectName;
    }

    public void setSelectName(String selectName) {
        this.selectName = selectName;
    }
}
