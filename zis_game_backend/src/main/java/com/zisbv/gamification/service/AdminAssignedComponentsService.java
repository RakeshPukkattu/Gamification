package com.zisbv.gamification.service;

import java.util.List;

import com.zisbv.gamification.dto.AdminAssignedComponentsDto;
import com.zisbv.gamification.entities.CompanyAssignedComponents;

public interface AdminAssignedComponentsService {

	// assign components to company (Admin only).
	void assignComponents(AdminAssignedComponentsDto assignedComponents, Long id);

	// get all companies and assigned components.
	List<CompanyAssignedComponents> findAll();

	// get components with id.
	CompanyAssignedComponents findWithid(Long id);

	// get assigned components with company id.
	CompanyAssignedComponents findWithCompanyID(Long companyID);

	// update assigned components for company based on company id
	void updateassignedComponents(CompanyAssignedComponents adminAssignedComponents);
}
