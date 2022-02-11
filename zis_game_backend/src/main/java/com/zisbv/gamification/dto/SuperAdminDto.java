package com.zisbv.gamification.dto;

import com.zisbv.gamification.entities.UserData;

public class SuperAdminDto {

	private String name;
	private String email;
	private String password;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserData dataFromPojo() {

		UserData user = new UserData();

		user.setUserName(name);
		user.setEmail(email);
		user.setPassword(password);
		user.setRoles("SuperAdmin");
		user.setCountryID((long) 0);
		user.setRegionID((long) 0);
		user.setCityID((long) 0);
		user.setUserStatus(true);
		user.setUserInAnyGroup(false);
		user.setUserInAnySession(false);

		return user;
	}

}
