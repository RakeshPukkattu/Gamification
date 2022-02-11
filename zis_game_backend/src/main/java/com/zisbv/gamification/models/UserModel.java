package com.zisbv.gamification.models;

public class UserModel {

	private Integer id;
	private String name;
	private String imageKey;
	private String email;
	private String[] roles;
	private String country;
	private String region;
	private String state;
	private String city;
	private String cityLatitude;
	private String cityLongitude;
	private Boolean status;
	private Boolean user_In_Group;
	private Boolean user_In_Any_Session;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageKey() {
		return imageKey;
	}

	public void setImageKey(String imageKey) {
		this.imageKey = imageKey;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}
	
	

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCityLatitude() {
		return cityLatitude;
	}

	public void setCityLatitude(String cityLatitude) {
		this.cityLatitude = cityLatitude;
	}

	public String getCityLongitude() {
		return cityLongitude;
	}

	public void setCityLongitude(String cityLongitude) {
		this.cityLongitude = cityLongitude;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Boolean getUser_In_Group() {
		return user_In_Group;
	}

	public void setUser_In_Group(Boolean user_In_Group) {
		this.user_In_Group = user_In_Group;
	}

	public Boolean getUser_In_Any_Session() {
		return user_In_Any_Session;
	}

	public void setUser_In_Any_Session(Boolean user_In_Any_Session) {
		this.user_In_Any_Session = user_In_Any_Session;
	}

}
