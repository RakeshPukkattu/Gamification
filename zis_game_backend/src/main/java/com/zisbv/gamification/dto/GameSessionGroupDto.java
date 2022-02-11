package com.zisbv.gamification.dto;

import java.util.ArrayList;
import java.util.List;

public class GameSessionGroupDto {

	private String sessionName;

	private String startDateandTime;

	private String endDateandTime;

	private List<String> groupIds = new ArrayList<String>();

	private Boolean sessionStatus;

	public Boolean getSessionStatus() {
		return sessionStatus;
	}

	public void setSessionStatus(Boolean sessionStatus) {
		this.sessionStatus = sessionStatus;
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public String getStartDateandTime() {
		return startDateandTime;
	}

	public void setStartDateandTime(String startDateandTime) {
		this.startDateandTime = startDateandTime;
	}

	public String getEndDateandTime() {
		return endDateandTime;
	}

	public void setEndDateandTime(String endDateandTime) {
		this.endDateandTime = endDateandTime;
	}

	public List<String> getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(List<String> groupIds) {
		this.groupIds = groupIds;
	}

}
