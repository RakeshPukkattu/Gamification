package com.zisbv.gamification.dto;

import java.util.ArrayList;
import java.util.List;

public class AssignSurveyDto {

	private List<String> userIDs = new ArrayList<String>();

	private List<String> groupIDs = new ArrayList<String>();

	public List<String> getUserIDs() {
		return userIDs;
	}

	public void setUserIDs(List<String> userIDs) {
		this.userIDs = userIDs;
	}

	public List<String> getGroupIDs() {
		return groupIDs;
	}

	public void setGroupIDs(List<String> groupIDs) {
		this.groupIDs = groupIDs;
	}

	@Override
	public String toString() {
		return "AssignSurveyDto [userIDs=" + userIDs + ", groupIDs=" + groupIDs + "]";
	}

}
