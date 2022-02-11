package com.zisbv.gamification.service;

import java.util.Optional;

import com.zisbv.gamification.entities.RefreshToken;
import com.zisbv.gamification.entities.UserDevice;
import com.zisbv.gamification.models.DeviceInfo;

public interface UserDeviceService {

  public Optional<UserDevice> findByUserId(Long userId);
  
  public Optional<UserDevice> findByRefreshToken(RefreshToken refreshToken);

  public UserDevice createUserDevice(DeviceInfo deviceInfo);

  public void verifyRefreshAvailability(RefreshToken refreshToken);
}
