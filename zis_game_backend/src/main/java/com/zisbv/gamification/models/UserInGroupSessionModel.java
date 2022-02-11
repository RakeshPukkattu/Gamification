package com.zisbv.gamification.models;

public class UserInGroupSessionModel {

	private Long id;

	private String userName;

	private String userEmail;

	private String avatharKey;

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

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getAvatharKey() {
		return avatharKey;
	}

	public void setAvatharKey(String avatharKey) {
		this.avatharKey = avatharKey;
	}

	@Override
	public String toString() {
		return "UserInGroupSessionModel [id=" + id + ", userName=" + userName + ", userEmail=" + userEmail
				+ ", avatharKey=" + avatharKey + "]";
	}

}
