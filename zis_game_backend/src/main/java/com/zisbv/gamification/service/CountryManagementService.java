package com.zisbv.gamification.service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zisbv.gamification.entities.City;
import com.zisbv.gamification.entities.Country;
import com.zisbv.gamification.entities.Language;
import com.zisbv.gamification.entities.Region;
import com.zisbv.gamification.entities.State;
import com.zisbv.gamification.exceptions.MyFileNotFoundException;
import com.zisbv.gamification.models.AppResponse;
import com.zisbv.gamification.repositories.CityRepository;
import com.zisbv.gamification.repositories.CountryRepository;
import com.zisbv.gamification.repositories.LanguageRepository;
import com.zisbv.gamification.repositories.RegionRepository;
import com.zisbv.gamification.repositories.StateRepository;

@Service(value = "countryManagementService")
public class CountryManagementService {

	@Autowired
	CountryRepository countryRepository;

	@Autowired
	LanguageRepository languageRepository;

	@Autowired
	StateRepository stateRepository;

	@Autowired
	CityRepository cityRepository;

	@Autowired
	RegionRepository regionRepository;

	public AppResponse createCountryRegionsAndLanguages(String countryJSON) throws JSONException {

		// Objects.
		Country countryData = new Country();

		// Input data.
		JSONObject countryInputData = new JSONObject(countryJSON);

		String countryName = (String) countryInputData.get("countryName");

		// Country name validation.
		if (countryRepository.getCountryWithName(countryName) != null) {
			throw new MyFileNotFoundException("Country already registered!!!!");
		}

		countryData.setCountryName(countryName);
		countryData.setAliasCountryName(countryName.toUpperCase());
		countryData.setEnableDisableStatus(true);

		// languages.
		JSONArray languages = (JSONArray) countryInputData.get("languages");
		for (int i = 0; i < languages.length(); i++) {
			Language languageData = new Language();
			String languageName = (String) languages.get(i);
			languageData.setLanguageName(languageName);
			countryData.getLanguages().add(languageData);
		}

		// regions.
		JSONArray regions = (JSONArray) countryInputData.get("regions");
		for (int i = 0; i < regions.length(); i++) {
			Region regionData = new Region();
			String regionName = (String) regions.get(i);
			regionData.setRegionName(regionName);
			countryData.getRegions().add(regionData);
		}

		countryRepository.save(countryData);
		return new AppResponse("200", "Country added successfully.");
	}

	public AppResponse createState(String stateJSON, Long regionID) throws JSONException {

		// Objects.
		Region regionData = regionRepository.getById(regionID);
		System.out.println(regionData.getRegionName());
		System.out.println(stateJSON);

		// Input data.
		JSONObject stateInputData = new JSONObject(stateJSON);
		System.out.println(stateInputData);

		// states.
		JSONArray states = (JSONArray) stateInputData.get("states");
		for (int i = 0; i < states.length(); i++) {

			JSONObject state = (JSONObject) states.get(i);
			// stateName
			String stateName = (String) state.get("stateName");

			// State name validation.
			if (stateRepository.getStateWithName(stateName) != null) {
				throw new MyFileNotFoundException(stateName + "State already registered!!!!");
			}

			State stateData = new State();
			stateData.setStateName(stateName);

			regionData.getStates().add(stateData);

		}

		regionRepository.save(regionData);
		return new AppResponse("200", "States added successfully.");
	}

	public AppResponse createCity(String cityJSON, Long stateID) throws JSONException {

		// Objects.
		State stateData = stateRepository.getById(stateID);

		// Input data.
		JSONObject cityInputData = new JSONObject(cityJSON);

		// cities.
		JSONArray cities = (JSONArray) cityInputData.get("cities");
		for (int i = 0; i < cities.length(); i++) {

			JSONObject city = (JSONObject) cities.get(i);
			// cityName
			String cityName = (String) city.get("cityName");
			// cityLattitude
			String cityLattitude = (String) city.get("cityLattitude");
			// cityLongitude
			String cityLongitude = (String) city.get("cityLongitude");

			// City name validation.
			if (cityRepository.getCityWithName(cityName) != null) {
				throw new MyFileNotFoundException(cityName + "City already registered!!!!");
			}

			City cityData = new City();
			cityData.setCityName(cityName);
			cityData.setCityLattitude(cityLattitude);
			cityData.setCityLongitude(cityLongitude);

			stateData.getCities().add(cityData);

		}

		stateRepository.save(stateData);
		return new AppResponse("200", "Cities added successfully.");
	}

//	public AppResponse createCountry(String countryJSON) throws JSONException {
//
//		// Objects.
//		Country countryData = new Country();
//
//		// Input data.
//		JSONObject countryInputData = new JSONObject(countryJSON);
//
//		String countryName = (String) countryInputData.get("countryName");
//
//		// Country name validation.
//		if (countryRepository.getCountryWithName(countryName) != null) {
//			throw new MyFileNotFoundException("Country already registered!!!!");
//		}
//
//		countryData.setCountryName(countryName);
//		countryData.setAliasCountryName(countryName.toUpperCase());
//		countryData.setEnableDisableStatus(true);
//
//		// languages.
//		JSONArray languages = (JSONArray) countryInputData.get("languages");
//		for (int i = 0; i < languages.length(); i++) {
//			Language languageData = new Language();
//			String languageName = (String) languages.get(i);
//			languageData.setLanguageName(languageName);
//			countryData.getLanguages().add(languageData);
//		}
//
//		// regions.
//		JSONArray regions = (JSONArray) countryInputData.get("regions");
//		for (int i = 0; i < regions.length(); i++) {
//			Region regionData = new Region();
//			String regionName = (String) regions.get(i);
//			regionData.setRegionName(regionName);
//			countryData.getRegions().add(regionData);
//		}
//
//		// cities.
//		JSONArray cities = (JSONArray) countryInputData.get("cities");
//		for (int i = 0; i < cities.length(); i++) {
//
//			JSONObject city = (JSONObject) cities.get(i);
//			// cityName
//			String cityName = (String) city.get("cityName");
//			// cityLattitude
//			String cityLattitude = (String) city.get("cityLattitude");
//			// cityLongitude
//			String cityLongitude = (String) city.get("cityLongitude");
//
//			City cityData = new City();
//			cityData.setCityName(cityName);
//			cityData.setCityLattitude(cityLattitude);
//			cityData.setCityLongitude(cityLongitude);
//			Region regionData = countryData.getRegions();
//			countryData.getCities().add(cityData);
//		}
//
//		countryRepository.save(countryData);
//		return new AppResponse("200", "Country added successfully.");
//	}

	public List<Country> getAllCountries() {
		return countryRepository.findAll();
	}

	public Country getCountryWithID(Long id) {
		return countryRepository.getCountryWithID(id);
	}

	public Language getLanguageWithID(Long id) {
		return languageRepository.getLanguageWithID(id);
	}

	public City getCityWithID(Long id) {
		return cityRepository.getCityWithID(id);
	}

	public Region getRegionWithID(Long id) {
		return regionRepository.getRegionWithID(id);
	}

//	public AppResponse modifyCountry(String countryJSON, Long id) throws JSONException {
//
//		// Input data.
//		JSONObject countryInputData = new JSONObject(countryJSON);
//
//		// Checking for existing country with id.
//		Country existingCountry = countryRepository.getCountryWithID(id);
//		if (existingCountry == null) {
//			return new AppResponse("404-NOT FOUND", "Country with ID : " + id + " not found in DataBase.");
//		}
//
//		// Checking for duplicateName.
//		String nameInDB = existingCountry.getCountryName();
//		String givenName = (String) countryInputData.get("countryName");
//		Country tempData = countryRepository.getCountryWithName(givenName);
//		if (tempData != null) {
//			if (tempData.getCountryName().equalsIgnoreCase(nameInDB)) {
//				if (tempData.getId() != id) {
//					return new AppResponse("406-NOT ACCEPTABLE", "Country has been added already : " + givenName);
//				}
//			}
//		}
//
//		// Modified object
//		Country modifiedCountry = new Country();
//		modifiedCountry.setId(id);
//
//		// name
//		if (givenName != null) {
//			modifiedCountry.setCountryName(givenName);
//		} else {
//			modifiedCountry.setCountryName(nameInDB);
//		}
//
//		// alias name
//		if (givenName != null) {
//			modifiedCountry.setAliasCountryName(givenName.toUpperCase());
//		} else {
//			modifiedCountry.setCountryName(existingCountry.getAliasCountryName());
//		}
//
//		// status
//		modifiedCountry.setEnableDisableStatus(existingCountry.getEnableDisableStatus());
//
//		// languages
//		if ((JSONArray) countryInputData.get("languages") != null) {
//			JSONArray languages = (JSONArray) countryInputData.get("languages");
//			for (int i = 0; i < languages.length(); i++) {
//				Language languageData = new Language();
//				String languageName = (String) languages.get(i);
//				languageData.setLanguageName(languageName);
//				modifiedCountry.getLanguages().add(languageData);
//			}
//		} else {
//			List<Language> languagesFromDB = existingCountry.getLanguages();
//			for (Language languageData : languagesFromDB) {
//				modifiedCountry.getLanguages().add(languageData);
//			}
//
//		}
//
//		// regions
//		if ((JSONArray) countryInputData.get("regions") != null) {
//			JSONArray regions = (JSONArray) countryInputData.get("regions");
//			for (int i = 0; i < regions.length(); i++) {
//				Region regionData = new Region();
//				String regionName = (String) regions.get(i);
//				regionData.setRegionName(regionName);
//				modifiedCountry.getRegions().add(regionData);
//			}
//		} else {
//			List<Region> regionsFromDB = existingCountry.getRegions();
//			for (Region regionData : regionsFromDB) {
//				modifiedCountry.getRegions().add(regionData);
//			}
//
//		}
//
//		// cities.
//		if ((JSONArray) countryInputData.get("cities") != null) {
//
//			JSONArray cities = (JSONArray) countryInputData.get("cities");
//			for (int i = 0; i < cities.length(); i++) {
//
//				JSONObject city = (JSONObject) cities.get(i);
//				// cityName
//				String cityName = (String) city.get("cityName");
//				// cityLattitude
//				String cityLattitude = (String) city.get("cityLattitude");
//				// cityLongitude
//				String cityLongitude = (String) city.get("cityLongitude");
//
//				City cityData = new City();
//				cityData.setCityName(cityName);
//				cityData.setCityLattitude(cityLattitude);
//				cityData.setCityLongitude(cityLongitude);
//				modifiedCountry.getCities().add(cityData);
//			}
//		} else {
//			List<City> citiesFromDB = existingCountry.getCities();
//			for (City city : citiesFromDB) {
//
//				City cityData = new City();
//				cityData.setCityName(city.getCityName());
//				cityData.setCityLattitude(city.getCityLattitude());
//				cityData.setCityLongitude(city.getCityLongitude());
//
//				modifiedCountry.getCities().add(cityData);
//			}
//		}
//
//		countryRepository.save(modifiedCountry);
//		return new AppResponse("200", "Country with ID : " + id + " modified.");
//	}

	public void modifyCountryStatus(Boolean inputStatus, Long id) {

		// Data from Data Base
		Country countryDataFromDB = countryRepository.getCountryWithID(id);

		// modified data
		Country modifiedCountry = new Country();
		modifiedCountry.setId(id);
		modifiedCountry.setCountryName(countryDataFromDB.getCountryName());
		modifiedCountry.setAliasCountryName(countryDataFromDB.getAliasCountryName());
		modifiedCountry.setEnableDisableStatus(inputStatus);
		modifiedCountry.setLanguages(countryDataFromDB.getLanguages());
		modifiedCountry.setRegions(countryDataFromDB.getRegions());
//		modifiedCountry.setCities(countryDataFromDB.getCities());

		// update data base
		countryRepository.save(modifiedCountry);

	}
}
