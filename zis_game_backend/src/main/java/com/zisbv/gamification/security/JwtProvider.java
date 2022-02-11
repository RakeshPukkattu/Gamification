package com.zisbv.gamification.security;


import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.zisbv.gamification.cache.LoggedOutJwtTokenCache;
import com.zisbv.gamification.entities.UserData;
import com.zisbv.gamification.event.OnUserLogoutSuccessEvent;
import com.zisbv.gamification.exceptions.InvalidTokenRequestException;
import com.zisbv.gamification.service.impl.UserPrincipal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtProvider {

  private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

  @Autowired
  private LoggedOutJwtTokenCache loggedOutJwtTokenCache;

  public String generateJwtToken(Authentication authentication) {

    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + 3600000);

    return Jwts.builder().setSubject((userPrincipal.getUsername())).setIssuer("gamification")
        .setId(Long.toString(userPrincipal.getId())).setIssuedAt(new Date())
        .setExpiration(expiryDate).signWith(SignatureAlgorithm.HS512, "zisbv").compact();
  }

  public String generateTokenFromUser(UserData user) {
    Instant expiryDate = Instant.now().plusMillis(3600000);
    return Jwts.builder().setSubject(user.getEmail()).setIssuer("gamification")
        .setId(Long.toString(user.getId())).setIssuedAt(Date.from(Instant.now()))
        .setExpiration(Date.from(expiryDate)).signWith(SignatureAlgorithm.HS512, "zisbv")
        .compact();
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser().setSigningKey("zisbv").parseClaimsJws(token).getBody().getSubject();
  }

  public Date getTokenExpiryFromJWT(String token) {
    Claims claims = Jwts.parser().setSigningKey("zisbv").parseClaimsJws(token).getBody();

    return claims.getExpiration();
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parser().setSigningKey("zisbv").parseClaimsJws(authToken);
      validateTokenIsNotForALoggedOutDevice(authToken);
      return true;
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token -> Message: {}", e);
    } catch (ExpiredJwtException e) {
      logger.error("Expired JWT token -> Message: {}", e);
    } catch (UnsupportedJwtException e) {
      logger.error("Unsupported JWT token -> Message: {}", e);
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty -> Message: {}", e);
    }

    return false;
  }

  private void validateTokenIsNotForALoggedOutDevice(String authToken) {
    OnUserLogoutSuccessEvent previouslyLoggedOutEvent =
        loggedOutJwtTokenCache.getLogoutEventForToken(authToken);
    if (previouslyLoggedOutEvent != null) {
      String userEmail = previouslyLoggedOutEvent.getUserEmail();
      Date logoutEventDate = previouslyLoggedOutEvent.getEventTime();
      String errorMessage = String.format(
          "Token corresponds to an already logged out user [%s] at [%s]. Please login again",
          userEmail, logoutEventDate);
      throw new InvalidTokenRequestException("JWT", authToken, errorMessage);
    }
  }

  public String getExpiryDuration() {
    long minutes = TimeUnit.MILLISECONDS.toMinutes(3600000);
    String duration = minutes + "minutes";
    return duration;
  }
}
