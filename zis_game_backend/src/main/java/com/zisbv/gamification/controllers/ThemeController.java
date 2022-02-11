package com.zisbv.gamification.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;

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
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zisbv.gamification.dto.ThemeDataDto;
import com.zisbv.gamification.entities.ThemeData;
import com.zisbv.gamification.models.AppResponse;
import com.zisbv.gamification.models.ThemeDataResponse;
import com.zisbv.gamification.models.ThemeDataResponseList;
import com.zisbv.gamification.service.FileStorageThemeService;
import com.zisbv.gamification.service.ThemeDataService;
import com.zisbv.gamification.util.AppConstants;

@RestController
@RequestMapping("/Themes")
public class ThemeController {

	@Autowired
	ThemeDataService themeDataService;

	@Autowired
	FileStorageThemeService fileStorageThemeService;

	ObjectMapper objectMapper = new ObjectMapper();

	// ALL THEMES
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/allThemes")
	public ThemeDataResponseList getThemes() {
		return themeDataService.getThemeDataResponseList();
	}

	// THEME WITH ID
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/theme/{id}")
	public ThemeDataResponse get(@PathVariable Long id) throws Exception {
		try {
			ThemeDataResponse result = themeDataService.getThemeDataResponse(id);
			if (result == null) {
				throw new FileNotFoundException("Not Found");
			}
			return result;
		} catch (Exception e) {
			throw new FileNotFoundException("Not Found");
		}
	}

	// ADD THEME
	@PreAuthorize("hasAnyRole('ADMIN')")
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/addTheme", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public AppResponse addTheme(@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String themeJson,
			@RequestParam(required = true, value = "thumbNail") MultipartFile thumbNail,
			@RequestParam(required = true, value = "image") MultipartFile image,
			@RequestParam(required = true, value = AppConstants.USER_FILE_PARAM) MultipartFile file)
			throws JsonParseException, JsonMappingException, IOException {

		ThemeDataDto themeDataDto = objectMapper.readValue(themeJson, ThemeDataDto.class);

		// uploading thumb nail
		String thumbNailName = fileStorageThemeService.storeFile(thumbNail);
		String thumbNailDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("Themes/downloadFile/")
				.path(thumbNailName).toUriString();

		// uploading image
		String imageName = fileStorageThemeService.storeFile(image);
		String imageDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("Themes/downloadFile/")
				.path(imageName).toUriString();

		// uploading zip file
		String zipName = fileStorageThemeService.storeZipFile(file);
		String zipDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("Themes/downloadFile/")
				.path(zipName).toUriString();

		ThemeData existingTheme = themeDataService.findWithName(themeDataDto.getThemeName());
		if (existingTheme != null) {
			String Name = existingTheme.getThemeName();
			if (Name != null) {
				return new AppResponse("406", "Theme name : " + Name + " is already used. ");
			}
		}

		themeDataService.createTheme(themeDataDto, thumbNailDownloadUri, imageDownloadUri, zipDownloadUri);
		return new AppResponse(AppConstants.SUCCESS_CODE, "Theme added successfully");
	}

	// MODIFY THEME WITH ID
	@CrossOrigin(origins = "*")
	@PutMapping(value = "/modifyTheme/{id}")
	public AppResponse modifyTheme(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = false) String modifiedJson,
			@RequestParam(required = false, value = "thumbNail") MultipartFile thumbNail,
			@RequestParam(required = false, value = "image") MultipartFile image,
			@RequestParam(required = false, value = AppConstants.USER_FILE_PARAM) MultipartFile file,
			@PathVariable(required = true) Long id) throws JsonParseException, JsonMappingException, IOException {

		ThemeDataDto modifiedTheme = null;
		if(modifiedJson != null) {
			modifiedTheme = objectMapper.readValue(modifiedJson, ThemeDataDto.class);
		} 
		AppResponse response = themeDataService.modifyTheme(modifiedTheme, id, thumbNail, image, file);
		return response;
	}
	
	// MODIFY THEME WITH ID
	@CrossOrigin(origins = "*")
	@PutMapping(value = "/modifyThemeStatus/{id}")
	public AppResponse modifyThemeStatus(@RequestParam(value = "status", required = true) Boolean status,
			@PathVariable(required = true) Long id) throws JsonParseException, JsonMappingException, IOException {

		AppResponse response = themeDataService.modifyThemeStatus(status, id);
		return response;

	}

	// DOWNLOAD FILES
	@CrossOrigin(origins = "*")
	@GetMapping(value = AppConstants.DOWNLOAD_URI)
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {

		Resource resource = fileStorageThemeService.loadFileAsResource(fileName);

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
