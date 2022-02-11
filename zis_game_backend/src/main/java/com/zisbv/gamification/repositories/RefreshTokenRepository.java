package com.zisbv.gamification.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zisbv.gamification.entities.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  @Override
  Optional<RefreshToken> findById(Long id);

  Optional<RefreshToken> findByToken(String token);

}
