package com.zisbv.gamification.dto;

public class UserExcelDto {

  private String userEmail;

  private String userFirstName;

  private String userMiddleName;

  private String userLastName;

  private String userRole1;

  private String userRole2;

  private String userRole3;

  private String country;

  private String errorLog;
  
  private Boolean errorFound;



  public UserExcelDto() {}


  public UserExcelDto(String userEmail, String userFirstName, String userMiddleName,
      String userLastName, String userRole1, String userRole2, String userRole3, String country,
      String errorLog, Boolean errorFound) {
    super();
    this.userEmail = userEmail;
    this.userFirstName = userFirstName;
    this.userMiddleName = userMiddleName;
    this.userLastName = userLastName;
    this.userRole1 = userRole1;
    this.userRole2 = userRole2;
    this.userRole3 = userRole3;
    this.country = country;
    this.errorLog = errorLog;
    this.errorFound = errorFound;
  }


  
  public Boolean getErrorFound() {
	return errorFound;
}


public void setErrorFound(Boolean errorFound) {
	this.errorFound = errorFound;
}


public String getUserEmail() {
    return userEmail;
  }

  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }

  public String getUserFirstName() {
    return userFirstName;
  }

  public void setUserFirstName(String userFirstName) {
    this.userFirstName = userFirstName;
  }

  public String getUserMiddleName() {
    return userMiddleName;
  }

  public void setUserMiddleName(String userMiddleName) {
    this.userMiddleName = userMiddleName;
  }

  public String getUserLastName() {
    return userLastName;
  }

  public void setUserLastName(String userLastName) {
    this.userLastName = userLastName;
  }

  public String getUserRole1() {
    return userRole1;
  }

  public void setUserRole1(String userRole1) {
    this.userRole1 = userRole1;
  }

  public String getUserRole2() {
    return userRole2;
  }

  public void setUserRole2(String userRole2) {
    this.userRole2 = userRole2;
  }

  public String getUserRole3() {
    return userRole3;
  }

  public void setUserRole3(String userRole3) {
    this.userRole3 = userRole3;
  }


  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getErrorLog() {
    return errorLog;
  }

  public void setErrorLog(String errorLog) {
    this.errorLog = errorLog;
  }

  @Override
  public String toString() {
    return "UserExcelDto [userEmail=" + userEmail + ", userFirstName=" + userFirstName
        + ", userMiddleName=" + userMiddleName + ", userLastName=" + userLastName + ", userRole1="
        + userRole1 + ", userRole2=" + userRole2 + ", userRole3=" + userRole3 + ", country="
        + country + ", errorLog=" + errorLog + "]";
  }



}
