package com.zisbv.gamification.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.zisbv.gamification.dto.AdminAssignedComponentsDto;
import com.zisbv.gamification.entities.CompanyAssignedComponents;
import com.zisbv.gamification.models.AppResponse;
import com.zisbv.gamification.service.AdminAssignedComponentsService;
import com.zisbv.gamification.util.AppConstants;

@RestController
@RequestMapping("/assignCompanyComponents")
public class AssignCompanyComponentsController {

	@Autowired
	AdminAssignedComponentsService adminAssignedComponentsService;

	ObjectMapper objectMapper = new ObjectMapper();

	// assign component
	@PreAuthorize("hasAnyRole('ADMIN')")
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/assignComponents/{id}")
	public AppResponse adAssignComponents(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String assignComponentJson,
			@PathVariable Long id) throws JsonParseException, JsonMappingException, IOException {

		AdminAssignedComponentsDto component = objectMapper.readValue(assignComponentJson,
				AdminAssignedComponentsDto.class);

		CompanyAssignedComponents existingComponentForCompany = adminAssignedComponentsService.findWithCompanyID(id);
		if (existingComponentForCompany != null) {

			return new AppResponse("406", "Component are allready assigned for this company kindly go for edit. ");
		}

		adminAssignedComponentsService.assignComponents(component, id);
		return new AppResponse(AppConstants.SUCCESS_CODE, "Assignments completed successfully");
	}

	// modify all with company id
	@PreAuthorize("hasAnyRole('ADMIN')")
	@CrossOrigin(origins = "*")
	@PutMapping(value = "/updateAssignedComponents/{id}")
	public AppResponse updateAssignedComponents(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = false) String assignedComponentsJson,
			@PathVariable(required = true) Long id) throws JsonMappingException, JsonProcessingException, IOException {

		// Checking for existing company assignedComponents with id.
		CompanyAssignedComponents existingAssignedComponents = adminAssignedComponentsService.findWithCompanyID(id);
		if (existingAssignedComponents == null) {
			return new AppResponse("404-NOT FOUND", "Components assigned for  : " + id + "not found.");
		}

		AdminAssignedComponentsDto assignedComponents = objectMapper.readValue(assignedComponentsJson,
				AdminAssignedComponentsDto.class);

		CompanyAssignedComponents modifiedAssignedComponents = new CompanyAssignedComponents();

		modifiedAssignedComponents.setId(existingAssignedComponents.getId());
		modifiedAssignedComponents.setCompanyID(existingAssignedComponents.getCompanyID());

		if (assignedComponents.getThemeId() != null) {
			modifiedAssignedComponents.setThemeID(assignedComponents.getThemeId());
		} else {
			modifiedAssignedComponents.setThemeID(existingAssignedComponents.getThemeID());
		}

		if (assignedComponents.getIndustryId() != null) {
			String industryIDString = assignedComponents.getIndustryId().stream().map(Object::toString)
					.collect(Collectors.joining(","));
			modifiedAssignedComponents.setIndustryID(industryIDString);
		} else {
			modifiedAssignedComponents.setIndustryID(existingAssignedComponents.getIndustryID());
		}

		if (assignedComponents.getComponentId() != null) {
			String componentsIDString = assignedComponents.getComponentId().stream().map(Object::toString)
					.collect(Collectors.joining(","));
			modifiedAssignedComponents.setComponentID(componentsIDString);
		} else {
			modifiedAssignedComponents.setComponentID(existingAssignedComponents.getComponentID());
		}

		if (assignedComponents.getGameId() != null) {
			String gamesIDString = assignedComponents.getGameId().stream().map(Object::toString)
					.collect(Collectors.joining(","));
			modifiedAssignedComponents.setGameID(gamesIDString);
		} else {
			modifiedAssignedComponents.setGameID(existingAssignedComponents.getGameID());
		}

		modifiedAssignedComponents.setLast_modified(new Date());

		adminAssignedComponentsService.updateassignedComponents(modifiedAssignedComponents);
		return new AppResponse("200 - OK", "updated.");
	}

}
