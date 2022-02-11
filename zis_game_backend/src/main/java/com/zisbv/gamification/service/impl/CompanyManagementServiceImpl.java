package com.zisbv.gamification.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zisbv.gamification.dto.CompanyDto;
import com.zisbv.gamification.entities.AdminComponents;
import com.zisbv.gamification.entities.CompanyAssignedComponents;
import com.zisbv.gamification.entities.CompanyData;
import com.zisbv.gamification.entities.IndustryData;
import com.zisbv.gamification.models.AssignedComponentsModel;
import com.zisbv.gamification.models.CompanyModel;
import com.zisbv.gamification.models.CompanyResponseModel;
import com.zisbv.gamification.models.ComponentModel;
import com.zisbv.gamification.models.GameDataResponse;
import com.zisbv.gamification.models.IndustryModel;
import com.zisbv.gamification.models.ThemeDataResponse;
import com.zisbv.gamification.repositories.CompanyRepository;
import com.zisbv.gamification.service.AdminAssignedComponentsService;
import com.zisbv.gamification.service.AdminComponentsService;
import com.zisbv.gamification.service.CompanyManagementService;
import com.zisbv.gamification.service.GameDataService;
import com.zisbv.gamification.service.IndustryService;
import com.zisbv.gamification.service.ThemeDataService;

@Service(value = "companyManagementService")
public class CompanyManagementServiceImpl implements CompanyManagementService {

	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	AdminAssignedComponentsService adminAssignedComponentsService;

	@Autowired
	AdminComponentsService adminComponentsService;

	@Autowired
	ThemeDataService themeDataService;

	@Autowired
	GameDataService gameDataService;
	
	@Autowired
	IndustryService industryService;

	@Override
	public void createCompany(CompanyDto company) {

		CompanyData data = company.dataFromPojo();

		// Checking for existing user.
		CompanyData existingCompany = findWithid(data.getId());
		if (existingCompany == null) {
			companyRepository.save(data);
		}

	}

	@Override
	public List<CompanyData> findAll() {
		List<CompanyData> list = new ArrayList<>();
		companyRepository.findAll().iterator().forEachRemaining(list::add);
		return list;
	}

	@Override
	public CompanyData findWithid(Long id) {
		CompanyData dataFromDB = companyRepository.getCompanyWithID(id);
		return dataFromDB;
	}

	@Override
	public CompanyData findWithName(String name) {
		CompanyData data = companyRepository.getCompanyWithName(name);
		return data;
	}

	@Override
	public CompanyResponseModel getCompanyResponseModel() {
		List<CompanyData> dataFromDb = companyRepository.findAll();

		// Final Response.
		CompanyResponseModel companies = new CompanyResponseModel();
		List<CompanyModel> responseModel = new ArrayList<CompanyModel>();

		// iterating database data and adding in to response model.
		for (CompanyData companyData : dataFromDb) {

			// Response.
			CompanyModel companyModel = new CompanyModel();
			companyModel.setId(companyData.getId());
			companyModel.setCompanyName(companyData.getCompanyName());
			companyModel.setAddress(companyData.getAddress());
			companyModel.setPincode(companyData.getPincode());
			companyModel.setState(companyData.getState());
			companyModel.setCountry(companyData.getCountry());
			companyModel.setPrimaryContact(companyData.getPrimaryContact());
			companyModel.setSecondaryContact(companyData.getSecondaryContact());
			companyModel.setStartDate(companyData.getStartDate());
			companyModel.setEndDate(companyData.getEndDate());
			companyModel.setDuration(companyData.getDuration());
			companyModel.setPrimaryEmail(companyData.getPrimaryEmail());
			companyModel.setSecondaryEmail(companyData.getSecondaryEmail());
			companyModel.setStatus(companyData.getStatus());

			CompanyAssignedComponents adminAssignedComponents = adminAssignedComponentsService
					.findWithCompanyID(companyData.getId());

			if (adminAssignedComponents != null) {
				AssignedComponentsModel assignedComponentsModel = new AssignedComponentsModel();
				
				//industries
				List<IndustryModel> industries = new ArrayList<IndustryModel>();
				String industriesIDString = adminAssignedComponents.getIndustryID();
				String[] industriesIDStringArray = industriesIDString.split(",");
				for (String industryID : industriesIDStringArray) {
					IndustryModel industryModel = new IndustryModel();
					IndustryData industryFromDB = industryService.findWithIndustryId(Long.parseLong(industryID));
					industryModel.setIndustryId(industryFromDB.getId());
					industryModel.setIndustryName(industryFromDB.getIndustryName());
					industryModel.setIndustryStatus(industryFromDB.getStatus());
					industries.add(industryModel);
				}
				assignedComponentsModel.setIndustries(industries);
				
				
				
				// components
				List<ComponentModel> components = new ArrayList<ComponentModel>();
				String componentIDString = adminAssignedComponents.getComponentID();
				String[] componentIDStringArray = componentIDString.split(",");
				for (String componentId : componentIDStringArray) {
					ComponentModel componentModel = new ComponentModel();
					AdminComponents componentFromDB = adminComponentsService.findWithid(Long.parseLong(componentId));
					componentModel.setId(componentFromDB.getId());
					componentModel.setTitle(componentFromDB.getTitle());
					componentModel.setContext(componentFromDB.getContent());
					componentModel.setImageKey(componentFromDB.getImageKey());
					componentModel.setClassName(componentFromDB.getClassName());
					componentModel.setLink(componentFromDB.getLink());
					components.add(componentModel);
				}
				assignedComponentsModel.setComponents(components);

				// theme
				List<ThemeDataResponse> theme = new ArrayList<ThemeDataResponse>();
				Long themeID = adminAssignedComponents.getThemeID();
				ThemeDataResponse themeData = themeDataService.getThemeDataResponse(themeID);
				theme.add(themeData);
				assignedComponentsModel.setTheme(theme);

				// game
				List<GameDataResponse> games = new ArrayList<GameDataResponse>();
				String gameIDString = adminAssignedComponents.getGameID();
				String[] gameIDStringArray = gameIDString.split(",");
				for (String gameID : gameIDStringArray) {
					GameDataResponse gameData = gameDataService.getGameDataResponse(Long.parseLong(gameID));
					games.add(gameData);
				}
				assignedComponentsModel.setGames(games);

				companyModel.setAssignedComponents(assignedComponentsModel);
			}

			responseModel.add(companyModel);

		}

		// final response
		companies.setCompany(responseModel);
		return companies;
	}

	@Override
	public CompanyModel getCompanyModel(Long id) {

		CompanyData companyData = companyRepository.getById(id);

		CompanyModel companyModel = new CompanyModel();
		companyModel.setId(companyData.getId());
		companyModel.setCompanyName(companyData.getCompanyName());
		companyModel.setAddress(companyData.getAddress());
		companyModel.setPincode(companyData.getPincode());
		companyModel.setState(companyData.getState());
		companyModel.setCountry(companyData.getCountry());
		companyModel.setPrimaryContact(companyData.getPrimaryContact());
		companyModel.setSecondaryContact(companyData.getSecondaryContact());
		companyModel.setStartDate(companyData.getStartDate());
		companyModel.setEndDate(companyData.getEndDate());
		companyModel.setDuration(companyData.getDuration());
		companyModel.setPrimaryEmail(companyData.getPrimaryEmail());
		companyModel.setSecondaryEmail(companyData.getSecondaryEmail());
		companyModel.setStatus(companyData.getStatus());

		CompanyAssignedComponents adminAssignedComponents = adminAssignedComponentsService
				.findWithCompanyID(companyData.getId());

		if (adminAssignedComponents != null) {
			AssignedComponentsModel assignedComponentsModel = new AssignedComponentsModel();

			//industries
			List<IndustryModel> industries = new ArrayList<IndustryModel>();
			String industriesIDString = adminAssignedComponents.getIndustryID();
			String[] industriesIDStringArray = industriesIDString.split(",");
			for (String industryID : industriesIDStringArray) {
				IndustryModel industryModel = new IndustryModel();
				IndustryData industryFromDB = industryService.findWithIndustryId(Long.parseLong(industryID));
				industryModel.setIndustryId(industryFromDB.getId());
				industryModel.setIndustryName(industryFromDB.getIndustryName());
				industryModel.setIndustryStatus(industryFromDB.getStatus());
				industries.add(industryModel);
			}
			assignedComponentsModel.setIndustries(industries);
			
			// components
			List<ComponentModel> components = new ArrayList<ComponentModel>();
			String componentIDString = adminAssignedComponents.getComponentID();
			String[] componentIDStringArray = componentIDString.split(",");
			for (String componentId : componentIDStringArray) {
				ComponentModel componentModel = new ComponentModel();
				AdminComponents componentFromDB = adminComponentsService.findWithid(Long.parseLong(componentId));
				componentModel.setId(componentFromDB.getId());
				componentModel.setTitle(componentFromDB.getTitle());
				componentModel.setContext(componentFromDB.getContent());
				componentModel.setImageKey(componentFromDB.getImageKey());
				componentModel.setClassName(componentFromDB.getClassName());
				componentModel.setLink(componentFromDB.getLink());
				components.add(componentModel);
			}
			assignedComponentsModel.setComponents(components);

			// theme
			List<ThemeDataResponse> theme = new ArrayList<ThemeDataResponse>();
			Long themeID = adminAssignedComponents.getThemeID();
			ThemeDataResponse themeData = themeDataService.getThemeDataResponse(themeID);
			theme.add(themeData);
			assignedComponentsModel.setTheme(theme);

			// game
			List<GameDataResponse> games = new ArrayList<GameDataResponse>();
			String gameIDString = adminAssignedComponents.getGameID();
			String[] gameIDStringArray = gameIDString.split(",");
			for (String gameID : gameIDStringArray) {
				GameDataResponse gameData = gameDataService.getGameDataResponse(Long.parseLong(gameID));
				games.add(gameData);
			}
			assignedComponentsModel.setGames(games);

			companyModel.setAssignedComponents(assignedComponentsModel);
		}

		return companyModel;
	}

	@Override
	public CompanyData updateCompanyStatus(CompanyData company) {
		return companyRepository.save(company);
	}

	@Override
	public void updateCompany(CompanyData company) {
		companyRepository.save(company);

	}

}
