package com.zisbv.gamification.service.impl;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zisbv.gamification.entities.RefreshToken;
import com.zisbv.gamification.exceptions.TokenRefreshException;
import com.zisbv.gamification.repositories.RefreshTokenRepository;
import com.zisbv.gamification.service.RefreshTokenService;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

  @Autowired
  RefreshTokenRepository refreshTokenRepository;

  @Override
  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  @Override
  public RefreshToken save(RefreshToken refreshToken) {
    return refreshTokenRepository.save(refreshToken);
  }

  @Override
  public RefreshToken createRefreshToken() {
    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setExpiryDate(Instant.now().plusMillis(3600000));
    refreshToken.setToken(UUID.randomUUID().toString());
    refreshToken.setRefreshCount(0L);
    return refreshToken;
  }

  @Override
  public void verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      throw new TokenRefreshException(token.getToken(),
          "Expired token. Please issue a new request");
    }

  }

  @Override
  public void deleteById(Long id) {
    refreshTokenRepository.deleteById(id);
  }


  @Override
  public void increaseCount(RefreshToken refreshToken) {
    refreshToken.incrementRefreshCount();
    save(refreshToken);

  }

}
