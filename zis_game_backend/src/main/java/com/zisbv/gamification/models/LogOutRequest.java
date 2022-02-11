package com.zisbv.gamification.models;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogOutRequest {

    @Valid
    @NotNull(message = "Device info cannot be null")
    private DeviceInfo deviceInfo;
    
    @Valid
    @NotNull(message = "Existing Token needs to be passed")
    private String token;

    public DeviceInfo getDeviceInfo() {
      return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
      this.deviceInfo = deviceInfo;
    }

    public String getToken() {
      return token;
    }

    public void setToken(String token) {
      this.token = token;
    }
    
    
}

