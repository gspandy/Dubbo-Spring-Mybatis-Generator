package com.chunxiaoli.gaia.Gaia1.provider.pojo;

import java.util.Date;

/**
 */
public class TaskPackage {
    private Integer id;

    /**
    * 发布任务的甲方公司,多个id用,隔开
    */
    private String firstPartyCompanys;

    /**
    * 发布任务的公司名字,多个用,隔开
    */
    private String firstPartyName;

    /**
    * 外包公司名称
    */
    private String outsourceCompanyName;

    /**
    * 任务类型
    */
    private String taskType;

    /**
    * 委托批次号
    */
    private String entrustBatchNo;

    /**
    * 现住址
    */
    private String presentAddress;

    /**
    * 户籍住址
    */
    private String residenceAddress;

    /**
    * 逾期阶段范围,多个范围以,隔开
    */
    private String phaseRange;

    /**
    * 甲方委托时间
    */
    private String taskTimeRange;

    /**
    * 欠款金额范围
    */
    private String loanMoneyRange;

    private String pubDate;

    /**
    * 任务数量
    */
    private Integer taskNum;

    /**
    * 委托时间范围
    */
    private String entrustTimeRange;

    /**
    * 佣金比例和天数的关系如（120-250:0.23;100-200:0.10）
    */
    private String rateDependenceTime;

    /**
    * 其他条件
    */
    private String otherLimit;

    /**
    * 1, 有效 2 逻辑删除
    */
    private Integer state;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 更新时间
    */
    private Date updateTime;

    /**
    * 来源：1-达飞 2鑫诚达
    */
    private Integer source;

    /**
    * 任务的上传批次号，只有后门委托可以用
    */
    private String uploadBatchNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstPartyCompanys() {
        return firstPartyCompanys;
    }

    public void setFirstPartyCompanys(String firstPartyCompanys) {
        this.firstPartyCompanys = firstPartyCompanys;
    }

    public String getFirstPartyName() {
        return firstPartyName;
    }

    public void setFirstPartyName(String firstPartyName) {
        this.firstPartyName = firstPartyName;
    }

    public String getOutsourceCompanyName() {
        return outsourceCompanyName;
    }

    public void setOutsourceCompanyName(String outsourceCompanyName) {
        this.outsourceCompanyName = outsourceCompanyName;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getEntrustBatchNo() {
        return entrustBatchNo;
    }

    public void setEntrustBatchNo(String entrustBatchNo) {
        this.entrustBatchNo = entrustBatchNo;
    }

    public String getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
    }

    public String getResidenceAddress() {
        return residenceAddress;
    }

    public void setResidenceAddress(String residenceAddress) {
        this.residenceAddress = residenceAddress;
    }

    public String getPhaseRange() {
        return phaseRange;
    }

    public void setPhaseRange(String phaseRange) {
        this.phaseRange = phaseRange;
    }

    public String getTaskTimeRange() {
        return taskTimeRange;
    }

    public void setTaskTimeRange(String taskTimeRange) {
        this.taskTimeRange = taskTimeRange;
    }

    public String getLoanMoneyRange() {
        return loanMoneyRange;
    }

    public void setLoanMoneyRange(String loanMoneyRange) {
        this.loanMoneyRange = loanMoneyRange;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public Integer getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(Integer taskNum) {
        this.taskNum = taskNum;
    }

    public String getEntrustTimeRange() {
        return entrustTimeRange;
    }

    public void setEntrustTimeRange(String entrustTimeRange) {
        this.entrustTimeRange = entrustTimeRange;
    }

    public String getRateDependenceTime() {
        return rateDependenceTime;
    }

    public void setRateDependenceTime(String rateDependenceTime) {
        this.rateDependenceTime = rateDependenceTime;
    }

    public String getOtherLimit() {
        return otherLimit;
    }

    public void setOtherLimit(String otherLimit) {
        this.otherLimit = otherLimit;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getUploadBatchNo() {
        return uploadBatchNo;
    }

    public void setUploadBatchNo(String uploadBatchNo) {
        this.uploadBatchNo = uploadBatchNo;
    }
}