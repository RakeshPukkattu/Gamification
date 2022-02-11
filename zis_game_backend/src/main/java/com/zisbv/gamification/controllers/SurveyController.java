package com.zisbv.gamification.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
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
import com.zisbv.gamification.dto.AssignSurveyDto;
import com.zisbv.gamification.entities.AssignedSurveyData;
import com.zisbv.gamification.entities.Country;
import com.zisbv.gamification.entities.SurveyData;
import com.zisbv.gamification.exceptions.MyFileNotFoundException;
//import com.zisbv.gamification.handlers.SurveyManagementHandler;
import com.zisbv.gamification.models.AppResponse;
import com.zisbv.gamification.models.AssignedSurveyResponseModel;
import com.zisbv.gamification.models.SurveyModel;
import com.zisbv.gamification.models.SurveyQuestionsResponseModel;
import com.zisbv.gamification.models.SurveyResponseModel;
import com.zisbv.gamification.repositories.CountryRepository;
import com.zisbv.gamification.service.SurveyDataManagementService;
import com.zisbv.gamification.util.AppConstants;

@RestController
@RequestMapping("/surveyManagement")
public class SurveyController {

//	@Autowired
//	private UserManagementService userManagementService;

//	@Autowired
//	private SurveyDataManagementService surveyManagementservice;

	private final SurveyDataManagementService surveyManagementservice;

	@Autowired
	private CountryRepository countryRepository;

	public SurveyController(SurveyDataManagementService surveyManagementservice) {
		this.surveyManagementservice = surveyManagementservice;
	}

	ObjectMapper objectMapper = new ObjectMapper();

	// Get all surveys.
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/surveys")
	public SurveyResponseModel getAllSurveys() {
		return surveyManagementservice.getSurveyResponseModel();
	}

	// Get survey [surveyId].
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/survey/{id}")
	public SurveyModel getSurveyWithId(@PathVariable Long id) {
		return surveyManagementservice.getSurveyModel(id);
	}

	// Get all survey based on country.
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/surveyInCountry/{country}")
	public SurveyResponseModel getAllSurveysInCountry(@PathVariable String country) {
		return surveyManagementservice.getSurveyResponseModelInCountry(country);
	}

	// Get questions in survey [surveyId].
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/surveyQuestions/{id}")
	public SurveyQuestionsResponseModel getQuestionsInSurvey(@PathVariable Long id) {
		return surveyManagementservice.getSurveyQuestionsResponseModel(id);
	}

	// Add survey.
	@PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/addSurvey")
	public AppResponse createSurvey(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String surveyJson,
			@RequestParam(value = "country", required = true) String country) {

		AppResponse response;
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

		System.out.println(country);
		Country dataFromDB = countryRepository.getCountryWithName(country);
				//countryRepository.getCountryWithName(country);	
		if (dataFromDB == null) {
			return new AppResponse("406", "Invalid country :" + country + " is not available. ");
		}
		System.out.println(dataFromDB);
		
		try {
			response = surveyManagementservice.createSurvey(surveyJson, dataFromDB.getId());
		} catch (MyFileNotFoundException e) {
			return new AppResponse("400", e.toString());
		} catch (Exception e) {
			return new AppResponse("400", "Failed to add survey.");
		}
		return response;
	}

	// Modify survey [ID].
	@PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
	@CrossOrigin(origins = "*")
	@PutMapping(value = "/modifySurvey/{id}")
	public AppResponse modifySurvey(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String surveyJson,
			@RequestParam(value = "country", required = true) String country, @PathVariable(required = true) Long id)
			throws JsonMappingException, JsonProcessingException, IOException, JSONException {

		// Checking for existing session with id.
		SurveyData existingSurvey = surveyManagementservice.findWithSurveyid(id);
		if (existingSurvey == null) {
			return new AppResponse("404-NOT FOUND", "survey not found with : " + id);
		}

		JSONObject surveyDataInput = new JSONObject(surveyJson);
		String surveyName = (String) surveyDataInput.get("surveyName");

		if (surveyManagementservice.findWithSurveyName(surveyName) != null) {
			SurveyData surveyInDB = surveyManagementservice.findWithSurveyName(surveyName);
			Long idFromDB = surveyInDB.getId();
			if (!idFromDB.equals(id)) {
				throw new MyFileNotFoundException("Survey name is already used !!!!");
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

		AppResponse response = surveyManagementservice.modifySurvey(surveyJson, id, countryData.getId());
		return response;
	}

	// Update survey status[surveyId].
	@PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/updateSurveyStatus/{id}")
	public AppResponse updateSurveyStatus(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) Boolean surveyStatus,
			@PathVariable Long id) throws IOException {

		Boolean status = surveyStatus;
		SurveyData existingSurvey = surveyManagementservice.findWithSurveyid(id);
		surveyManagementservice.updateStatus(existingSurvey, status);
		return new AppResponse("200 - OK", "Survey Status Updated.");

	}

	// Assign survey [List of users],[List of groups]
	@PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/assignSurvey/{id}")
	public AppResponse assignSurvey(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String userJson, @PathVariable Long id)
			throws JsonParseException, JsonMappingException, IOException {

		// Input
		AssignSurveyDto assignSurveyDto = objectMapper.readValue(userJson, AssignSurveyDto.class);
		Long surveyId = id;
		List<String> userIdsList = assignSurveyDto.getUserIDs();
		List<String> groupIdsList = assignSurveyDto.getGroupIDs();

		// check if already assigned
		SurveyData surveyModel = surveyManagementservice.findWithSurveyid(surveyId);
		if (surveyModel.getIsAssigned()) {
			return new AppResponse("400 - BAD Request", "Survey is already assigned - Try to modify.");
		}

		// Response
		AssignedSurveyData assignedSurveyData = new AssignedSurveyData();
		assignedSurveyData.setSurveyID(surveyId);

		String userIDsString = "";
		String groupIDsString = "";

		// Setting id s
		if (!userIdsList.isEmpty()) {
			for (String userID : userIdsList) {
				if (userIDsString.equals("")) {
					userIDsString = userID;
				} else {
					userIDsString = userIDsString + "," + userID;
				}
			}
		}

		if (!groupIdsList.isEmpty()) {
			for (String groupID : groupIdsList) {
				if (groupIDsString.equals("")) {
					groupIDsString = groupID;
				} else {
					groupIDsString = groupIDsString + "," + groupID;
				}
			}

		}

		assignedSurveyData.setUserID(userIDsString);
		assignedSurveyData.setGroupID(groupIDsString);
		assignedSurveyData.setLast_modified(new Date());

		// save assignment
		surveyManagementservice.saveAssignment(assignedSurveyData);

		// set flag
		surveyManagementservice.updateAssignedSurveyStatus(surveyModel, true);

		return new AppResponse("200 - OK", "Survey assigned Successfully.");
	}

	// modify assigned survey
	@PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
	@CrossOrigin(origins = "*")
	@PutMapping(value = "/modifyAssignedSurvey/{id}")
	public AppResponse modifyAssignedSurvey(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String userJson, @PathVariable Long id)
			throws JsonParseException, JsonMappingException, IOException {

		// Input
		AssignSurveyDto assignSurveyDto = objectMapper.readValue(userJson, AssignSurveyDto.class);
		Long surveyId = id;
		List<String> userIdsList = assignSurveyDto.getUserIDs();
		List<String> groupIdsList = assignSurveyDto.getGroupIDs();

		SurveyData surveyModel = surveyManagementservice.findWithSurveyid(surveyId);

		if (surveyModel == null) {
			return new AppResponse("400 - BAD REQUEST", "Survey not found with id:" + surveyId + ".");
		}

		// delete and change status

		AssignedSurveyData assignedSurveyDataDB = surveyManagementservice.getSurveyAssignmentsData(surveyId);

		if (assignedSurveyDataDB == null) {
			return new AppResponse("400 - BAD REQUEST", "Survey not assigned, Kindly assign before modifying.");
		}

		Long assignedSurveyDataID = assignedSurveyDataDB.getId();
		surveyManagementservice.removeSurveyAssignments(assignedSurveyDataID);
		surveyManagementservice.updateAssignedSurveyStatus(surveyModel, false);

		// add and change status
		// Response
		AssignedSurveyData assignedSurveyData = new AssignedSurveyData();
		assignedSurveyData.setSurveyID(surveyId);

		String userIDsString = "";
		String groupIDsString = "";

		// Setting id s
		if (!userIdsList.isEmpty()) {
			for (String userID : userIdsList) {
				if (userIDsString.equals("")) {
					userIDsString = userID;
				} else {
					userIDsString = userIDsString + "," + userID;
				}
			}
		}

		if (!groupIdsList.isEmpty()) {
			for (String groupID : groupIdsList) {
				if (groupIDsString.equals("")) {
					groupIDsString = groupID;
				} else {
					groupIDsString = groupIDsString + "," + groupID;
				}
			}

		}

		assignedSurveyData.setUserID(userIDsString);
		assignedSurveyData.setGroupID(groupIDsString);
		assignedSurveyData.setLast_modified(new Date());

		// save assignment
		surveyManagementservice.saveAssignment(assignedSurveyDataDB, assignedSurveyData);

		// set flag
		surveyManagementservice.updateAssignedSurveyStatus(surveyModel, true);

		return new AppResponse("200 - OK", "Survey assigned Successfully.");
	}

	// Get assigned surveys [survey ID].
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/surveyAssignments/{id}")
	public AssignedSurveyResponseModel getSurveyAssignmentsWithId(@PathVariable Long id) {
		return surveyManagementservice.getAssignedSurveyResponseModel(String.valueOf(id));
	}
}
