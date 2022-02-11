package com.zisbv.gamification.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zisbv.gamification.dto.AdminBulkUserUploadDto;
import com.zisbv.gamification.dto.SuperAdminBulkUserUploadDto;
import com.zisbv.gamification.models.BulkUploadResponse;
import com.zisbv.gamification.service.ExcelUploadService;
import com.zisbv.gamification.service.UserManagementService;
import com.zisbv.gamification.util.AppConstants;

@RestController
@RequestMapping(value = AppConstants.USER_SERVICE_URI)
public class BulkUserController {

	@Autowired
	UserManagementService userManagementService;

	ByteArrayInputStream stream;

	private final ExcelUploadService excelUploadService;

	public BulkUserController(ExcelUploadService excelUploadService) {
		this.excelUploadService = excelUploadService;
	}

	// Admin bulk upload.
	@CrossOrigin(origins = "*")
	@PostMapping("/adminBulkUpload")
	public BulkUploadResponse adminBulkUserUpload(@RequestParam("file") MultipartFile file) throws Exception {

		int errorCounter = 0;
		int successCounter = 0;

		List<Map<String, String>> userExcelData = new ArrayList<Map<String, String>>();
		List<AdminBulkUserUploadDto> errorLog = new ArrayList<AdminBulkUserUploadDto>();

		userExcelData = excelUploadService.uploadBulkUsersForAdmin(file);

		// email list for check
		List<String> emailChecker = new ArrayList<String>();
		for (Map<String, String> user : userExcelData) {
			emailChecker.add((user.get("UserEmail")));
		}

		// Checking repeats
		Map<String, Integer> frequencyMap = new HashMap<>();
		for (String e : emailChecker) {
			Integer count = frequencyMap.get(e);
			if (count == null) {
				count = 0;
			}
			frequencyMap.put(e, count + 1);
		}

		for (Map<String, String> user : userExcelData) {
			// loop through the maps
			AdminBulkUserUploadDto data = new AdminBulkUserUploadDto();
			data.setUserEmail(user.get("UserEmail"));
			data.setUserFirstName(user.get("UserFirstName"));
			data.setUserMiddleName(user.get("UserMiddleName"));
			data.setUserLastName(user.get("UserLastName"));
			data.setUserRole(user.get("UserRole"));
			data.setCountry(user.get("Country"));
			data.setRegion(user.get("Region"));
			data.setState(user.get("State"));
			data.setCity(user.get("City"));
			data.setErrorFound(false);

			int emailOccurance = frequencyMap.get(data.getUserEmail());

			if (data.getUserFirstName() == "" && data.getUserMiddleName() == "" && data.getUserLastName() == ""
					&& data.getUserEmail() == "" && data.getUserRole() == "" && data.getCountry() == ""
					&& data.getRegion() == "" && data.getState() == "" && data.getCity() == "") {
				break;
			}

			// Validation - save user - returns user with error log List
			data = excelUploadService.validateUser(data, emailOccurance);

			if (data.getErrorFound()) {
				errorCounter++;
				errorLog.add(data);
			} else {
				successCounter++;
				excelUploadService.saveData(data);
			}
		}

		if (errorCounter == 0) {
			return new BulkUploadResponse("ok", ":" + successCounter + "", ":" + userExcelData.size() + "", false);
		}
		stream = excelUploadService.adminErrorLogToExcelFile(errorLog);
		return new BulkUploadResponse("ok", ":" + successCounter + "", ":" + userExcelData.size() + "", true);
	}

	// SuperAdmin bulk upload.
	@CrossOrigin(origins = "*")
	@PostMapping("/superAdminBulkUpload")
	public BulkUploadResponse superAdminBulkUserUpload(@RequestParam("file") MultipartFile file) throws Exception {

		int errorCounter = 0;
		int successCounter = 0;

		List<Map<String, String>> userExcelData = new ArrayList<Map<String, String>>();
		List<SuperAdminBulkUserUploadDto> errorLog = new ArrayList<SuperAdminBulkUserUploadDto>();

		userExcelData = excelUploadService.uploadBulkUsersForSuperAdmin(file);

		// email list for check
		List<String> emailChecker = new ArrayList<String>();
		for (Map<String, String> user : userExcelData) {
			emailChecker.add((user.get("UserEmail")));
		}

		// Checking repeats
		Map<String, Integer> frequencyMap = new HashMap<>();
		for (String e : emailChecker) {
			Integer count = frequencyMap.get(e);
			if (count == null) {
				count = 0;
			}
			frequencyMap.put(e, count + 1);
		}

		for (Map<String, String> user : userExcelData) {
			// loop through the maps
			SuperAdminBulkUserUploadDto data = new SuperAdminBulkUserUploadDto();
			data.setUserEmail(user.get("UserEmail"));
			data.setUserFirstName(user.get("UserFirstName"));
			data.setUserMiddleName(user.get("UserMiddleName"));
			data.setUserLastName(user.get("UserLastName"));
			data.setUserRole1(user.get("UserRole1"));
			data.setUserRole2(user.get("UserRole2"));
			data.setCountry(user.get("Country"));
			data.setRegion(user.get("Region"));
			data.setState(user.get("State"));
			data.setCity(user.get("City"));
			data.setErrorFound(false);

			int emailOccurance = frequencyMap.get(data.getUserEmail());

			if (data.getUserFirstName() == "" && data.getUserMiddleName() == "" && data.getUserLastName() == ""
					&& data.getUserEmail() == "" && data.getUserRole1() == "" && data.getUserRole2() == ""
					&& data.getCountry() == "" && data.getRegion() == "" && data.getState() == "" && data.getCity() == "") {
				break;
			}

			// Validation - save user - returns user with error log List
			data = excelUploadService.validateUser(data, emailOccurance);

			if (data.getErrorFound()) {
				errorCounter++;
				errorLog.add(data);
			} else {
				successCounter++;
				excelUploadService.saveData(data);
			}
		}

		if (errorCounter == 0) {
			return new BulkUploadResponse("ok", ":" + successCounter + "", ":" + userExcelData.size() + "", false);
		}
		stream = excelUploadService.superAdminErrorLogToExelFile(errorLog);
		return new BulkUploadResponse("ok", ":" + successCounter + "", ":" + userExcelData.size() + "", true);

	}

	// Error log download
	@GetMapping("/download/errorLog.xlsx")
	public void downloadCsv(HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=errorLog.xlsx");
		IOUtils.copy(stream, response.getOutputStream());
	}

}
