package com.zisbv.gamification.service;

import java.util.List;

import com.zisbv.gamification.dto.CompanyDto;
import com.zisbv.gamification.entities.CompanyData;
import com.zisbv.gamification.models.CompanyModel;
import com.zisbv.gamification.models.CompanyResponseModel;

public interface CompanyManagementService {

	// create company (Admin only).
	void createCompany(CompanyDto company);

	// get all companies.
	List<CompanyData> findAll();

	// get company with id.
	CompanyData findWithid(Long id);

	// get company with name.
	CompanyData findWithName(String name);

	// get all companies as CompanyResponseModel.
	CompanyResponseModel getCompanyResponseModel();

	// get company with id as CompanyModel
	CompanyModel getCompanyModel(Long id);

	// Update status.
	CompanyData updateCompanyStatus(CompanyData company);

	// Update company (Admin only).
	void updateCompany(CompanyData company);

}
