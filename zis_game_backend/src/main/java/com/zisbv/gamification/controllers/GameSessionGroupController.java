package com.zisbv.gamification.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.zisbv.gamification.dto.GameSessionGroupDto;
import com.zisbv.gamification.entities.Country;
import com.zisbv.gamification.entities.GameSessionGroupData;
import com.zisbv.gamification.entities.GroupData;
import com.zisbv.gamification.entities.UserData;
import com.zisbv.gamification.handlers.GameSessionManagementHandler;
import com.zisbv.gamification.models.AppResponse;
import com.zisbv.gamification.models.GameSessionModel;
import com.zisbv.gamification.models.GameSessionResponseModel;
import com.zisbv.gamification.models.GroupInSessionModel;
import com.zisbv.gamification.repositories.CountryRepository;
import com.zisbv.gamification.repositories.GameSessionStatus;
import com.zisbv.gamification.repositories.GroupRepository;
import com.zisbv.gamification.service.GameSessionManagementService;
import com.zisbv.gamification.util.AppConstants;

@RestController
@RequestMapping("/gameSessionManagement")
public class GameSessionGroupController {

//	@Autowired
//	private UserManagementService userManagementService;

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private CountryRepository countryRepository;

	private final GameSessionManagementService gameSessionManagementservice;

	public GameSessionGroupController(GameSessionManagementService gameSessionManagementservice) {
		this.gameSessionManagementservice = gameSessionManagementservice;
	}

	GameSessionManagementHandler handler = new GameSessionManagementHandler();
	ObjectMapper objectMapper = new ObjectMapper();

	// Get all Sessions.
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/gameSessions")
	public GameSessionResponseModel getAllSessions() {
		return gameSessionManagementservice.getSessionResponseModel();
	}

	// Get all session based on country.
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/gameSession/sessionInCountry/{country}")
	public GameSessionResponseModel getAllSessionsInCountry(@PathVariable String country) {
		return gameSessionManagementservice.getSessionResponseModelGroupedbyCountry(country);
	}

	// Get session with id.
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/gameSession/{id}")
	public GameSessionModel getSession(@PathVariable Long id) throws Exception {
		try {
			GameSessionModel result = gameSessionManagementservice.getSession(id);

			if (result == null) {
				throw new FileNotFoundException("Not Found");
			}
			return result;
		} catch (Exception e) {
			throw new FileNotFoundException("Not Found");
		}
	}

	// Get all groups in session{ID}.
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/gameSessions/groupInSession/{id}")
	public List<GroupInSessionModel> getGroupsInSession(@PathVariable Long id) throws Exception {
		return gameSessionManagementservice.getGroupInSession(id);
	}

	// Create Sessions
	@PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/createGameSession")
	public AppResponse createSession(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String gameSessionJson,
			@RequestParam(value = "country", required = false) String country)
			throws JsonParseException, JsonMappingException, IOException {

//		UserData userData = userManagementService.findWithid(userID);
//		Boolean isSuperAdmin = false;
//		Long userCountryID = null;
//
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

		GameSessionGroupDto session = objectMapper.readValue(gameSessionJson, GameSessionGroupDto.class);

		String finalStartT = session.getStartDateandTime() + ":00z";
		String finalEndT = session.getEndDateandTime() + ":00z";

		GameSessionGroupData data = new GameSessionGroupData();

		data.setSessionName(session.getSessionName());
		Instant instant = Instant.parse(finalStartT);
		LocalDateTime result = LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId()));
		data.setStartDateAndTime(result);
		Instant instant1 = Instant.parse(finalEndT);
		LocalDateTime result1 = LocalDateTime.ofInstant(instant1, ZoneId.of(ZoneOffset.UTC.getId()));
		data.setEndDateAndTime(result1);
		data.setSessionEnableDisable(true);
		data.setSessionCurrentStatus("not_Initiated");

		for (String groupID : session.getGroupIds()) {
			GroupData group = groupRepository.getGroupWithID(Long.parseLong(groupID));
			data.getGroups().add(group);
		}

		// NAME VALIDATION :: Check for Duplicate Name.
		GameSessionGroupData existingSession = gameSessionManagementservice.findWithName(session.getSessionName());
		if (existingSession != null) {
			String session_Name = existingSession.getSessionName();
			if (session_Name != null) {
				return new AppResponse("406", "Session name : " + session_Name + " is not available. ");
			}
		}

		// SESSION PERIOD VALIDATION :: minimum session time is 4 hours .
		long SessionTimer = 4;
		LocalDateTime startDateTime = data.getStartDateAndTime();
		LocalDateTime endDateTime = data.getEndDateAndTime();

		Period period = Period.between(startDateTime.toLocalDate(), endDateTime.toLocalDate());
		Duration duration = Duration.between(startDateTime.toLocalTime(), endDateTime.toLocalTime());

		if (duration.isNegative()) {
			period = period.minusDays(1);
			duration = duration.plusDays(1);
		}
		long seconds = duration.getSeconds();
		long hours = seconds / 3600;
		long minutes = ((seconds % 3600) / 60);
		long secs = (seconds % 60);
		long time[] = { hours, minutes, secs };

		if (period.getDays() < 1) {
			if (SessionTimer > time[0]) {
				return new AppResponse("406", "Session period should not be less than 4 hours. ");
			}
		}

		// GROUP VALIDATION
		// new
		List<GroupData> groups = data.getGroups();
		// end

		for (GroupData group : groups) {

			if (!group.getGroupStatus()) {
				return new AppResponse("400", "Group with id " + group.getGroupID() + "is not active.");
			}

			List<UserData> groupMembers = group.getMembers();
			for (UserData member : groupMembers) {

				// USER VALIDATION : Check if user is free for this session.
				boolean userIsFreeForSession = gameSessionManagementservice.checkIfUserIsFreeForSession(member,
						session.getStartDateandTime());
				if (!userIsFreeForSession) {
					return new AppResponse("400",
							member.getId() + member.getUserName() + ": is not free to join this session");
				}
			}
		}

		Country countryData = countryRepository.getCountryWithName(country);
		if (countryData == null) {
			return new AppResponse("406", "Invalid country :" + country + " is not available. ");
		}

		gameSessionManagementservice.createGameSession(data, countryData.getId());
		return new AppResponse(AppConstants.SUCCESS_CODE, "Session created successfully");
	}

	// Update session Enable-Disable.
	@PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/updateEnableDisable/{id}")
	public AppResponse updateEnableDisable(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String sessionJson,
			@PathVariable Long id) throws JsonParseException, JsonMappingException, IOException {

		GameSessionModel session = objectMapper.readValue(sessionJson, GameSessionModel.class);
		Boolean status = session.getSessionEnableDisable();

		GameSessionGroupData existingSession = gameSessionManagementservice.findWithid(id);
		GameSessionGroupData newSession = handler.updateEnableDisable(existingSession, status);

		gameSessionManagementservice.updateStatus(newSession);
		return new AppResponse("200 - OK", "Status Updated.");

	}

	// Update current session status.
	@PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/updateSessionStatus/{id}")
	public AppResponse updateCurrentSessionStatus(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String sessionJson,
			@PathVariable Long id) throws JsonParseException, JsonMappingException, IOException {

		GameSessionModel session = objectMapper.readValue(sessionJson, GameSessionModel.class);
		String status = session.getSessionCurrentStatus();

		if (!status.equalsIgnoreCase(GameSessionStatus.not_Initiated.toString())) {
			if (!status.equalsIgnoreCase(GameSessionStatus.about_To_Start.toString())) {
				if (!status.equalsIgnoreCase(GameSessionStatus.in_progress.toString())) {
					if (!status.equalsIgnoreCase(GameSessionStatus.completed.toString())) {
						return new AppResponse("BAD REQUEST", "USE VALID PARAMETERS ONLY.");
					}
				}
			}
		}

		GameSessionGroupData existingSession = gameSessionManagementservice.findWithid(id);
		GameSessionGroupData newSession = handler.updateCurrentSessionStatus(existingSession, status);
		gameSessionManagementservice.updateStatus(newSession);
		return new AppResponse("200 - OK", "Status Updated.");
	}

	// modify session.
	@PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
	@CrossOrigin(origins = "*")
	@PutMapping(value = "/modifySession/{id}")
	public AppResponse modifySession(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String sessionJson,
			@RequestParam(value = "country", required = false) String country, @PathVariable(required = true) Long id)
			throws JsonMappingException, JsonProcessingException, IOException {

		// Checking for existing session with id.
		GameSessionGroupData existingSession = gameSessionManagementservice.findWithid(id);
		if (existingSession == null) {
			return new AppResponse("404-NOT FOUND", "Session not found with : " + id);
		}

		GameSessionGroupDto session = objectMapper.readValue(sessionJson, GameSessionGroupDto.class);
		String newName = session.getSessionName(); // new name given
		String existingName = existingSession.getSessionName(); // old name given

		String finalStartT = session.getStartDateandTime() + ":00z";
		String finalEndT = session.getEndDateandTime() + ":00z";

		GameSessionGroupData data = new GameSessionGroupData();

		data.setSessionName(session.getSessionName());
		Instant instant = Instant.parse(finalStartT);
		LocalDateTime result = LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId()));
		data.setStartDateAndTime(result);
		Instant instant1 = Instant.parse(finalEndT);
		LocalDateTime result1 = LocalDateTime.ofInstant(instant1, ZoneId.of(ZoneOffset.UTC.getId()));
		data.setEndDateAndTime(result1);
		data.setSessionEnableDisable(true);
		data.setSessionCurrentStatus("not_Initiated");

		for (String groupID : session.getGroupIds()) {
			GroupData group = groupRepository.getGroupWithID(Long.parseLong(groupID));
			data.getGroups().add(group);
		}

		// Checking new name is used or not.
		if (gameSessionManagementservice.findWithName(newName) != null) {
			Boolean matcher = newName.equalsIgnoreCase(existingName);
			if (!matcher) {
				return new AppResponse("406-NOT ACCEPTABLE", "Session name is not available : " + newName);
			}
		}

		// SESSION PERIOD VALIDATION :: minimum session time is 4 hours .
		long SessionTimer = 4;
		LocalDateTime startDateTime = data.getStartDateAndTime();
		LocalDateTime endDateTime = data.getEndDateAndTime();

		Period period = Period.between(startDateTime.toLocalDate(), endDateTime.toLocalDate());
		Duration duration = Duration.between(startDateTime.toLocalTime(), endDateTime.toLocalTime());

		if (duration.isNegative()) {
			period = period.minusDays(1);
			duration = duration.plusDays(1);
		}
		long seconds = duration.getSeconds();
		long hours = seconds / 3600;
		long minutes = ((seconds % 3600) / 60);
		long secs = (seconds % 60);
		long time[] = { hours, minutes, secs };

		if (period.getDays() < 1) {
			if (SessionTimer > time[0]) {
				return new AppResponse("406", "Session period should not be less than 4 hours. ");
			}
		}

		// GROUP VALIDATION
		List<GroupData> sessionGroups = data.getGroups();
		for (GroupData sessionGroup : sessionGroups) {

			if (!sessionGroup.getGroupStatus()) {
				return new AppResponse("400", "Group is not active  : " + sessionGroup.getGroupID());
			}

			List<UserData> groupMembers = sessionGroup.getMembers();
			for (UserData member : groupMembers) {

				// USER VALIDATION : Check if user is free for this session.
				boolean userIsFreeForSession = gameSessionManagementservice.checkIfUserIsFreeForSession(member, id,
						session.getStartDateandTime());
				if (!userIsFreeForSession) {
					return new AppResponse("400",
							member.getId() + member.getUserName() + ": is not free to join this session");
				}
			}
		}

//		UserData userData = userManagementService.findWithid(userID);
//		Boolean isSuperAdmin = false;
//		Long userCountryID = null;
//
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
		if (countryData == null) {
			return new AppResponse("406", "Invalid country :" + country + " is not available. ");
		}
		gameSessionManagementservice.updateGameSession(session, existingSession, countryData.getId());
		return new AppResponse("200 - OK", "Session modified.");

	}

}
