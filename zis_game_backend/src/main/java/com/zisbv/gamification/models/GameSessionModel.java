package com.zisbv.gamification.models;

import java.util.List;

public class GameSessionModel {

	private Long sessionId;

	private String sessionName;

	private String countryName;

	private String startTime;

	private String endTime;

	private Boolean sessionEnableDisable;

	private String sessionCurrentStatus;

	private List<GroupInSessionModel> groupsInSession;

	public List<GroupInSessionModel> getGetGroupsInSession() {
		return groupsInSession;
	}

	public void setGetGroupsInSession(List<GroupInSessionModel> getGroupsInSession) {
		this.groupsInSession = getGroupsInSession;
	}

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

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
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

}
