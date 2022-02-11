package com.zisbv.gamification.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;

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
import com.zisbv.gamification.dto.CompanyDto;
import com.zisbv.gamification.entities.CompanyData;
import com.zisbv.gamification.handlers.CompanyManagementHandler;
import com.zisbv.gamification.models.AppResponse;
import com.zisbv.gamification.models.CompanyModel;
import com.zisbv.gamification.models.CompanyResponseModel;
import com.zisbv.gamification.service.CompanyManagementService;
import com.zisbv.gamification.util.AppConstants;

@RestController
@RequestMapping("/companyManagement")
public class CompanyManagementController {

	@Autowired
	CompanyManagementService companyManagementService;

	ObjectMapper objectMapper = new ObjectMapper();
	CompanyManagementHandler handler = new CompanyManagementHandler();

	// get all companies as list
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/allCompanies")
	public CompanyResponseModel getAllCompanies() {
		return companyManagementService.getCompanyResponseModel();
	}

	// get company with id
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/company/{id}")
	public CompanyModel get(@PathVariable Long id) throws Exception {
		try {
			CompanyModel result = companyManagementService.getCompanyModel(id);

			if (result == null) {
				throw new FileNotFoundException("Not Found");
			}
			return result;
		} catch (Exception e) {
			throw new FileNotFoundException("Not Found");
		}
	}

	// create company
	@PreAuthorize("hasAnyRole('ADMIN')")
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/registerCompany")
	public AppResponse registerCompany(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String companyJson)
			throws JsonParseException, JsonMappingException, IOException {

		CompanyDto company = objectMapper.readValue(companyJson, CompanyDto.class);

		CompanyData existingCompany = companyManagementService.findWithName(company.getCompanyName());
		if (existingCompany != null) {
			String cmpName = existingCompany.getCompanyName();
			if (cmpName != null) {
				return new AppResponse("406", "Company name : " + cmpName + " is already registered. ");
			}
		}

		companyManagementService.createCompany(company);
		return new AppResponse(AppConstants.SUCCESS_CODE, "Company registered successfully");
	}

	// update status
	@PreAuthorize("hasAnyRole('ADMIN')")
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/updateStatus/{id}")
	public AppResponse updateStatus(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String companyJson,
			@PathVariable Long id) throws JsonParseException, JsonMappingException, IOException {

		CompanyModel company = objectMapper.readValue(companyJson, CompanyModel.class);
		Boolean status = company.getStatus();
		CompanyData existingCompany = companyManagementService.findWithid(id);
		CompanyData newCompany = handler.updateStatus(existingCompany, status);
		companyManagementService.updateCompanyStatus(newCompany);
		return new AppResponse("200 - OK", "Status Updated.");

	}

	// modify company
	@PreAuthorize("hasAnyRole('ADMIN')")
	@CrossOrigin(origins = "*")
	@PutMapping(value = "/modifyCompany/{id}")
	public AppResponse modifyCompany(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String companyJson,
			@PathVariable(required = true) Long id) throws JsonMappingException, JsonProcessingException, IOException {

		// Checking for existing group with id.
		CompanyData existingCompany = companyManagementService.findWithid(id);
		if (existingCompany == null) {
			return new AppResponse("404-NOT FOUND", "Company not found with : " + id);
		}

		CompanyDto company = objectMapper.readValue(companyJson, CompanyDto.class);
		String newName = company.getCompanyName(); // new name given
		String existingName = existingCompany.getCompanyName();// old name given

		// Checking new name is used or not.
		if (companyManagementService.findWithName(newName) != null) {
			Boolean matcher = newName.equalsIgnoreCase(existingName);
			if (!matcher) {
				return new AppResponse("406-NOT ACCEPTABLE", " using pre-registered company name : " + newName);
			}
		}

		CompanyData modifiedCompany = handler.modifyCompany(company, existingCompany);
		companyManagementService.updateCompany(modifiedCompany);
		return new AppResponse("200 - OK", "Company details modified.");

	}

}
