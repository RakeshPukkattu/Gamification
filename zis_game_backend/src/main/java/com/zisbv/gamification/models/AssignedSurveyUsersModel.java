package com.zisbv.gamification.models;

public class AssignedSurveyUsersModel {

	private Long userID;

	private String userName;

	private String userImageKey;

	private String userEmail;

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserImageKey() {
		return userImageKey;
	}

	public void setUserImageKey(String userImageKey) {
		this.userImageKey = userImageKey;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	@Override
	public String toString() {
		return "AssignedSurveyUsersModel [userID=" + userID + ", userName=" + userName + ", userImageKey="
				+ userImageKey + ", userEmail=" + userEmail + "]";
	}

}
