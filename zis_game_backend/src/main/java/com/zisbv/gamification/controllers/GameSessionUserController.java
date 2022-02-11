package com.zisbv.gamification.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
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
import com.zisbv.gamification.dto.GameSessionUserDto;
import com.zisbv.gamification.entities.Country;
import com.zisbv.gamification.entities.GameSessionUserData;
import com.zisbv.gamification.entities.UserData;
import com.zisbv.gamification.handlers.GameSessionManagementHandler;
import com.zisbv.gamification.models.AppResponse;
import com.zisbv.gamification.models.GameSessionUserModel;
import com.zisbv.gamification.models.GameSessionUserResponseModel;
import com.zisbv.gamification.models.UserInGroupSessionModel;
import com.zisbv.gamification.repositories.CountryRepository;
import com.zisbv.gamification.repositories.GameSessionStatus;
import com.zisbv.gamification.service.GameSessionManagementUserService;
import com.zisbv.gamification.service.UserManagementService;
import com.zisbv.gamification.util.AppConstants;

@RestController
@RequestMapping("/gameSessionUserManagement")
public class GameSessionUserController {

//	@Autowired
//	private GameSessionManagementUserService gameSessionManagementUserService;

	@Autowired
	private UserManagementService userManagementService;
	
	@Autowired
	private CountryRepository countryRepository;
 
	private final GameSessionManagementUserService gameSessionManagementUserService;

	public GameSessionUserController(GameSessionManagementUserService gameSessionManagementUserService) {
		this.gameSessionManagementUserService = gameSessionManagementUserService;
	}

	GameSessionManagementHandler handler = new GameSessionManagementHandler();
	ObjectMapper objectMapper = new ObjectMapper();

	// Get all Sessions.
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/gameUserSessions")
	public GameSessionUserResponseModel getAllSessions() {
		return gameSessionManagementUserService.getSessionUserResponseModel();
	}

	// Get all session based on country.
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/gameUserSession/sessionInCountry/{country}")
	public GameSessionUserResponseModel getAllUserSessionsInCountry(@PathVariable String country) {
		return gameSessionManagementUserService.getUserSessionResponseModelGroupedbyCountry(country);
	}

	// Get session with id.
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/gameUserSession/{id}")
	public GameSessionUserModel getSession(@PathVariable Long id) throws Exception {
		try {
			GameSessionUserModel result = gameSessionManagementUserService.getSession(id);

			if (result == null) {
				throw new FileNotFoundException("Not Found");
			}
			return result;
		} catch (Exception e) {
			throw new FileNotFoundException("Not Found");
		}
	}

	// Get all users in session{ID}.
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/gameUserSessions/userInSession/{id}")
	public List<UserInGroupSessionModel> getUsersInSession(@PathVariable Long id) throws Exception {
		return gameSessionManagementUserService.getuserInSession(id);
	}

	// Create Sessions
	@PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/createGameUserSession")
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

		GameSessionUserDto session = objectMapper.readValue(gameSessionJson, GameSessionUserDto.class);
		GameSessionUserData data = session.dataFromPojo();

		// NAME VALIDATION :: Check for Duplicate Name.
		GameSessionUserData existingSession = gameSessionManagementUserService.findWithName(session.getSessionName());
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

		// USER VALIDATION
		String userIds = data.getUserIds();
		String[] userIdsArray = userIds.split(",");

		for (String uID : userIdsArray) {
			UserData userModel = userManagementService.findWithid(Long.parseLong(uID));

			// :: null check , active check for users.
			if (userModel == null) {
				return new AppResponse("406", "User not found with id : " + uID);
			} else {
				if (!userModel.getUserStatus()) {
					return new AppResponse("400", "User is not active  : " + uID);
				}
			}

			// USER VALIDATION : Check if user is free for this session.
			boolean userIsFreeForSession = gameSessionManagementUserService.checkIfUserIsFreeForSession(userModel,
					session.getStartDateandTime());
			if (!userIsFreeForSession) {
				return new AppResponse("400",
						userModel.getId() + userModel.getUserName() + ": is not free to join this session");
			}

		}
		
		Country countryData = countryRepository.getCountryWithName(country);
		if (countryData == null) {
			return new AppResponse("406", "Invalid country :" + country + " is not available. ");
		}
		
		gameSessionManagementUserService.createGameSession(session, countryData.getId());
		return new AppResponse(AppConstants.SUCCESS_CODE, "Session created successfully");

	}// end

	// Update session Enable-Disable.
	@PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/updateEnableDisable/{id}")
	public AppResponse updateEnableDisable(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String sessionJson,
			@PathVariable Long id) throws JsonParseException, JsonMappingException, IOException {

		GameSessionUserModel session = objectMapper.readValue(sessionJson, GameSessionUserModel.class);
		Boolean status = session.getSessionEnableDisable();

		GameSessionUserData existingSession = gameSessionManagementUserService.findWithid(id);
		GameSessionUserData newSession = handler.updateEnableDisable(existingSession, status);
		gameSessionManagementUserService.updateStatus(newSession);
		return new AppResponse("200 - OK", "Status Updated.");

	}

	// Update current session status.
	@PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/updateSessionStatus/{id}")
	public AppResponse updateCurrentSessionStatus(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String sessionJson,
			@PathVariable Long id) throws JsonParseException, JsonMappingException, IOException {

		GameSessionUserModel session = objectMapper.readValue(sessionJson, GameSessionUserModel.class);
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

		GameSessionUserData existingSession = gameSessionManagementUserService.findWithid(id);
		GameSessionUserData newSession = handler.updateCurrentSessionStatus(existingSession, status);
		gameSessionManagementUserService.updateStatus(newSession);
		return new AppResponse("200 - OK", "Status Updated.");
	}

	// modify session.
	@PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
	@CrossOrigin(origins = "*")
	@PutMapping(value = "/modifyGameUserSession/{id}")
	public AppResponse modifySession(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String sessionJson,
			@RequestParam(value = "country", required = false) String country, @PathVariable(required = true) Long id)
			throws JsonMappingException, JsonProcessingException, IOException {

		// Checking for existing session with id.
		GameSessionUserData existingSession = gameSessionManagementUserService.findWithid(id);
		if (existingSession == null) {
			return new AppResponse("404-NOT FOUND", "Session not found with : " + id);
		}

		GameSessionUserDto session = objectMapper.readValue(sessionJson, GameSessionUserDto.class);
		String newName = session.getSessionName(); // new name given
		String existingName = existingSession.getSessionName(); // old name given
		GameSessionUserData data = session.dataFromPojo();

		// Checking new name is used or not.
		if (gameSessionManagementUserService.findWithName(newName) != null) {
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

		// USER VALIDATION
		String userIds = data.getUserIds();
		String[] userIdsArray = userIds.split(",");

		for (String uID : userIdsArray) {
			UserData userModel = userManagementService.findWithid(Long.parseLong(uID));

			// :: null check , active check for users.
			if (userModel == null) {
				return new AppResponse("406", "User not found with id : " + uID);
			} else {
				if (!userModel.getUserStatus()) {
					return new AppResponse("400", "User is not active  : " + uID);
				}
			}

			// USER VALIDATION : Check if user is free for this session.
			boolean userIsFreeForSession = gameSessionManagementUserService.checkIfUserIsFreeForSession(userModel, id,
					session.getStartDateandTime());
			if (!userIsFreeForSession) {
				return new AppResponse("400",
						userModel.getId() + userModel.getUserName() + ": is not free to join this session");
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

		GameSessionUserData modifiedSession = handler.modifySession(session, existingSession, countryData.getId());
		gameSessionManagementUserService.updateGameUserSession(modifiedSession, existingSession);
		return new AppResponse("200 - OK", "Session modified.");

	}// end
}
