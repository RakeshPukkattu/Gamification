package com.zisbv.gamification.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zisbv.gamification.entities.RefreshToken;
import com.zisbv.gamification.entities.UserDevice;
import com.zisbv.gamification.exceptions.TokenRefreshException;
import com.zisbv.gamification.models.DeviceInfo;
import com.zisbv.gamification.repositories.UserDeviceRepository;
import com.zisbv.gamification.service.UserDeviceService;

@Service
public class UserDeviceServiceImpl implements UserDeviceService {

  @Autowired
  private UserDeviceRepository userDeviceRepository;

  @Override
  public Optional<UserDevice> findByUserId(Long userId) {
    return userDeviceRepository.findByUserId(userId);
  }

  @Override
  public Optional<UserDevice> findByRefreshToken(RefreshToken refreshToken) {
    return userDeviceRepository.findByRefreshToken(refreshToken);
  }

  @Override
  public UserDevice createUserDevice(DeviceInfo deviceInfo) {
    UserDevice userDevice = new UserDevice();
    userDevice.setDeviceId(deviceInfo.getDeviceId());
    userDevice.setDeviceType(deviceInfo.getDeviceType());
    userDevice.setIsRefreshActive(true);
    return userDevice;
  }

  @Override
  public void verifyRefreshAvailability(RefreshToken refreshToken) {
    UserDevice userDevice = findByRefreshToken(refreshToken)
        .orElseThrow(() -> new TokenRefreshException(refreshToken.getToken(),
            "No device found for the matching token. Please login again"));

    if (!userDevice.getIsRefreshActive()) {
      throw new TokenRefreshException(refreshToken.getToken(),
          "Refresh blocked for the device. Please login through a different device");
    }
  }
}
