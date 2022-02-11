package com.zisbv.gamification.dto;

import java.util.ArrayList;
import java.util.List;

public class GroupDto {

	private String groupName;

	private List<String> groupMemberIds = new ArrayList<String>();

	public List<String> getGroupMemberIds() {
		return groupMemberIds;
	}

	public void setGroupMemberIds(List<String> groupMemberIds) {
		this.groupMemberIds = groupMemberIds;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

}
