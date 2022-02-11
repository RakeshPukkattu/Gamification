package com.zisbv.gamification.models;

import java.util.ArrayList;
import java.util.List;

public class AssignedSurveyGroupsModel {

	private Long groupID;

	private String groupName;

	private boolean groupStatus;

	private List<AssignedSurveyUsersModel> assignedUsers = new ArrayList<AssignedSurveyUsersModel>();

	public Long getGroupID() {
		return groupID;
	}

	public void setGroupID(Long groupID) {
		this.groupID = groupID;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public boolean isGroupStatus() {
		return groupStatus;
	}

	public void setGroupStatus(boolean groupStatus) {
		this.groupStatus = groupStatus;
	}

	public List<AssignedSurveyUsersModel> getAssignedUsers() {
		return assignedUsers;
	}

	public void setAssignedUsers(List<AssignedSurveyUsersModel> assignedUsers) {
		this.assignedUsers = assignedUsers;
	}

	@Override
	public String toString() {
		return "AssignedSurveyGroupsModel [groupID=" + groupID + ", groupName=" + groupName + ", groupStatus="
				+ groupStatus + ", assignedUsers=" + assignedUsers + "]";
	}

}
