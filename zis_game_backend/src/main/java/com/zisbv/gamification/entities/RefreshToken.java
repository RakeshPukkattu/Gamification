package com.zisbv.gamification.entities;

import java.time.Instant;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RefreshToken {

  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refresh_token_seq")
  @SequenceGenerator(name = "refresh_token_seq", allocationSize = 1)
  private Long id;

  @Column(name = "TOKEN", nullable = false, unique = true)
  private String token;

  @OneToOne(optional = false, cascade = CascadeType.ALL)
  @JoinColumn(name = "USER_DEVICE_ID", unique = true)
  private UserDevice userDevice;

  @Column(name = "REFRESH_COUNT")
  private Long refreshCount;

  @Column(name = "EXPIRY_DT", nullable = false)
  private Instant expiryDate;
  
  

  public Long getId() {
    return id;
  }



  public void setId(Long id) {
    this.id = id;
  }



  public String getToken() {
    return token;
  }



  public void setToken(String token) {
    this.token = token;
  }



  public UserDevice getUserDevice() {
    return userDevice;
  }



  public void setUserDevice(UserDevice userDevice) {
    this.userDevice = userDevice;
  }



  public Long getRefreshCount() {
    return refreshCount;
  }



  public void setRefreshCount(Long refreshCount) {
    this.refreshCount = refreshCount;
  }



  public Instant getExpiryDate() {
    return expiryDate;
  }



  public void setExpiryDate(Instant expiryDate) {
    this.expiryDate = expiryDate;
  }



  public void incrementRefreshCount() {
    refreshCount = refreshCount + 1;
  }
}
