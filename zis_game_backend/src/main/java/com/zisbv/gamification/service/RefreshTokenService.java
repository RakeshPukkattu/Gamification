package com.zisbv.gamification.service;

import java.util.Optional;

import com.zisbv.gamification.entities.RefreshToken;

public interface RefreshTokenService {

  public Optional<RefreshToken> findByToken(String token);

  public RefreshToken createRefreshToken();

  public void verifyExpiration(RefreshToken token);

  public void increaseCount(RefreshToken refreshToken);

  public RefreshToken save(RefreshToken refreshToken);
  
  public void deleteById(Long id);
}
