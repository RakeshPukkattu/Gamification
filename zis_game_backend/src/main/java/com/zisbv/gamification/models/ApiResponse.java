package com.zisbv.gamification.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponse {
  private boolean success;
  private String message;



  public boolean isSuccess() {
    return success;
  }



  public void setSuccess(boolean success) {
    this.success = success;
  }



  public String getMessage() {
    return message;
  }



  public void setMessage(String message) {
    this.message = message;
  }



  public ApiResponse(boolean success, String message) {
    this.setSuccess(success);
    this.setMessage(message);
  }

}
