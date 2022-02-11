package com.zisbv.gamification.service;

import java.util.List;

import com.zisbv.gamification.dto.AdminComponentsDto;
import com.zisbv.gamification.entities.AdminComponents;
import com.zisbv.gamification.models.ComponentModel;
import com.zisbv.gamification.models.ComponentsResponseModel;

public interface AdminComponentsService {

	// create component (Admin only).
	void createAdminComponent(AdminComponentsDto adminComponents, String imageKey);

	// get all components.
	List<AdminComponents> findAll();

	// get component with id.
	AdminComponents findWithid(Long id);

	// get component with name.
	AdminComponents findWithTitle(String title);

	// get all components as ComponentsResponseModel.
	ComponentsResponseModel getComponentsResponseModel();

	// get component with id as ComponentModel
	ComponentModel getComponentModel(Long id);

	// update
	void updateComponent(AdminComponents component);

	// delete
}
