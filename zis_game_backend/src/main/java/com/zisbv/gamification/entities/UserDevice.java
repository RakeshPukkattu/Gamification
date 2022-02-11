package com.zisbv.gamification.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserDevice {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_device_seq")
    @SequenceGenerator(name = "user_device_seq", allocationSize = 1)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserData user;

    @Column(name = "DEVICE_TYPE")
    private String deviceType;

    @Column(name = "DEVICE_ID", nullable = false)
    private String deviceId;

    @OneToOne(optional = false, mappedBy = "userDevice")
    private RefreshToken refreshToken;

    @Column(name = "IS_REFRESH_ACTIVE")
    private Boolean isRefreshActive;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public UserData getUser() {
      return user;
    }

    public void setUser(UserData user) {
      this.user = user;
    }

    public String getDeviceType() {
      return deviceType;
    }

    public void setDeviceType(String deviceType) {
      this.deviceType = deviceType;
    }

    public String getDeviceId() {
      return deviceId;
    }

    public void setDeviceId(String deviceId) {
      this.deviceId = deviceId;
    }

    public RefreshToken getRefreshToken() {
      return refreshToken;
    }

    public void setRefreshToken(RefreshToken refreshToken) {
      this.refreshToken = refreshToken;
    }

    public Boolean getIsRefreshActive() {
      return isRefreshActive;
    }

    public void setIsRefreshActive(Boolean isRefreshActive) {
      this.isRefreshActive = isRefreshActive;
    }
    
    
}
