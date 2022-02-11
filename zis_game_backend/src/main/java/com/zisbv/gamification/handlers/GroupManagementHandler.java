package com.zisbv.gamification.handlers;

import com.zisbv.gamification.entities.GroupData;

public class GroupManagementHandler {

	public GroupData updateStatus(GroupData existingGroup, Boolean status) {

		GroupData group = new GroupData();
		group.setGroupID(existingGroup.getGroupID());
		group.setGroupName(existingGroup.getGroupName());
		group.setCountryID(existingGroup.getCountryID());
		group.setGroupStatus(status);
		group.setGroupInAnySession(existingGroup.getGroupInAnySession());
		group.setMembers(existingGroup.getMembers());

		return group;
	}

}
