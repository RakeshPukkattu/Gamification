package com.zisbv.gamification.dto;

public class AdminBulkUserUploadDto {

	private String userEmail;

	private String userFirstName;

	private String userMiddleName;

	private String userLastName;

	private String userRole;

	private String country;

	private String region;

	private String state;

	private String city;

	private String errorLog;

	private Boolean errorFound;

	public AdminBulkUserUploadDto() {
	}

	public AdminBulkUserUploadDto(String userEmail, String userFirstName, String userMiddleName, String userLastName,
			String userRole, String country, String region, String state, String city, String errorLog,
			Boolean errorFound) {
		super();
		this.userEmail = userEmail;
		this.userFirstName = userFirstName;
		this.userMiddleName = userMiddleName;
		this.userLastName = userLastName;
		this.userRole = userRole;
		this.country = country;
		this.region = region;
		this.state = state;
		this.city = city;
		this.errorLog = errorLog;
		this.errorFound = errorFound;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserMiddleName() {
		return userMiddleName;
	}

	public void setUserMiddleName(String userMiddleName) {
		this.userMiddleName = userMiddleName;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
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

	public String getErrorLog() {
		return errorLog;
	}

	public void setErrorLog(String errorLog) {
		this.errorLog = errorLog;
	}

	public Boolean getErrorFound() {
		return errorFound;
	}

	public void setErrorFound(Boolean errorFound) {
		this.errorFound = errorFound;
	}

	@Override
	public String toString() {
		return "AdminBulkUserUploadDto [userEmail=" + userEmail + ", userFirstName=" + userFirstName
				+ ", userMiddleName=" + userMiddleName + ", userLastName=" + userLastName + ", userRole=" + userRole
				+ ", country=" + country + ", region=" + region + ", state=" + state + ", city=" + city + ", errorLog="
				+ errorLog + ", errorFound=" + errorFound + "]";
	}

}
