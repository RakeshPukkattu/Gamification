package com.zisbv.gamification.models;

import java.util.Arrays;

public class UserCredsModel {

	private Long id;
	private String emailId;
	private String password;
	private String imageKey;
	private String[] roles;
	private Long countryID;

	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}

	public String getImageKey() {
		return imageKey;
	}

	public void setImageKey(String imageKey) {
		this.imageKey = imageKey;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCountryID() {
		return countryID;
	}

	public void setCountryID(Long countryID) {
		this.countryID = countryID;
	}

	@Override
	public String toString() {
		return "UserCredsModel [id=" + id + ", emailId=" + emailId + ", password=" + password + ", imageKey=" + imageKey
				+ ", roles=" + Arrays.toString(roles) + ", countryID=" + countryID + "]";
	}

}
