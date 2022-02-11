package com.zisbv.gamification.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class UserData {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String userName;

	private String email;

	private String password;

	private String imageKey;

	private String roles;

	private Long countryID;

	private Long regionID;

	private Long stateID;

	private Long cityID;

	@Column(columnDefinition = "tinyint(1) default 1")
	private Boolean userStatus;

	@Column(columnDefinition = "tinyint(1) default 0")
	private Boolean userInAnyGroup;

	@Column(columnDefinition = "tinyint(1) default 0")
	private Boolean userInAnySession;

	private String resetPasswordToken;

	private String verificationCode;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "members")
	private List<GroupData> groups = new ArrayList<>();

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "users")
	private List<GameSessionUserData> userSessions = new ArrayList<>();

	public UserData() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getImageKey() {
		return imageKey;
	}

	public void setImageKey(String imageKey) {
		this.imageKey = imageKey;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public Long getCountryID() {
		return countryID;
	}

	public void setCountryID(Long countryID) {
		this.countryID = countryID;
	}

	public Long getRegionID() {
		return regionID;
	}

	public void setRegionID(Long regionID) {
		this.regionID = regionID;
	}

	public Long getStateID() {
		return stateID;
	}

	public void setStateID(Long stateID) {
		this.stateID = stateID;
	}

	public Long getCityID() {
		return cityID;
	}

	public void setCityID(Long cityID) {
		this.cityID = cityID;
	}

	public Boolean getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(Boolean userStatus) {
		this.userStatus = userStatus;
	}

	public Boolean getUserInAnyGroup() {
		return userInAnyGroup;
	}

	public void setUserInAnyGroup(Boolean userInAnyGroup) {
		this.userInAnyGroup = userInAnyGroup;
	}

	public Boolean getUserInAnySession() {
		return userInAnySession;
	}

	public void setUserInAnySession(Boolean userInAnySession) {
		this.userInAnySession = userInAnySession;
	}

	public String getResetPasswordToken() {
		return resetPasswordToken;
	}

	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public List<GroupData> getGroups() {
		return groups;
	}

	public void setGroups(List<GroupData> groups) {
		this.groups = groups;
	}

	public List<GameSessionUserData> getUserSessions() {
		return userSessions;
	}

	public void setUserSessions(List<GameSessionUserData> userSessions) {
		this.userSessions = userSessions;
	}

}
