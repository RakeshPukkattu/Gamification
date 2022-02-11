package com.zisbv.gamification.models;

public class LoginUser {

  private String email;
  private String password;

  public String getUsername() {
      return email;
  }

  public void setUsername(String username) {
      this.email = username;
  }

  public String getPassword() {
      return password;
  }

  public void setPassword(String password) {
      this.password = password;
  }

  @Override
  public String toString() {
    return "LoginUser [email=" + email + ", password=" + password + "]";
  }
  
  
}
