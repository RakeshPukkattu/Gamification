package com.zisbv.gamification.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.zisbv.gamification.dto.ThemeDataDto;
import com.zisbv.gamification.entities.ThemeData;
import com.zisbv.gamification.models.AppResponse;
import com.zisbv.gamification.models.ThemeDataResponse;
import com.zisbv.gamification.models.ThemeDataResponseList;
import com.zisbv.gamification.repositories.ThemeDataRepository;
import com.zisbv.gamification.util.AppConstants;

@Service("themeDataService")
public class ThemeDataService {

	@Autowired
	ThemeDataRepository themeDataRepository;

	@Autowired
	FileStorageThemeService fileStorageThemeService;

	// GET ALL THEMES FROM DB
	public List<ThemeData> findAll() {

		List<ThemeData> themesListFromDB = new ArrayList<ThemeData>();
		themeDataRepository.findAll().iterator().forEachRemaining(themesListFromDB::add);
		return themesListFromDB;

	}

	// GET THEME FROM DB USING ID
	public ThemeData findWithid(Long id) {

		ThemeData dataFromDB = themeDataRepository.getThemeDataWithID(id);
		return dataFromDB;

	}

	// GET THEME FROM DB USING NAME
	public ThemeData findWithName(String name) {

		ThemeData dataFromDB = themeDataRepository.getThemeDataWithName(name);
		return dataFromDB;

	}

	// GET THEMEDATARESPONSE WITH ID
	public ThemeDataResponse getThemeDataResponse(Long id) {
		ThemeData theme = themeDataRepository.getById(id);

		ThemeDataResponse themeDataResponse = new ThemeDataResponse();
		themeDataResponse.setId(theme.getId());
		themeDataResponse.setThemeName(theme.getThemeName());
		themeDataResponse.setThumbNailKey(theme.getThumbNailKey());
		themeDataResponse.setImageKey(theme.getImageKey());
		themeDataResponse.setZipFileKey(theme.getZipFileKey());
		themeDataResponse.setKeyWords(theme.getKeyWords());
		themeDataResponse.setStatus(theme.getThemeStatus());

		return themeDataResponse;
	}

	// GET ALL THEMES AS LIST
	public ThemeDataResponseList getThemeDataResponseList() {
		List<ThemeData> listFromDB = themeDataRepository.findAll();

		ThemeDataResponseList themeDataResponseList = new ThemeDataResponseList();
		List<ThemeDataResponse> themeDataResponse = new ArrayList<ThemeDataResponse>();

		// iterate listFromDB and add to response
		for (ThemeData theme : listFromDB) {
			ThemeDataResponse response = new ThemeDataResponse();
			response.setId(theme.getId());
			response.setThemeName(theme.getThemeName());
			response.setThumbNailKey(theme.getThumbNailKey());
			response.setImageKey(theme.getImageKey());
			response.setZipFileKey(theme.getZipFileKey());
			response.setKeyWords(theme.getKeyWords());
			response.setStatus(theme.getThemeStatus());
			themeDataResponse.add(response);
		}

		themeDataResponseList.setThemes(themeDataResponse);
		return themeDataResponseList;

	}

	// CREATE NEW THEME.
	public void createTheme(ThemeDataDto themeData, String thumbNailKey, String imageKey, String zipFileKey) {

		ThemeData data = new ThemeData();
		data.setThemeName(themeData.getThemeName());
		data.setThumbNailKey(thumbNailKey);
		data.setImageKey(imageKey);
		data.setZipFileKey(zipFileKey);
		String keywordsString = themeData.getKeywords().stream().map(Object::toString).collect(Collectors.joining(","));
		data.setKeyWords(keywordsString);
		data.setThemeStatus(true);
		data.setLast_modified(new Date());

		themeDataRepository.save(data);

	}

	// MODIFY THEME WITH ID
	public AppResponse modifyTheme(ThemeDataDto modifiedTheme, Long id, MultipartFile thumbNail, MultipartFile image,
			MultipartFile file) throws IOException {

		// Check existing theme or not
		if (themeDataRepository.getById(id) == null) {
			return new AppResponse("400:BAD REQUEST", "Theme not found with id " + id);
		}

		// Getting theme from repository
		ThemeData dataFromDB = themeDataRepository.getById(id);

		// Modified response
		ThemeData modifiedData = new ThemeData();

		// ID
		modifiedData.setId(dataFromDB.getId());

		// NAME
		if (modifiedTheme != null) {

			if (modifiedTheme.getThemeName() != null) {
				// Checks duplicate name
				if (themeDataRepository.getThemeDataWithName(modifiedTheme.getThemeName()) != null) {
					return new AppResponse("400:BAD REQUEST", "Duplicate name not allowed.");
				} else {
					modifiedData.setThemeName(modifiedTheme.getThemeName());
				}
			} else {
				modifiedData.setThemeName(dataFromDB.getThemeName());
			}

		} else {
			modifiedData.setThemeName(dataFromDB.getThemeName());
		}

		// Thumb nail
		if (thumbNail != null) {
			// uploading thumb nail
			String thumbNailName = fileStorageThemeService.storeFile(thumbNail);
			String thumbNailDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
					.path("Themes/downloadFile/").path(thumbNailName).toUriString();
			modifiedData.setThumbNailKey(thumbNailDownloadUri);
		} else {
			modifiedData.setThumbNailKey(dataFromDB.getThumbNailKey());
		}

		// Image
		if (image != null) {
			// uploading image
			String imageName = fileStorageThemeService.storeFile(image);
			String imageDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("Themes/downloadFile/")
					.path(imageName).toUriString();
			modifiedData.setImageKey(imageDownloadUri);
		} else {
			modifiedData.setImageKey(dataFromDB.getImageKey());
		}

		// File
		if (file != null) {
			// uploading file
			String fileName = fileStorageThemeService.storeZipFile(file);
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("Themes/downloadFile/")
					.path(fileName).toUriString();
			modifiedData.setZipFileKey(fileDownloadUri);
		} else {
			modifiedData.setZipFileKey(dataFromDB.getZipFileKey());
		}

		// Keywords
		if (modifiedTheme != null) {
			if (!modifiedTheme.getKeywords().isEmpty()) {
				String keyWordsString = modifiedTheme.getKeywords().stream().map(Object::toString)
						.collect(Collectors.joining(","));
				modifiedData.setKeyWords(keyWordsString);
			} else {
				modifiedData.setKeyWords(dataFromDB.getKeyWords());
			}
		} else {
			modifiedData.setKeyWords(dataFromDB.getKeyWords());
		}

		// Status
		modifiedData.setThemeStatus(dataFromDB.getThemeStatus());
		modifiedData.setLast_modified(new Date());

		// Saving modified data
		themeDataRepository.save(modifiedData);
		return new AppResponse(AppConstants.SUCCESS_CODE, "modified successfully");
	}

	// MODIFY THEME STATUS WITH ID
	public AppResponse modifyThemeStatus(Boolean status, Long id) {

		ThemeData dataFromDB = themeDataRepository.getById(id);
		ThemeData modifiedData = new ThemeData();
		modifiedData.setId(id);
		modifiedData.setThemeName(dataFromDB.getThemeName());
		modifiedData.setThumbNailKey(dataFromDB.getThumbNailKey());
		modifiedData.setImageKey(dataFromDB.getImageKey());
		modifiedData.setZipFileKey(dataFromDB.getZipFileKey());
		modifiedData.setKeyWords(dataFromDB.getKeyWords());
		modifiedData.setThemeStatus(status);
		modifiedData.setLast_modified(new Date());

		themeDataRepository.save(modifiedData);
		return new AppResponse(AppConstants.SUCCESS_CODE, "modified successfully");
	}
}
