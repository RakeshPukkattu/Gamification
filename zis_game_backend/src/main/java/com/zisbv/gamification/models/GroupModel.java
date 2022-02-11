package com.zisbv.gamification.models;

import java.util.ArrayList;
import java.util.List;

public class GroupModel {

	private Long groupId;

	private String groupName;

	private String countryName;

	private Boolean groupStatus;

	private Boolean groupInAnySession;

	private List<GroupMember> members = new ArrayList<GroupMember>();

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public Boolean getGroupStatus() {
		return groupStatus;
	}

	public void setGroupStatus(Boolean groupStatus) {
		this.groupStatus = groupStatus;
	}

	public Boolean getGroupInAnySession() {
		return groupInAnySession;
	}

	public void setGroupInAnySession(Boolean groupInAnySession) {
		this.groupInAnySession = groupInAnySession;
	}

	public List<GroupMember> getMembers() {
		return members;
	}

	public void setMembers(List<GroupMember> members) {
		this.members = members;
	}

}
