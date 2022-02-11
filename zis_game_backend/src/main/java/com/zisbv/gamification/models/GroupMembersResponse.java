package com.zisbv.gamification.models;

import java.util.ArrayList;
import java.util.List;

public class GroupMembersResponse {

	private List<GroupMember> groupMembers = new ArrayList<GroupMember>();

	public List<GroupMember> getGroupMembers() {
		return groupMembers;
	}

	public void setGroupMembers(List<GroupMember> groupMembers) {
		this.groupMembers = groupMembers;
	}

}
