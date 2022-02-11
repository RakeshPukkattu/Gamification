package com.zisbv.gamification.models;

import java.util.List;

public class GameSessionUserModel {

	private Long sessionId;

	private String sessionName;

	private Long countryID;

	private String startTime;

	private String endTime;

	private Boolean sessionEnableDisable;

	private String sessionCurrentStatus;

	private List<UserInGroupSessionModel> getUsersInSession;

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public Long getCountryID() {
		return countryID;
	}

	public void setCountryID(Long countryID) {
		this.countryID = countryID;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Boolean getSessionEnableDisable() {
		return sessionEnableDisable;
	}

	public void setSessionEnableDisable(Boolean sessionEnableDisable) {
		this.sessionEnableDisable = sessionEnableDisable;
	}

	public String getSessionCurrentStatus() {
		return sessionCurrentStatus;
	}

	public void setSessionCurrentStatus(String sessionCurrentStatus) {
		this.sessionCurrentStatus = sessionCurrentStatus;
	}

	public List<UserInGroupSessionModel> getGetUsersInSession() {
		return getUsersInSession;
	}

	public void setGetUsersInSession(List<UserInGroupSessionModel> getUsersInSession) {
		this.getUsersInSession = getUsersInSession;
	}

	@Override
	public String toString() {
		return "GameSessionUserModel [sessionId=" + sessionId + ", sessionName=" + sessionName + ", countryID="
				+ countryID + ", startTime=" + startTime + ", endTime=" + endTime + ", sessionEnableDisable="
				+ sessionEnableDisable + ", sessionCurrentStatus=" + sessionCurrentStatus + ", getUsersInSession="
				+ getUsersInSession + "]";
	}

}
