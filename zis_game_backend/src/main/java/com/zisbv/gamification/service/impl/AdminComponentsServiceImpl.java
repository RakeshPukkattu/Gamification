package com.zisbv.gamification.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zisbv.gamification.dto.AdminComponentsDto;
import com.zisbv.gamification.entities.AdminComponents;
import com.zisbv.gamification.models.ComponentModel;
import com.zisbv.gamification.models.ComponentsResponseModel;
import com.zisbv.gamification.repositories.AdminComponentsRepository;
import com.zisbv.gamification.service.AdminComponentsService;

@Service(value = "adminComponentsService")
public class AdminComponentsServiceImpl implements AdminComponentsService {

	@Autowired
	AdminComponentsRepository adminComponentsRepository;

	@Override
	public void createAdminComponent(AdminComponentsDto adminComponents, String imageKey) {

		AdminComponents data = adminComponents.dataFromPojo();
		data.setImageKey(imageKey);

		// Checking for existing user.
		AdminComponents existingComponent = findWithid(data.getId());
		if (existingComponent == null) {
			adminComponentsRepository.save(data);
		}

	}

	@Override
	public List<AdminComponents> findAll() {
		List<AdminComponents> list = new ArrayList<>();
		adminComponentsRepository.findAll().iterator().forEachRemaining(list::add);
		return list;
	}

	@Override
	public AdminComponents findWithid(Long id) {
		AdminComponents dataFromDB = adminComponentsRepository.getAdminComponentsWithID(id);
		return dataFromDB;
	}

	@Override
	public AdminComponents findWithTitle(String title) {
		AdminComponents data = adminComponentsRepository.getAdminComponentsWithTitle(title);
		return data;
	}

	@Override
	public ComponentsResponseModel getComponentsResponseModel() {
		List<AdminComponents> dataFromDb = adminComponentsRepository.findAll();

		// Final Response.
		ComponentsResponseModel components = new ComponentsResponseModel();
		List<ComponentModel> responseModel = new ArrayList<ComponentModel>();

		// iterating database data and adding in to response model.
		for (AdminComponents component : dataFromDb) {

			// Response.
			ComponentModel componentModel = new ComponentModel();
			componentModel.setId(component.getId());
			componentModel.setTitle(component.getTitle());
			componentModel.setContext(component.getContent());
			componentModel.setImageKey(component.getImageKey());
			componentModel.setClassName(component.getClassName());
			componentModel.setLink(component.getLink());

			responseModel.add(componentModel);

		}

		// final response
		components.setComponents(responseModel);
		return components;
	}

	@Override
	public ComponentModel getComponentModel(Long id) {
		AdminComponents componentData = adminComponentsRepository.getById(id);

		ComponentModel componentModel = new ComponentModel();
		componentModel.setId(componentData.getId());
		componentModel.setTitle(componentData.getTitle());
		componentModel.setContext(componentData.getContent());
		componentModel.setImageKey(componentData.getImageKey());
		componentModel.setClassName(componentData.getClassName());
		componentModel.setLink(componentData.getLink());

		return componentModel;
	}

	@Override
	public void updateComponent(AdminComponents component) {
		adminComponentsRepository.save(component);

	}

}
