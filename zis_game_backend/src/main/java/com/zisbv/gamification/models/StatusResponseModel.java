package com.zisbv.gamification.models;

public class StatusResponseModel {

  private String emailId;

  private Boolean status;


  public String getEmailId() {
    return emailId;
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }
  
  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "StatusResponseModel [emailId=" + emailId + ", status=" + status + "]";
  }

  
}
