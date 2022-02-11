package com.zisbv.gamification.service;

import java.util.List;

import com.zisbv.gamification.dto.GroupDto;
import com.zisbv.gamification.entities.GroupData;
import com.zisbv.gamification.models.GroupModel;
import com.zisbv.gamification.models.GroupsResponseModel;

public interface GroupManagementService {

	// create group (Admin only).
	void createGroup(GroupDto group, Long countryID);

	// get all groups.
	List<GroupData> findAll();
	
	// get group based on countries
	GroupsResponseModel getGroupResponseModelGroupedbyCountry(String country);

	// get group with id.
	GroupData findWithid(Long id);

	// get Group members.
	GroupModel getGroup(Long Id);

	// get group with name.
	GroupData findWithName(String name);

	// get all groups as GroupResponseModel.
	GroupsResponseModel getGroupResponseModel();

	// Update status.
	GroupData updateGroupStatus(GroupData group);
	
	// Update status.
	GroupData updateGroupInAnySession(GroupData group);

	// Update group (Admin only).
	void updateGroup(GroupDto dataFromUser, GroupData existingGroup, Long countryID);

	// delete list of groups (Admin only).
	String deleteGroups(Long[] groups);

	// delete group with id.
	Boolean deleteGroupWithID(Long id);


}
