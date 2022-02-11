package com.zisbv.gamification.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.zisbv.gamification.entities.UserData;

public class UserDto {

	private String name;
	private String email;
	private List<String> roles = new ArrayList<String>();
	private Long countryID;
	private Long regionID;
	private Long stateID;
	private Long cityID;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
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

	public UserData dataFromPojo() {

		String rolesString = roles.stream().map(Object::toString).collect(Collectors.joining(","));

		UserData user = new UserData();

		user.setUserName(name);
		user.setEmail(email);
		user.setRoles(rolesString);
		user.setCountryID(countryID);
		user.setRegionID(regionID);
		user.setStateID(stateID);
		user.setCityID(cityID);
		user.setUserStatus(false);
		user.setUserInAnyGroup(false);
		user.setUserInAnySession(false);

		return user;
	}

}
