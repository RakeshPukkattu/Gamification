package com.zisbv.gamification.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class DeviceInfo {

  @NotBlank(message = "Device id cannot be blank")
  private String deviceId;

  @NotNull(message = "Device type cannot be null")
  private String deviceType;

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public String getDeviceType() {
    return deviceType;
  }

  public void setDeviceType(String deviceType) {
    this.deviceType = deviceType;
  }


}
