package com.zisbv.gamification.models;

import java.util.ArrayList;
import java.util.List;

//@ApiModel(description = "") need to add io.swagger.annotation
public class UserResponseModel {

  private List<UserModel> users = new ArrayList<UserModel>();

  public List<UserModel> getUsers() {
    return users;
  }

  public void setUsers(List<UserModel> users) {
    this.users = users;
  }

  @Override
  public String toString() {
    return "UserResponseModel [users=" + users + "]";
  }



}
