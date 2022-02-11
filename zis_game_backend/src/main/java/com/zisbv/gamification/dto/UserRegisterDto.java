package com.zisbv.gamification.dto;

import com.zisbv.gamification.entities.UserData;

public class UserRegisterDto {

	private String email;
	private String password;
	private String name;
	private Long countryID;
	private Long regionID;
	private Long stateID;
	private Long cityID;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public UserData dataFromPojo() {

		UserData user = new UserData();

		user.setEmail(email);
		user.setPassword(password);
		user.setUserName(name);
		user.setCountryID(countryID);
		user.setRegionID(regionID);
		user.setStateID(stateID);
		user.setRegionID(regionID);
		user.setCityID(cityID);
		user.setRoles("Learner");
		user.setUserStatus(true);
		user.setUserInAnyGroup(false);

		return user;
	}

	@Override
	public String toString() {
		return "UserRegisterDto [email=" + email + ", password=" + password + ", name=" + name + ", countryID="
				+ countryID + "]";
	}

}
