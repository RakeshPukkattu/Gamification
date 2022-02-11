package com.zisbv.gamification.models;

import java.util.ArrayList;
import java.util.List;

public class GroupInSessionModel {

	private long groupId;

	private String groupName;

	private List<UserInGroupSessionModel> groupMembers = new ArrayList<UserInGroupSessionModel>();

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<UserInGroupSessionModel> getGroupMembers() {
		return groupMembers;
	}

	public void setGroupMembers(List<UserInGroupSessionModel> groupMembers) {
		this.groupMembers = groupMembers;
	}

	@Override
	public String toString() {
		return "GroupInSessionModel [groupId=" + groupId + ", groupName=" + groupName + ", groupMembers=" + groupMembers
				+ "]";
	}

}
