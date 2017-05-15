package com.chunxiaoli.gaia.Gaia1.api.dto;

import java.lang.Integer;
import java.lang.String;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskPackageDto {
  private static final Logger logger = LoggerFactory.getLogger(TaskPackageDto.class);

  private Integer id;

  private String firstPartyCompanys;

  private String firstPartyName;

  private String outsourceCompanyName;

  private String taskType;

  private String entrustBatchNo;

  private String presentAddress;

  private String residenceAddress;

  private String phaseRange;

  private String taskTimeRange;

  private String loanMoneyRange;

  private String pubDate;

  private Integer taskNum;

  private String entrustTimeRange;

  private String rateDependenceTime;

  private String otherLimit;

  private Integer state;

  private Date createTime;

  private Date updateTime;

  private Integer source;

  private String uploadBatchNo;

  public void setId(Integer id) {
    this.id=id;
  }

  public void getId() {
    return this.id;
  }

  public void setFirstPartyCompanys(String firstPartyCompanys) {
    this.firstPartyCompanys=firstPartyCompanys;
  }

  public void getFirstPartyCompanys() {
    return this.firstPartyCompanys;
  }

  public void setFirstPartyName(String firstPartyName) {
    this.firstPartyName=firstPartyName;
  }

  public void getFirstPartyName() {
    return this.firstPartyName;
  }

  public void setOutsourceCompanyName(String outsourceCompanyName) {
    this.outsourceCompanyName=outsourceCompanyName;
  }

  public void getOutsourceCompanyName() {
    return this.outsourceCompanyName;
  }

  public void setTaskType(String taskType) {
    this.taskType=taskType;
  }

  public void getTaskType() {
    return this.taskType;
  }

  public void setEntrustBatchNo(String entrustBatchNo) {
    this.entrustBatchNo=entrustBatchNo;
  }

  public void getEntrustBatchNo() {
    return this.entrustBatchNo;
  }

  public void setPresentAddress(String presentAddress) {
    this.presentAddress=presentAddress;
  }

  public void getPresentAddress() {
    return this.presentAddress;
  }

  public void setResidenceAddress(String residenceAddress) {
    this.residenceAddress=residenceAddress;
  }

  public void getResidenceAddress() {
    return this.residenceAddress;
  }

  public void setPhaseRange(String phaseRange) {
    this.phaseRange=phaseRange;
  }

  public void getPhaseRange() {
    return this.phaseRange;
  }

  public void setTaskTimeRange(String taskTimeRange) {
    this.taskTimeRange=taskTimeRange;
  }

  public void getTaskTimeRange() {
    return this.taskTimeRange;
  }

  public void setLoanMoneyRange(String loanMoneyRange) {
    this.loanMoneyRange=loanMoneyRange;
  }

  public void getLoanMoneyRange() {
    return this.loanMoneyRange;
  }

  public void setPubDate(String pubDate) {
    this.pubDate=pubDate;
  }

  public void getPubDate() {
    return this.pubDate;
  }

  public void setTaskNum(Integer taskNum) {
    this.taskNum=taskNum;
  }

  public void getTaskNum() {
    return this.taskNum;
  }

  public void setEntrustTimeRange(String entrustTimeRange) {
    this.entrustTimeRange=entrustTimeRange;
  }

  public void getEntrustTimeRange() {
    return this.entrustTimeRange;
  }

  public void setRateDependenceTime(String rateDependenceTime) {
    this.rateDependenceTime=rateDependenceTime;
  }

  public void getRateDependenceTime() {
    return this.rateDependenceTime;
  }

  public void setOtherLimit(String otherLimit) {
    this.otherLimit=otherLimit;
  }

  public void getOtherLimit() {
    return this.otherLimit;
  }

  public void setState(Integer state) {
    this.state=state;
  }

  public void getState() {
    return this.state;
  }

  public void setCreateTime(Date createTime) {
    this.createTime=createTime;
  }

  public void getCreateTime() {
    return this.createTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime=updateTime;
  }

  public void getUpdateTime() {
    return this.updateTime;
  }

  public void setSource(Integer source) {
    this.source=source;
  }

  public void getSource() {
    return this.source;
  }

  public void setUploadBatchNo(String uploadBatchNo) {
    this.uploadBatchNo=uploadBatchNo;
  }

  public void getUploadBatchNo() {
    return this.uploadBatchNo;
  }
}
