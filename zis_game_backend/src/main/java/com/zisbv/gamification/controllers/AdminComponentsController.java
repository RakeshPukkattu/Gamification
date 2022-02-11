package com.zisbv.gamification.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zisbv.gamification.dto.AdminComponentsDto;
import com.zisbv.gamification.entities.AdminComponents;
import com.zisbv.gamification.models.AppResponse;
import com.zisbv.gamification.models.ComponentModel;
import com.zisbv.gamification.models.ComponentsResponseModel;
import com.zisbv.gamification.service.AdminComponentsService;
import com.zisbv.gamification.service.FileStorageThemeService;
import com.zisbv.gamification.util.AppConstants;

@RestController
@RequestMapping("/adminComponentsManagement")
public class AdminComponentsController {

	@Autowired
	AdminComponentsService adminComponentsService;

	@Autowired
	FileStorageThemeService fileStorageService;

	ObjectMapper objectMapper = new ObjectMapper();

	// get all components as list
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/allAdminComponents")
	public ComponentsResponseModel getAllAdminComponents() {
		return adminComponentsService.getComponentsResponseModel();
	}

	// get components with id
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/adminComponent/{id}")
	public ComponentModel get(@PathVariable Long id) throws Exception {
		try {
			ComponentModel result = adminComponentsService.getComponentModel(id);

			if (result == null) {
				throw new FileNotFoundException("Not Found");
			}
			return result;
		} catch (Exception e) {
			throw new FileNotFoundException("Not Found");
		}
	}

	// add admin component
	@PreAuthorize("hasAnyRole('ADMIN')")
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/addAdminComponent", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public AppResponse addAdminComponent(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String adminComponentJson,
			@RequestParam(required = true, value = AppConstants.USER_FILE_PARAM) MultipartFile file)
			throws JsonParseException, JsonMappingException, IOException {

		AdminComponentsDto component = objectMapper.readValue(adminComponentJson, AdminComponentsDto.class);

		// uploading image
		String fileName = fileStorageService.storeFile(file);

		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path(AppConstants.COMPONENT_DOWNLOAD_PATH).path(fileName).toUriString();

		AdminComponents existingComponent = adminComponentsService.findWithTitle(component.getTitle());
		if (existingComponent != null) {
			String Name = existingComponent.getTitle();
			if (Name != null) {
				return new AppResponse("406", "Component name : " + Name + " is already used. ");
			}
		}

		adminComponentsService.createAdminComponent(component, fileDownloadUri);
		return new AppResponse(AppConstants.SUCCESS_CODE, "Component added successfully");
	}

	// update component
	@PreAuthorize("hasAnyRole('ADMIN')")
	@CrossOrigin(origins = "*")
	@PutMapping(value = "/updateComponent/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public AppResponse updateTheme(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = false) String componentJson,
			@RequestParam(required = false, value = AppConstants.USER_FILE_PARAM) MultipartFile file,
			@PathVariable(required = true) Long id) throws JsonMappingException, JsonProcessingException, IOException {

		// Checking for existing component with id.
		AdminComponents existingComponent = adminComponentsService.findWithid(id);
		if (existingComponent == null) {
			return new AppResponse("404-NOT FOUND", "Component not found with : " + id);
		}

		AdminComponentsDto component = objectMapper.readValue(componentJson, AdminComponentsDto.class);
		String newName = component.getTitle(); // new name given
		String existingName = existingComponent.getTitle();// old name given

		// Checking new name is used or not.
		if (adminComponentsService.findWithTitle(newName) != null) {
			Boolean matcher = newName.equalsIgnoreCase(existingName);
			if (!matcher) {
				return new AppResponse("406-NOT ACCEPTABLE", " using pre-registered Component name : " + newName);
			}
		}

		AdminComponents modifiedComponent = new AdminComponents();
		modifiedComponent.setId(id);

		if (component.getTitle() != null) {
			modifiedComponent.setTitle(component.getTitle());
		} else {
			modifiedComponent.setTitle(existingComponent.getTitle());
		}

		if (component.getContent() != null) {
			modifiedComponent.setContent(component.getContent());
		} else {
			modifiedComponent.setContent(existingComponent.getContent());
		}

		if (component.getClassName() != null) {
			modifiedComponent.setClassName(component.getClassName());
		} else {
			modifiedComponent.setClassName(existingComponent.getClassName());
		}

		if (component.getLink() != null) {
			modifiedComponent.setLink(component.getLink());
		} else {
			modifiedComponent.setLink(existingComponent.getLink());
		}


		if (file != null) {

			// uploading image
			String fileName = fileStorageService.storeFile(file);
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
					.path(AppConstants.COMPONENT_DOWNLOAD_PATH).path(fileName).toUriString();

			modifiedComponent.setImageKey(fileDownloadUri);
		} else {
			modifiedComponent.setImageKey(existingComponent.getImageKey());
		}
		
		modifiedComponent.setLast_modified(new Date());

		adminComponentsService.updateComponent(modifiedComponent);
		return new AppResponse("200 - OK", "Game updated.");

	}

	// Download imagefiles.
	@CrossOrigin(origins = "*")
	@GetMapping(value = AppConstants.DOWNLOAD_URI)
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {

		Resource resource = fileStorageService.loadFileAsResource(fileName);

		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		if (contentType == null) {
			contentType = AppConstants.DEFAULT_CONTENT_TYPE;
		}
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION,
						String.format(AppConstants.FILE_DOWNLOAD_HTTP_HEADER, resource.getFilename()))
				.body(resource);
	}
}
