package com.zisbv.gamification.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zisbv.gamification.dto.GroupDto;
import com.zisbv.gamification.entities.Country;
import com.zisbv.gamification.entities.GroupData;
import com.zisbv.gamification.entities.UserData;
import com.zisbv.gamification.handlers.GroupManagementHandler;
import com.zisbv.gamification.models.AppResponse;
import com.zisbv.gamification.models.GroupModel;
import com.zisbv.gamification.models.GroupsResponseModel;
import com.zisbv.gamification.repositories.CountryRepository;
import com.zisbv.gamification.repositories.UserRepository;
import com.zisbv.gamification.service.GroupManagementService;
import com.zisbv.gamification.util.AppConstants;

@RestController
@RequestMapping("/groupManagement")
public class GroupController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CountryRepository countryRepository;

	private final GroupManagementService groupManagementservice;

	public GroupController(GroupManagementService groupManagementservice) {
		this.groupManagementservice = groupManagementservice;
	}

	GroupManagementHandler handler = new GroupManagementHandler();
	ObjectMapper objectMapper = new ObjectMapper();

	// Get all groups.
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/groups")
	public GroupsResponseModel getAllGroups() {
		return groupManagementservice.getGroupResponseModel();
	}

	// Get groups with id.
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/group/{id}")
	public GroupModel getGroup(@PathVariable Long id) throws Exception {
		try {
			GroupModel result = groupManagementservice.getGroup(id);

			if (result == null) {
				throw new FileNotFoundException("Not Found");
			}
			return result;
		} catch (Exception e) {
			throw new FileNotFoundException("Not Found");
		}
	}

	// Get all groups based on country.
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/groupsInCountry/{country}")
	public GroupsResponseModel getAllGroupsInCountry(@PathVariable String country) {
		return groupManagementservice.getGroupResponseModelGroupedbyCountry(country);
	}

	// Create group
	@PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/creategroup")
	public AppResponse createGroup(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String groupJson,
			@RequestParam(value = "country", required = true) String country)
			throws JsonParseException, JsonMappingException, IOException {

		//UserData userData = userRepository.findById(userID);
		//Boolean isSuperAdmin = false;
		//Long userCountryID = null;

		//String roles = userData.getRoles();
		//String[] rolesArray = roles.split(",");
		//for (String role : rolesArray) {
			//if (role.equalsIgnoreCase("SUPERADMIN")) {
				//isSuperAdmin = true;
			//}
		//}

		//if (isSuperAdmin) {
			//if (countryID == null) {
				//return new AppResponse("406", "Please select  a country.");
			//}
			//userCountryID = countryID;
		//} else {
			//userCountryID = userData.getCountryID();
		//}

		GroupDto group = objectMapper.readValue(groupJson, GroupDto.class);

		GroupData existingGroup = groupManagementservice.findWithName(group.getGroupName());
		if (existingGroup != null) {
			String grpName = existingGroup.getGroupName();
			if (grpName != null) {
				return new AppResponse("406", "Group name : " + grpName + " is not available. ");
			}
		}
		
		Country countryData = countryRepository.getCountryWithName(country);
		if(countryData == null) {
			return new AppResponse("406", "Invalid country :" + country + " is not available. ");
		}

		groupManagementservice.createGroup(group, countryData.getId());
		return new AppResponse(AppConstants.SUCCESS_CODE, "Group created successfully");
	}

	// Update group status
	@PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/updateStatus/{id}")
	public AppResponse updateStatus(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String groupJson,
			@PathVariable Long id) throws JsonParseException, JsonMappingException, IOException {

		GroupModel group = objectMapper.readValue(groupJson, GroupModel.class);
		Boolean status = group.getGroupStatus();
		
		GroupData existingGroup = groupManagementservice.findWithid(id);
		GroupData newUser = handler.updateStatus(existingGroup, status);
		
		groupManagementservice.updateGroupStatus(newUser);
		return new AppResponse("200 - OK", "Status Updated.");

	}

	// modify group
	@PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
	@CrossOrigin(origins = "*")
	@PutMapping(value = "/modifyGroup/{id}")
	public AppResponse modifyGroup(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String groupJson,
			@RequestParam(value = "country", required = false) String country, @PathVariable(required = true) Long id)
			throws JsonMappingException, JsonProcessingException, IOException {

		// Checking for existing group with id.
		GroupData existingGroup = groupManagementservice.findWithid(id);
		if (existingGroup == null) {
			return new AppResponse("404-NOT FOUND", "Group not found with : " + id);
		}

		GroupDto group = objectMapper.readValue(groupJson, GroupDto.class);
		String newName = group.getGroupName(); // new name given
		String existingName = existingGroup.getGroupName(); // old name given

		// Checking new name is used or not.
		if (groupManagementservice.findWithName(newName) != null) {
			Boolean matcher = newName.equalsIgnoreCase(existingName);
			if (!matcher) {
				return new AppResponse("406-NOT ACCEPTABLE", "Group name is not available : " + newName);
			}
		}

		// change old members status
		List<UserData> oldMembers = existingGroup.getMembers();
		for (UserData oldMember : oldMembers) {
			oldMember.setUserInAnyGroup(false);
			userRepository.save(oldMember);
		}

//		UserData userData = userRepository.findById(userID);
//		Boolean isSuperAdmin = false;
//		Long userCountryID = null;

//		String roles = userData.getRoles();
//		String[] rolesArray = roles.split(",");
//		for (String role : rolesArray) {
//			if (role.equalsIgnoreCase("SUPERADMIN")) {
//				isSuperAdmin = true;
//			}
//		}
//
//		if (isSuperAdmin) {
//			if (countryID == null) {
//				return new AppResponse("406", "Please select  a country.");
//			}
//			userCountryID = countryID;
//		} else {
//			userCountryID = userData.getCountryID();
//		}

		Country countryData = countryRepository.getCountryWithName(country);
		if(countryData == null) {
			return new AppResponse("406", "Invalid country :" + country + " is not available. ");
		}
		
		groupManagementservice.updateGroup(group, existingGroup, countryData.getId());
		return new AppResponse("200 - OK", "Group modified.");

	}

	// delete group with id.
	@PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = "/delete/{id}")
	public AppResponse deleteGroupWithID(@PathVariable(required = true) Long id) throws Exception {
		Boolean isRemoved = groupManagementservice.deleteGroupWithID(id);

		if (!isRemoved) {
			return new AppResponse(HttpStatus.NOT_FOUND.toString(), id + " :not found.");
		}

		return new AppResponse(HttpStatus.OK.toString(), "Group with " + id + " :removed.");
	}

	// delete group with id.
	@PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = "/deleteGroups")
	public AppResponse deleteGroups(@RequestParam(value = "groupIds", required = true) String groupJson)
			throws JsonMappingException, JsonProcessingException, IOException {

		GroupDto group = objectMapper.readValue(groupJson, GroupDto.class);
		String groupMembersString = group.getGroupMemberIds().stream().map(Object::toString)
				.collect(Collectors.joining(","));

		String[] ids = groupMembersString.split(",");
		Long[] data = new Long[ids.length];
		for (int i = 0; i < ids.length; i++) {
			data[i] = Long.valueOf(ids[i]);
		}

		String isRemoved = groupManagementservice.deleteGroups(data);

		return new AppResponse(HttpStatus.OK.toString(), isRemoved);
	}

}
