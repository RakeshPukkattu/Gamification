package com.zisbv.gamification.models;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginForm {
  @NotBlank
  @Size(min = 3, max = 60)
  private String email;

  @NotBlank
  @Size(min = 6, max = 40)
  private String password;

  @Valid
  @NotNull(message = "Device info cannot be null")
  private DeviceInfo deviceInfo;
  
  private String userAvathar;

  public String getUserAvathar() {
    return userAvathar;
  }

  public void setUserAvathar(String userAvathar) {
    this.userAvathar = userAvathar;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public DeviceInfo getDeviceInfo() {
    return deviceInfo;
  }

  public void setDeviceInfo(DeviceInfo deviceInfo) {
    this.deviceInfo = deviceInfo;
  }
  
  
}
