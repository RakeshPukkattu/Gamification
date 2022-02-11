package com.zisbv.gamification.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zisbv.gamification.dto.AdminAssignedComponentsDto;
import com.zisbv.gamification.entities.CompanyAssignedComponents;
import com.zisbv.gamification.repositories.AdminAssignedComponentsRepository;
import com.zisbv.gamification.service.AdminAssignedComponentsService;
import com.zisbv.gamification.service.AdminComponentsService;
import com.zisbv.gamification.service.GameDataService;
import com.zisbv.gamification.service.ThemeDataService;

@Service(value = "adminAssignedComponentsService")
public class AdminAssignedComponentsServiceImpl implements AdminAssignedComponentsService {

	@Autowired
	AdminAssignedComponentsRepository adminAssignedComponentsRepository;

	@Autowired
	ThemeDataService themeDataService;

	@Autowired
	AdminComponentsService adminComponentsService;

	@Autowired
	GameDataService gameDataService;

	@Override
	public void assignComponents(AdminAssignedComponentsDto assignedComponents, Long id) {
		CompanyAssignedComponents data = assignedComponents.dataFromPojo();
		data.setCompanyID(id);

		// Checking for existing company.
		CompanyAssignedComponents existingAssignedComponents = findWithCompanyID(id);
		if (existingAssignedComponents == null) {
			adminAssignedComponentsRepository.save(data);
		}

	}

	@Override
	public List<CompanyAssignedComponents> findAll() {
		List<CompanyAssignedComponents> list = new ArrayList<>();
		adminAssignedComponentsRepository.findAll().iterator().forEachRemaining(list::add);
		return list;
	}

	@Override
	public CompanyAssignedComponents findWithid(Long id) {
		CompanyAssignedComponents dataFromDB = adminAssignedComponentsRepository.getAdminAssignedComponentsWithID(id);
		return dataFromDB;
	}

	@Override
	public CompanyAssignedComponents findWithCompanyID(Long companyID) {
		CompanyAssignedComponents dataFromDB = adminAssignedComponentsRepository
				.getAdminAssignedComponentsWithCompanyID(companyID);
		return dataFromDB;
	}

	@Override
	public void updateassignedComponents(CompanyAssignedComponents adminAssignedComponents) {
		adminAssignedComponentsRepository.save(adminAssignedComponents);

	}

}
