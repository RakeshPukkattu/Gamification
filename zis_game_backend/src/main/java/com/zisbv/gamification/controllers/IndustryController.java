package com.zisbv.gamification.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zisbv.gamification.dto.AssignIndustryDto;
import com.zisbv.gamification.dto.IndustryDto;
import com.zisbv.gamification.entities.IndustryData;
import com.zisbv.gamification.models.AppResponse;
import com.zisbv.gamification.models.AssignedIndustryModel;
import com.zisbv.gamification.models.AssignedIndustryModelResponse;
import com.zisbv.gamification.models.ModifyIndustryModel;
import com.zisbv.gamification.service.IndustryService;
import com.zisbv.gamification.util.AppConstants;

@RestController
@RequestMapping("/industryManagement")
public class IndustryController {

	@Autowired
	IndustryService industryService;

	ObjectMapper objectMapper = new ObjectMapper();

	// ALL INDUSTRIES
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/industries")
	public AssignedIndustryModelResponse getAllIndustries() {
		return industryService.getIndustryModelsList();
	}

	// INDUSTRY WITH ID
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/industry/{id}")
	public AssignedIndustryModel getIndustry(@PathVariable Long id) throws Exception {
		try {
			AssignedIndustryModel result = industryService.getIndustryModel(id);
			if (result == null) {
				throw new FileNotFoundException("Not Found");
			}
			return result;
		} catch (Exception e) {
			throw new FileNotFoundException("Not Found");
		}
	}

	// CREATE INDUSTRY
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/addIndustry")
	public AppResponse addIndustry(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String industryJson)
			throws JsonParseException, JsonMappingException, IOException {

		IndustryDto industry = objectMapper.readValue(industryJson, IndustryDto.class);

		IndustryData existingIndustry = industryService.findWitIndustryName(industry.getName());
		if (existingIndustry != null) {
			String Name = existingIndustry.getIndustryName();
			if (Name != null) {
				return new AppResponse("406", "Industry name : " + Name + " is already used. ");
			}
		}

		industryService.createIndustry(industry);
		return new AppResponse(AppConstants.SUCCESS_CODE, "Industry added successfully");
	}

	// Assign industry
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/assignIndustry/{id}")
	public AppResponse assignIndustry(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String assignmentJson,
			@PathVariable(required = true) Long id) throws JsonParseException, JsonMappingException, IOException {

		AssignIndustryDto industry = objectMapper.readValue(assignmentJson, AssignIndustryDto.class);

		industryService.assignIndustry(industry, id);
		return new AppResponse(AppConstants.SUCCESS_CODE, "assignment successfully");
	}

	// MODIFY INDUSTRY WITH ID
	@CrossOrigin(origins = "*")
	@PutMapping(value = "/modifyIndustry/{id}")
	public AppResponse modifyIndustry(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String modifiedJson,
			@PathVariable(required = true) Long id) throws JsonParseException, JsonMappingException, IOException {

		ModifyIndustryModel modifiedIndustry = objectMapper.readValue(modifiedJson, ModifyIndustryModel.class);

		AppResponse response = industryService.modifyIndustry(modifiedIndustry, id);

		return response;
	}

}
