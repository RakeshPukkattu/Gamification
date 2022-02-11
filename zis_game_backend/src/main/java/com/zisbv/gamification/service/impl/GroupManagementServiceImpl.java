package com.zisbv.gamification.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zisbv.gamification.dto.GroupDto;
import com.zisbv.gamification.entities.Country;
import com.zisbv.gamification.entities.GroupData;
import com.zisbv.gamification.entities.UserData;
import com.zisbv.gamification.models.GroupMember;
import com.zisbv.gamification.models.GroupModel;
import com.zisbv.gamification.models.GroupsResponseModel;
import com.zisbv.gamification.repositories.CountryRepository;
import com.zisbv.gamification.repositories.GroupRepository;
import com.zisbv.gamification.repositories.UserRepository;
import com.zisbv.gamification.service.GroupManagementService;
import com.zisbv.gamification.service.UserManagementService;

@Service(value = "groupManagementService")
public class GroupManagementServiceImpl implements GroupManagementService {

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	CountryRepository countryRepository;

	@Autowired
	UserManagementService userManagementService;

	@Override
	public void createGroup(GroupDto group, Long countryID) {

		GroupData newGroup = new GroupData();

		newGroup.setGroupName(group.getGroupName());
		newGroup.setGroupStatus(true);
		newGroup.setGroupInAnySession(false);
		newGroup.setCountryID(countryID);

		String groupMembersString = group.getGroupMemberIds().stream().map(Object::toString)
				.collect(Collectors.joining(","));
		String[] groupMembersIDsArray = groupMembersString.split(",");
		for (String memberID : groupMembersIDsArray) {
			UserData memberData = userRepository.findById(Long.valueOf(memberID));
			newGroup.getMembers().add(memberData);

			memberData.setUserInAnyGroup(true);
			userRepository.save(memberData);

		}

		groupRepository.save(newGroup);

	}

	@Override
	public List<GroupData> findAll() {
		List<GroupData> list = new ArrayList<>();
		groupRepository.findAll().iterator().forEachRemaining(list::add);
		return list;
	}

	@Override
	public GroupData findWithid(Long id) {
		GroupData dataFromDB = groupRepository.getGroupWithID(id);
		return dataFromDB;
	}

	@Override
	public GroupModel getGroup(Long id) {

		// final response
		GroupModel groupModel = new GroupModel();

		// data from db
		GroupData dataFromDb = findWithid(id);

		// db data to groupModel
		groupModel.setGroupId(dataFromDb.getGroupID());
		groupModel.setGroupName(dataFromDb.getGroupName());

		Country countryData = countryRepository.getCountryWithID(dataFromDb.getCountryID());
		groupModel.setCountryName(countryData.getCountryName());
		groupModel.setGroupStatus(dataFromDb.getGroupStatus());
		groupModel.setGroupInAnySession(dataFromDb.getGroupInAnySession());

		// group members
		List<GroupMember> groupMembersModel = new ArrayList<GroupMember>();
		List<UserData> membersInDB = dataFromDb.getMembers();

		// iterating members
		for (UserData member : membersInDB) {

			GroupMember groupMember = new GroupMember();

			groupMember.setId(member.getId());
			groupMember.setName(member.getUserName());
			groupMember.setImageKey(member.getImageKey());
			groupMember.setEmail(member.getEmail());
			String roleStringFromDb = member.getRoles();
			String[] rolesArray = roleStringFromDb.split(",", 0);
			groupMember.setRoles(rolesArray);
			groupMember.setCountryID(member.getCountryID());
			groupMember.setStatus(member.getUserStatus());
			groupMembersModel.add(groupMember);
		}

		groupModel.setMembers(groupMembersModel);

		return groupModel;
	}

	@Override
	public GroupData findWithName(String name) {
		GroupData data = groupRepository.getGroupWithName(name);
		return data;
	}

	@Override
	public GroupData updateGroupStatus(GroupData group) {
		return groupRepository.save(group);
	}

	@Override
	public void updateGroup(GroupDto dataFromUser, GroupData existingGroup, Long countryID) {

		// new changes for response
		GroupData modifiedGroup = new GroupData();
		modifiedGroup.setGroupID(existingGroup.getGroupID());

		// if name is not given.
		if (dataFromUser.getGroupName() != null) {
			modifiedGroup.setGroupName(dataFromUser.getGroupName());
		} else {
			modifiedGroup.setGroupName(existingGroup.getGroupName());
		}

		modifiedGroup.setCountryID(countryID);
		modifiedGroup.setGroupStatus(existingGroup.getGroupStatus());
		modifiedGroup.setGroupInAnySession(existingGroup.getGroupInAnySession());

		// if members id is not given
		if (dataFromUser.getGroupMemberIds() != null) {
			List<UserData> groupMembers = new ArrayList<UserData>();
			for (String memberID : dataFromUser.getGroupMemberIds()) {
				UserData newMember = userRepository.findById(Long.parseLong(memberID));
				groupMembers.add(newMember);
				newMember.setUserInAnyGroup(true);
				userRepository.save(newMember);
			}
			modifiedGroup.setMembers(groupMembers);
		} else {
			modifiedGroup.setMembers(existingGroup.getMembers());
		}

		groupRepository.save(modifiedGroup);

	}

	@Override
	public String deleteGroups(Long[] groupIDs) {

		String response = "";
		int delCounter = 0;
		int notFoundCounter = 0;
		ArrayList<Long> notFoundIds = new ArrayList<Long>();

		for (long groupId : groupIDs) {
			GroupData existingGroup = groupRepository.getGroupWithID(groupId);
			if (existingGroup != null) {
				groupRepository.delete(existingGroup);
				delCounter++;
			} else {
				notFoundIds.add(groupId);
			}
		}

		if (!notFoundIds.isEmpty()) {
			notFoundCounter = notFoundIds.size();
			long[] longArray = new long[notFoundCounter];
			for (int i = 0; i < notFoundCounter; i++) {
				longArray[i] = notFoundIds.get(i);
			}
			String ids = "";
			for (long n : longArray) {
				ids = ids + " " + n;
			}
			response = notFoundCounter + " Group with id's -" + ids + " was not found";
		}

		int groupCount = delCounter;
		String successResponse = response + ".  Deleted " + groupCount + "groups.";
		return successResponse;
	}

	@Override
	public Boolean deleteGroupWithID(Long id) {

		GroupData existingGroup = groupRepository.getGroupWithID(id);
		if (existingGroup != null) {
			groupRepository.delete(existingGroup);
			return true;
		}

		return false;

	}

	@Override
	public GroupsResponseModel getGroupResponseModel() {
		List<GroupData> dataFromDb = groupRepository.findAll();

		// Final Response.
		GroupsResponseModel groups = new GroupsResponseModel();
		List<GroupModel> responseModel = new ArrayList<GroupModel>();

		// iterating database data and adding in to response model.
		for (GroupData groupData : dataFromDb) {

			// Response.
			GroupModel groupModel = new GroupModel();
			groupModel.setGroupId(groupData.getGroupID());
			groupModel.setGroupName(groupData.getGroupName());
			groupModel.setGroupStatus(groupData.getGroupStatus());

			Country countryData = countryRepository.getCountryWithID(groupData.getCountryID());
			groupModel.setCountryName(countryData.getCountryName());
			groupModel.setGroupInAnySession(groupData.getGroupInAnySession());

			List<GroupMember> groupMembersModel = new ArrayList<GroupMember>();
			List<UserData> membersInDB = groupData.getMembers();

			// iterating members
			for (UserData member : membersInDB) {

				GroupMember groupMember = new GroupMember();

				groupMember.setId(member.getId());
				groupMember.setName(member.getUserName());
				groupMember.setImageKey(member.getImageKey());
				groupMember.setEmail(member.getEmail());
				String roleStringFromDb = member.getRoles();
				String[] rolesArray = roleStringFromDb.split(",", 0);
				groupMember.setRoles(rolesArray);
				groupMember.setCountryID(member.getCountryID());
				groupMember.setStatus(member.getUserStatus());
				groupMembersModel.add(groupMember);
			}

			groupModel.setMembers(groupMembersModel);
			responseModel.add(groupModel);
		}

		// final response
		groups.setGroups(responseModel);
		return groups;
	}

	@Override
	public GroupsResponseModel getGroupResponseModelGroupedbyCountry(String country) {
		List<GroupData> dataFromDb = groupRepository.findAll();

		// Final Response.
		GroupsResponseModel groups = new GroupsResponseModel();
		List<GroupModel> responseModel = new ArrayList<GroupModel>();

		// iterating database data and adding in to response model.
		for (GroupData groupData : dataFromDb) {

			Long countryIDDB = groupData.getCountryID();
			Country countryDataDB = countryRepository.getCountryWithID(countryIDDB);
			String countryNameDB = countryDataDB.getCountryName();

			if (countryNameDB.equalsIgnoreCase(country)) {

				// Response.
				GroupModel groupModel = new GroupModel();
				groupModel.setGroupId(groupData.getGroupID());
				groupModel.setGroupName(groupData.getGroupName());
				groupModel.setGroupStatus(groupData.getGroupStatus());
				
				Country countryData = countryRepository.getCountryWithID(groupData.getCountryID());
				groupModel.setCountryName(countryData.getCountryName());
				groupModel.setGroupInAnySession(groupData.getGroupInAnySession());

				// group members
				List<GroupMember> groupMembersModel = new ArrayList<GroupMember>();
				List<UserData> membersInDB = groupData.getMembers();

				// iterating members
				for (UserData member : membersInDB) {

					GroupMember groupMember = new GroupMember();

					groupMember.setId(member.getId());
					groupMember.setName(member.getUserName());
					groupMember.setImageKey(member.getImageKey());
					groupMember.setEmail(member.getEmail());
					String roleStringFromDb = member.getRoles();
					String[] rolesArray = roleStringFromDb.split(",", 0);
					groupMember.setRoles(rolesArray);
					groupMember.setCountryID(member.getCountryID());
					groupMember.setStatus(member.getUserStatus());
					groupMembersModel.add(groupMember);
				}

				groupModel.setMembers(groupMembersModel);
				responseModel.add(groupModel);
			}
			// final response
			groups.setGroups(responseModel);
		}

		return groups;
	}

	@Override
	public GroupData updateGroupInAnySession(GroupData group) {
		return groupRepository.save(group);
	}
}
