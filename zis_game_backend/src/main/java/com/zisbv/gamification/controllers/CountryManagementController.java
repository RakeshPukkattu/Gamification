package com.zisbv.gamification.controllers;

import java.io.IOException;
import java.util.List;

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
import com.fasterxml.jackson.databind.JsonMappingException;
import com.zisbv.gamification.entities.City;
import com.zisbv.gamification.entities.Country;
import com.zisbv.gamification.entities.Language;
import com.zisbv.gamification.entities.Region;
import com.zisbv.gamification.exceptions.MyFileNotFoundException;
import com.zisbv.gamification.models.AppResponse;
import com.zisbv.gamification.models.CountriesResponse;
import com.zisbv.gamification.service.CountryManagementService;
import com.zisbv.gamification.util.AppConstants;

@RestController
@RequestMapping("/countryManagement")
public class CountryManagementController {

	private final CountryManagementService countryManagementService;

	public CountryManagementController(CountryManagementService countryManagementService) {
		this.countryManagementService = countryManagementService;
	}

	// Add Country, Languages and Regions.
	@PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/addCountryRegionsLanguages")
	public AppResponse addCountryRegionsLanguages(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String countryJSON)
			throws JsonParseException, JsonMappingException, IOException {

		AppResponse response;

		try {

			response = countryManagementService.createCountryRegionsAndLanguages(countryJSON);

		} catch (MyFileNotFoundException e) {
			return new AppResponse("400", e.toString());
		} catch (Exception e) {
			return new AppResponse("400", "Failed to add country.");
		}

		return response;
	}

	// Add state and details with regionID.
	@PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/addState/{regionID}")
	public AppResponse addState(@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String stateJSON,
			@PathVariable Long regionID) throws JsonParseException, JsonMappingException, IOException {

		AppResponse response;

		try {

			response = countryManagementService.createState(stateJSON, regionID);

		} catch (MyFileNotFoundException e) {
			return new AppResponse("400", e.toString());
		} catch (Exception e) {
			return new AppResponse("400", "Failed to add State.");
		}

		return response;
	}
		
	// Add city and details with regionID.
	@PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/addCity/{regionID}")
	public AppResponse addCity(@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String cityJSON,
			@PathVariable Long regionID) throws JsonParseException, JsonMappingException, IOException {

		AppResponse response;

		try {

			response = countryManagementService.createCity(cityJSON,regionID);

		} catch (MyFileNotFoundException e) {
			return new AppResponse("400", e.toString());
		} catch (Exception e) {
			return new AppResponse("400", "Failed to add City.");
		}

		return response;
	}

//	// Add country with all details.
//	@PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
//	@CrossOrigin(origins = "*")
//	@PostMapping(value = "/addCountry")
//	public AppResponse addCountry(
//			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String countryJSON)
//			throws JsonParseException, JsonMappingException, IOException {
//
//		AppResponse response;
//
//		try {
//
//			response = countryManagementService.createCountry(countryJSON);
//
//		} catch (MyFileNotFoundException e) {
//			return new AppResponse("400", e.toString());
//		} catch (Exception e) {
//			return new AppResponse("400", "Failed to add country.");
//		}
//
//		return response;
//	}

	// Get all countries.
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/countries")
	public CountriesResponse getAllCountries() {
		List<Country> countries = countryManagementService.getAllCountries();
		CountriesResponse response = new CountriesResponse();
		response.setCountries(countries);
		return response;
	}

	// Get country [id]
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/country/{id}")
	public Country getCountryWithId(@PathVariable Long id) {
		return countryManagementService.getCountryWithID(id);
	}

	// Get language [id]
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/language/{id}")
	public Language getLanguageWithId(@PathVariable Long id) {
		return countryManagementService.getLanguageWithID(id);
	}

	// Get city [id]
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/city/{id}")
	public City getCityWithId(@PathVariable Long id) {
		return countryManagementService.getCityWithID(id);
	}

	// Get region [id]
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/region/{id}")
	public Region getRegionWithId(@PathVariable Long id) {
		return countryManagementService.getRegionWithID(id);
	}

//	// Modify country with ID.
//	@PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
//	@CrossOrigin(origins = "*")
//	@PutMapping(value = "/modify/{id}")
//	public AppResponse modifyCountry(
//			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String countryJSON,
//			@PathVariable(required = true) Long id) throws JsonParseException, JsonMappingException, IOException {
//
//		AppResponse response;
//
//		try {
//
//			response = countryManagementService.modifyCountry(countryJSON, id);
//
//		} catch (MyFileNotFoundException e) {
//			return new AppResponse("400", e.toString());
//		} catch (Exception e) {
//			return new AppResponse("400", "Failed to add country.");
//		}
//
//		return response;
//	}

	// Update country status [countryId].
	@PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
	@CrossOrigin(origins = "*")
	@PutMapping(value = "/updateCountryStatus/{id}")
	public AppResponse updateCountryStatus(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String inputStatus,
			@PathVariable Long id) throws JsonParseException, JsonMappingException, IOException {

		try {

			Boolean enableDisableStatus = Boolean.parseBoolean(inputStatus);
			countryManagementService.modifyCountryStatus(enableDisableStatus, id);
			return new AppResponse("200 - OK", "Country Updated.");

		} catch (Exception e) {
			return new AppResponse("400 - OK", "Country Updated failed.");
		}

	}
}
