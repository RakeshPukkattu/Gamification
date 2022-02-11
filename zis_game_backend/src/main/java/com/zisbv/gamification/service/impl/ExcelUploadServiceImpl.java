package com.zisbv.gamification.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.zisbv.gamification.config.AsynchronousMailSender;
import com.zisbv.gamification.dto.AdminBulkUserUploadDto;
import com.zisbv.gamification.dto.SuperAdminBulkUserUploadDto;
import com.zisbv.gamification.entities.City;
import com.zisbv.gamification.entities.Country;
import com.zisbv.gamification.entities.Region;
import com.zisbv.gamification.entities.State;
import com.zisbv.gamification.entities.UserData;
import com.zisbv.gamification.handlers.ExcelUploadServiceHandler;
import com.zisbv.gamification.models.GamificationMailContents;
import com.zisbv.gamification.repositories.CityRepository;
import com.zisbv.gamification.repositories.CountryRepository;
import com.zisbv.gamification.repositories.Domains;
import com.zisbv.gamification.repositories.RegionRepository;
import com.zisbv.gamification.repositories.StateRepository;
import com.zisbv.gamification.repositories.UserRepository;
import com.zisbv.gamification.service.ExcelUploadService;

import net.bytebuddy.utility.RandomString;

@Service
public class ExcelUploadServiceImpl implements ExcelUploadService {

	@Value("${dev.url}")
	String devURl;

	@Autowired
	UserRepository userRepository;

	@Autowired
	CountryRepository countryRepository;

	@Autowired
	RegionRepository regionRepository;

	@Autowired
	StateRepository stateRepository;

	@Autowired
	CityRepository cityRepository;

	@Autowired
	private AsynchronousMailSender asynchronousWorker;

	private final ExcelUploadServiceHandler excelUploadServiceHandler;

	public ExcelUploadServiceImpl(ExcelUploadServiceHandler excelUploadServiceHandler) {
		this.excelUploadServiceHandler = excelUploadServiceHandler;
	}

	@Override
	public List<Map<String, String>> uploadBulkUsersForAdmin(MultipartFile file) throws Exception {

		List<Map<String, String>> responseList = new ArrayList<>();

		try {
			Path tempDir = Files.createTempDirectory("");
			File tempFile = tempDir.resolve(file.getOriginalFilename()).toFile();
			file.transferTo(tempFile);
			Workbook workbook = WorkbookFactory.create(tempFile);
			Sheet sheet;
			if (workbook.getSheetAt(0) != null) {
				sheet = workbook.getSheetAt(0);
			} else {
				sheet = workbook.getSheetAt(1);
			}

			int rowCount = sheet.getPhysicalNumberOfRows();

			// getting header cells.
			Row headerRow = sheet.getRow(0);
			List<String> headerCells = excelUploadServiceHandler.getStream(headerRow).map(Cell::getStringCellValue)
					.collect(Collectors.toList());

			// getting cells.
			for (int i = 1; i < rowCount; i++) {
				Row row = sheet.getRow(i);
				List<Cell> listCell = new ArrayList<Cell>();
				List<String> cellList = new ArrayList<String>();
				for (int j = 0; j <= 8; j++) {
					if (row.getCell(j) == null) {
						listCell.add(null);
					} else {
						final Cell cell = row.getCell(j);
						listCell.add(cell);
					}
				}
				for (Cell c : listCell) {
					try {
						final String cell = c.getStringCellValue();
						cellList.add(cell);

					} catch (Exception e) {
						String s = null;
						cellList.add(s);
					}
				}
				Map<String, String> map = new HashMap<>();
				for (int n = 0; n <= 8; n++) {
					if (cellList.get(n) == null) {
						map.put(headerCells.get(n), "");
					} else {
						map.put(headerCells.get(n), cellList.get(n));
					}
				}
				responseList.add(map);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseList;

	}

	@Override
	public List<Map<String, String>> uploadBulkUsersForSuperAdmin(MultipartFile file) throws Exception {

		List<Map<String, String>> responseList = new ArrayList<>();

		try {
			Path tempDir = Files.createTempDirectory("");
			File tempFile = tempDir.resolve(file.getOriginalFilename()).toFile();
			file.transferTo(tempFile);
			Workbook workbook = WorkbookFactory.create(tempFile);
			Sheet sheet = workbook.getSheetAt(0);
			int rowCount = sheet.getPhysicalNumberOfRows();

			// getting header cells.
			Row headerRow = sheet.getRow(0);
			List<String> headerCells = excelUploadServiceHandler.getStream(headerRow).map(Cell::getStringCellValue)
					.collect(Collectors.toList());

			// getting cells.
			for (int i = 1; i < rowCount; i++) {
				Row row = sheet.getRow(i);
				List<Cell> listCell = new ArrayList<Cell>();
				List<String> cellList = new ArrayList<String>();
				for (int j = 0; j <= 9; j++) {
					if (row.getCell(j) == null) {
						listCell.add(null);
					} else {
						final Cell cell = row.getCell(j);
						listCell.add(cell);
					}
				}
				for (Cell c : listCell) {
					try {
						final String cell = c.getStringCellValue();
						cellList.add(cell);

					} catch (Exception e) {
						String s = null;
						cellList.add(s);
					}
				}
				Map<String, String> map = new HashMap<>();
				for (int n = 0; n <= 9; n++) {
					if (cellList.get(n) == null) {
						map.put(headerCells.get(n), "");
					} else {
						map.put(headerCells.get(n), cellList.get(n));
					}
				}
				responseList.add(map);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseList;

	}

	@Override
	public AdminBulkUserUploadDto validateUser(AdminBulkUserUploadDto data, int emailOccurance) {

		// validate name.
		Pattern patternName = getPatternName();
		validateFirstName(data, patternName);
		validateMiddleName(data, patternName);
		validateLastName(data, patternName);

		// validate email.
		Pattern patternMail = getPatternMail();
		validateEMail(data, patternMail, emailOccurance);

		// validate roles.
		validateRoles(data);

		// validate country
		validateCountry(data);

		// validate region
		validateRegion(data);

		// validate state
		validateState(data);

		// validate city
		validateCity(data);

		return data;
	}

	@Override
	public SuperAdminBulkUserUploadDto validateUser(SuperAdminBulkUserUploadDto data, int emailOccurance) {
		// validate name.
		Pattern patternName = getPatternName();
		validateFirstName(data, patternName);
		validateMiddleName(data, patternName);
		validateLastName(data, patternName);

		// validate email.
		Pattern patternMail = getPatternMail();
		validateEMail(data, patternMail, emailOccurance);

		// validate roles.
		validateRoles(data);

		// validate country
		validateCountry(data);

		// validate region
		validateRegion(data);

		// validate state
		validateState(data);

		// validate city
		validateCity(data);

		return data;
	}

	@Override
	public void saveData(AdminBulkUserUploadDto data) {

		// saving to db
		String name;
		if (data.getUserMiddleName().equalsIgnoreCase("")) {
			name = data.getUserFirstName() + " " + data.getUserLastName();
		} else {
			name = data.getUserFirstName() + " " + data.getUserMiddleName() + " " + data.getUserLastName();
		}

		UserData userToDb = new UserData();
		userToDb.setUserName(name);
		userToDb.setEmail(data.getUserEmail());
		userToDb.setRoles(data.getUserRole());
		Country countryData = countryRepository.getCountryWithName(data.getCountry());
		userToDb.setCountryID(countryData.getId());
		Region regionData = regionRepository.getRegionWithName(data.getRegion());
		userToDb.setRegionID(regionData.getId());
		State stateData = stateRepository.getStateWithName(data.getState());
		userToDb.setStateID(stateData.getId());
		City cityData = cityRepository.getCityWithName(data.getCity());
		userToDb.setCityID(cityData.getId());
		userToDb.setUserStatus(false);
		userToDb.setUserInAnyGroup(false);
		userToDb.setUserInAnySession(false);

		String randomCode = RandomString.make(64);
		userToDb.setVerificationCode(randomCode);

		userRepository.save(userToDb);

		try {
			sendVerificationEmail(userToDb, devURl);
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void saveData(SuperAdminBulkUserUploadDto data) {
		// saving to db
		String name;
		if (data.getUserMiddleName().equalsIgnoreCase("")) {
			name = data.getUserFirstName() + " " + data.getUserLastName();
		} else {
			name = data.getUserFirstName() + " " + data.getUserMiddleName() + " " + data.getUserLastName();
		}

		// String[] roles = new String[3];
		String role1;
		String role2;
		String rolesString = "";

		if (data.getUserRole1() != "") {
			role1 = data.getUserRole1();
			rolesString = rolesString + role1;
		}
		if (data.getUserRole2() != "") {
			role2 = data.getUserRole2();
			if (rolesString != "") {
				rolesString = rolesString + "," + role2;
			} else {
				rolesString = rolesString + role2;
			}
		}

		UserData userToDb = new UserData();
		userToDb.setUserName(name);
		userToDb.setEmail(data.getUserEmail());
		userToDb.setRoles(rolesString);
		Country countryData = countryRepository.getCountryWithName(data.getCountry());
		userToDb.setCountryID(countryData.getId());
		Region regionData = regionRepository.getRegionWithName(data.getRegion());
		userToDb.setRegionID(regionData.getId());
		State stateData = stateRepository.getStateWithName(data.getState());
		userToDb.setStateID(stateData.getId());
		City cityData = cityRepository.getCityWithName(data.getCity());
		userToDb.setCityID(cityData.getId());
		userToDb.setUserStatus(false);
		userToDb.setUserInAnyGroup(false);
		userToDb.setUserInAnySession(false);

		String randomCode = RandomString.make(64);
		userToDb.setVerificationCode(randomCode);

		userRepository.save(userToDb);

		try {
			sendVerificationEmail(userToDb, devURl);
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
		}

	}

	@Override
	public ByteArrayInputStream adminErrorLogToExcelFile(List<AdminBulkUserUploadDto> exelUsers) {

		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("ErrorLog");

			Row row = sheet.createRow(0);
			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
			headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			// Creating header
			Cell cell = row.createCell(0);
			cell.setCellValue("UserEmail");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(1);
			cell.setCellValue("UserFirstName");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(2);
			cell.setCellValue("UserMiddleName");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(3);
			cell.setCellValue("UserLastName");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(4);
			cell.setCellValue("UserRole");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(5);
			cell.setCellValue("Country");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(6);
			cell.setCellValue("Region");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(7);
			cell.setCellValue("State");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(8);
			cell.setCellValue("City");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(9);
			cell.setCellValue("ErrorLog");
			cell.setCellStyle(headerCellStyle);

			// Creating data rows for each customer
			int s = exelUsers.size();
			for (int i = 0; i < s; i++) {
				Row dataRow = sheet.createRow(i + 1);
				dataRow.createCell(0).setCellValue(exelUsers.get(i).getUserEmail());
				dataRow.createCell(1).setCellValue(exelUsers.get(i).getUserFirstName());
				dataRow.createCell(2).setCellValue(exelUsers.get(i).getUserMiddleName());
				dataRow.createCell(3).setCellValue(exelUsers.get(i).getUserLastName());
				dataRow.createCell(4).setCellValue(exelUsers.get(i).getUserRole());
				dataRow.createCell(5).setCellValue(exelUsers.get(i).getCountry());
				dataRow.createCell(6).setCellValue(exelUsers.get(i).getRegion());
				dataRow.createCell(7).setCellValue(exelUsers.get(i).getState());
				dataRow.createCell(8).setCellValue(exelUsers.get(i).getCity());
				dataRow.createCell(9).setCellValue(exelUsers.get(i).getErrorLog());
			}

			// Making size of column auto resize to fit with data
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(6);
			sheet.autoSizeColumn(7);
			sheet.autoSizeColumn(8);
			sheet.autoSizeColumn(9);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			return new ByteArrayInputStream(outputStream.toByteArray());
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}

	}

	@Override
	public ByteArrayInputStream superAdminErrorLogToExelFile(List<SuperAdminBulkUserUploadDto> exelUsers) {

		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("ErrorLog");

			Row row = sheet.createRow(0);
			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
			headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			// Creating header
			Cell cell = row.createCell(0);
			cell.setCellValue("UserEmail");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(1);
			cell.setCellValue("UserFirstName");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(2);
			cell.setCellValue("UserMiddleName");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(3);
			cell.setCellValue("UserLastName");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(4);
			cell.setCellValue("UserRole1");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(5);
			cell.setCellValue("UserRole2");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(6);
			cell.setCellValue("Country");
			cell.setCellStyle(headerCellStyle);
			
			cell = row.createCell(7);
			cell.setCellValue("Region");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(8);
			cell.setCellValue("State");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(9);
			cell.setCellValue("City");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(10);
			cell.setCellValue("ErrorLog");
			cell.setCellStyle(headerCellStyle);

			// Creating data rows for each customer
			int s = exelUsers.size();
			for (int i = 0; i < s; i++) {
				Row dataRow = sheet.createRow(i + 1);
				dataRow.createCell(0).setCellValue(exelUsers.get(i).getUserEmail());
				dataRow.createCell(1).setCellValue(exelUsers.get(i).getUserFirstName());
				dataRow.createCell(2).setCellValue(exelUsers.get(i).getUserMiddleName());
				dataRow.createCell(3).setCellValue(exelUsers.get(i).getUserLastName());
				dataRow.createCell(4).setCellValue(exelUsers.get(i).getUserRole1());
				dataRow.createCell(5).setCellValue(exelUsers.get(i).getUserRole2());
				dataRow.createCell(6).setCellValue(exelUsers.get(i).getCountry());
				dataRow.createCell(7).setCellValue(exelUsers.get(i).getRegion());
				dataRow.createCell(8).setCellValue(exelUsers.get(i).getState());
				dataRow.createCell(9).setCellValue(exelUsers.get(i).getCity());
				dataRow.createCell(10).setCellValue(exelUsers.get(i).getErrorLog());
			}

			// Making size of column auto resize to fit with data
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(6);
			sheet.autoSizeColumn(7);
			sheet.autoSizeColumn(8);
			sheet.autoSizeColumn(9);
			sheet.autoSizeColumn(10);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			return new ByteArrayInputStream(outputStream.toByteArray());
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}

	}

	// PRIVATE METHODS.

	private void validateCountry(AdminBulkUserUploadDto data) {
		if (data.getCountry() == "" || data.getCountry().isEmpty()) {
			String error = data.getErrorLog();
			if (error == null) {
				error = "";
			}
			data.setErrorLog(error + "::Invalid Country.");
			data.setErrorFound(true);
		} else {
			Country countryData = countryRepository.getCountryWithName(data.getCountry());
			if (countryData == null) {
				String error = data.getErrorLog();
				if (error == null) {
					error = "";
				}
				data.setErrorLog(error + "::Country name doesn't match with DB countries.");
				data.setErrorFound(true);
			}

		}
	}

	private void validateRegion(AdminBulkUserUploadDto data) {

		if (data.getRegion() == null || data.getRegion() == "" || data.getRegion().isEmpty()) {
			String error = data.getErrorLog();
			if (error == null) {
				error = "";
			}
			data.setErrorLog(error + "::Invalid Region.");
			data.setErrorFound(true);
		} else {
			Country countryData = countryRepository.getCountryWithName(data.getCountry());
			if(countryData != null) {
				List<Region> regionData = countryData.getRegions();
				boolean matchFound = false;
				for (Region regionName : regionData) {
					if (data.getRegion().equalsIgnoreCase(regionName.getRegionName())) {
						matchFound = true;
					}
				}
				if (matchFound == false) {
					String error = data.getErrorLog();
					if (error == null) {
						error = "";
					}
					data.setErrorLog(error + "::Region not found in " + data.getCountry() + ".");
					data.setErrorFound(true);
				}
			}
		}
	}

	private void validateState(AdminBulkUserUploadDto data) {
		if (data.getState() == null || data.getState() == "" || data.getState().isEmpty()) {
			String error = data.getErrorLog();
			if (error == null) {
				error = "";
			}
			data.setErrorLog(error + "::Invalid State.");
			data.setErrorFound(true);
		} else {
			Region regionData = regionRepository.getRegionWithName(data.getRegion());
			if(regionData != null) {
				List<State> stateData = regionData.getStates();
				boolean matchFound = false;
				for (State stateName : stateData) {
					if (data.getState().equalsIgnoreCase(stateName.getStateName())) {
						matchFound = true;
					}
				}
				if (matchFound == false) {
					String error = data.getErrorLog();
					if (error == null) {
						error = "";
					}
					data.setErrorLog(error + "::State not found in " + data.getRegion() + ".");
					data.setErrorFound(true);
				}
			}
		}
	}

	private void validateCity(AdminBulkUserUploadDto data) {
		if (data.getCity() == null || data.getCity() == "" || data.getCity().isEmpty()) {
			String error = data.getErrorLog();
			if (error == null) {
				error = "";
			}
			data.setErrorLog(error + "::Invalid City.");
			data.setErrorFound(true);
		} else {
			State stateData = stateRepository.getStateWithName(data.getState());
			if(stateData != null) {
				List<City> cityData = stateData.getCities();
				boolean matchFound = false;
				for (City cityName : cityData) {
					if (data.getCity().equalsIgnoreCase(cityName.getCityName())) {
						matchFound = true;
					}
				}
				if (matchFound == false) {
					String error = data.getErrorLog();
					if (error == null) {
						error = "";
					}
					data.setErrorLog(error + "::City not found in " + data.getCity() + ".");
					data.setErrorFound(true);
				}
			}
		}
	}

	private void validateCountry(SuperAdminBulkUserUploadDto data) {
		if (data.getCountry() == "" || data.getCountry().isEmpty()) {
			String error = data.getErrorLog();
			if (error == null) {
				error = "";
			}
			data.setErrorLog(error + "::Invalid Country.");
			data.setErrorFound(true);
		} else {
			Country countryData = countryRepository.getCountryWithName(data.getCountry());
			if (countryData == null) {
				String error = data.getErrorLog();
				if (error == null) {
					error = "";
				}
				data.setErrorLog(error + "::Country name doesn't match with DB countries.");
				data.setErrorFound(true);
			}

		}
	}

	private void validateRegion(SuperAdminBulkUserUploadDto data) {

		if (data.getRegion() == null || data.getRegion() == "" || data.getRegion().isEmpty()) {
			String error = data.getErrorLog();
			if (error == null) {
				error = "";
			}
			data.setErrorLog(error + "::Invalid Region.");
			data.setErrorFound(true);
		} else {
			Country countryData = countryRepository.getCountryWithName(data.getCountry());
			if(countryData != null) {
				List<Region> regionData = countryData.getRegions();
				boolean matchFound = false;
				for (Region regionName : regionData) {
					if (data.getRegion().equalsIgnoreCase(regionName.getRegionName())) {
						matchFound = true;
					}
				}
				if (matchFound == false) {
					String error = data.getErrorLog();
					if (error == null) {
						error = "";
					}
					data.setErrorLog(error + "::Region not found in " + data.getCountry() + ".");
					data.setErrorFound(true);
				}
			}
		}
	}

	private void validateState(SuperAdminBulkUserUploadDto data) {
		if (data.getState() == null || data.getState() == "" || data.getState().isEmpty()) {
			String error = data.getErrorLog();
			if (error == null) {
				error = "";
			}
			data.setErrorLog(error + "::Invalid State.");
			data.setErrorFound(true);
		} else {
			Region regionData = regionRepository.getRegionWithName(data.getRegion());
			if(regionData != null) {
				List<State> stateData = regionData.getStates();
				boolean matchFound = false;
				for (State stateName : stateData) {
					if (data.getState().equalsIgnoreCase(stateName.getStateName())) {
						matchFound = true;
					}
				}
				if (matchFound == false) {
					String error = data.getErrorLog();
					if (error == null) {
						error = "";
					}
					data.setErrorLog(error + "::State not found in " + data.getRegion() + ".");
					data.setErrorFound(true);
				}
			}
		}
	}

	private void validateCity(SuperAdminBulkUserUploadDto data) {
		if (data.getCity() == null || data.getCity() == "" || data.getCity().isEmpty()) {
			String error = data.getErrorLog();
			if (error == null) {
				error = "";
			}
			data.setErrorLog(error + "::Invalid City.");
			data.setErrorFound(true);
		} else {
			State stateData = stateRepository.getStateWithName(data.getState());
			if(stateData != null) {
				List<City> cityData = stateData.getCities();
				boolean matchFound = false;
				for (City cityName : cityData) {
					if (data.getCity().equalsIgnoreCase(cityName.getCityName())) {
						matchFound = true;
					}
				}
				if (matchFound == false) {
					String error = data.getErrorLog();
					if (error == null) {
						error = "";
					}
					data.setErrorLog(error + "::City not found in " + data.getCity() + ".");
					data.setErrorFound(true);
				}
			}
		}
	}

	private void validateRoles(AdminBulkUserUploadDto data) {
		String role = data.getUserRole();

		// Must have any role.
		// can't be null
		if (role == "" || role.equals("")) {
			String error = data.getErrorLog();
			if (error == null) {
				error = "";
			}
			data.setErrorLog(error + "::Must have any role.");
			data.setErrorFound(true);
		}

		// role must be learner
		if (!role.equals("LEARNER")) {
			String error = data.getErrorLog();
			if (error == null) {
				error = "";
			}
			data.setErrorLog(error + "::Must have any role.");
			data.setErrorFound(true);
		}

	}

	private void validateRoles(SuperAdminBulkUserUploadDto data) {
		String role1 = data.getUserRole1();
		String role2 = data.getUserRole2();

		validateRole1(data, role1, role2);

		if (data.getErrorFound() == false) {
			validateRole2(data, role1, role2);
		}

	}

	private void validateRole1(SuperAdminBulkUserUploadDto data, String role1, String role2) {

		// Must have any role.
		if (role1 == "" || role1.equals("")) {
			if (role2 == "" || role2.equals("")) {
				String error = data.getErrorLog();
				if (error == null) {
					error = "";
				}
				data.setErrorLog(error + "::Must have any role.");
				data.setErrorFound(true);
			}
		}

		// Admin
		if (role1 == "ADMIN" || role1.equalsIgnoreCase("ADMIN")) {
			if (role1.equalsIgnoreCase(role2) || role1 == role2) {
				String error = data.getErrorLog();
				if (error == null) {
					error = "";
				}
				data.setErrorLog(error + "::Duplicate Role ADMIN.");
				data.setErrorFound(true);
			}
		}

		// Learner
		if (role1 == "LEARNER" || role1.equals("LEARNER")) {
			if (role1.equalsIgnoreCase(role2) || role1 == role2) {
				String error = data.getErrorLog();
				if (error == null) {
					error = "";
				}
				data.setErrorLog(error + "::Duplicate Role LEARNER.");
				data.setErrorFound(true);
			}
		}

		if (!role1.equals("")) {
			if (!role1.equals("ADMIN")) {
				if (!role1.equals("LEARNER")) {
					String error = data.getErrorLog();
					if (error == null) {
						error = "";
					}
					data.setErrorLog(error + "::Invalid Roles.");
					data.setErrorFound(true);
				}
			}
		}

	}

	private void validateRole2(SuperAdminBulkUserUploadDto data, String role1, String role2) {

		if (role2.equals("")) {
			if (role1.equals("")) {

				String error = data.getErrorLog();
				if (error == null) {
					error = "";
				}
				data.setErrorLog(error + "::Must have any role.");
				data.setErrorFound(true);
			}
		}

		if (role2.equalsIgnoreCase("ADMIN") || role2 == "ADMIN") {
			if (role2.equalsIgnoreCase(role1) || role2 == role1) {
				String error = data.getErrorLog();
				if (error == null) {
					error = "";
				}
				data.setErrorLog(error + "::Duplicate Role ADMIN.");
				data.setErrorFound(true);
			}
		}
		if (role2.equalsIgnoreCase("LEARNER") || role2 == "LEARNER") {
			if (role2.equalsIgnoreCase(role1) || role2 == role1) {
				String error = data.getErrorLog();
				if (error == null) {
					error = "";
				}
				data.setErrorLog(error + "::Duplicate Role LEARNER.");
				data.setErrorFound(true);
			}
		}
		if (!role2.equals("")) {
			if (!role2.equals("ADMIN")) {
				if (!role2.equals("LEARNER")) {
					String error = data.getErrorLog();
					if (error == null) {
						error = "";
					}
					data.setErrorLog(error + "::Invalid Roles.");
					data.setErrorFound(true);
				}
			}
		}

	}

	private void validateEMail(AdminBulkUserUploadDto data, Pattern patternMail, int emailOccurance) {
		// null- empty check
		if (data.getUserEmail() == "" || data.getUserEmail().isEmpty()) {
			String error = data.getErrorLog();
			if (error == null) {
				error = "";
			}
			data.setErrorLog(error + "::Invalid EmailID.");
			data.setErrorFound(true);
		}
		// pattern checking.
		Matcher matcherEID = patternMail.matcher(data.getUserEmail());
		if (matcherEID.matches() == false) {
			String error = data.getErrorLog();
			if (error == null) {
				error = "";
			}
			data.setErrorLog(error + "::Invalid Email pattern.");
			data.setErrorFound(true);
		}
		// domains.
		String EIDD1 = Domains.GMAIL.name() + ".com";
		Pattern patternEIDD1 = Pattern.compile(EIDD1.toLowerCase());
		Matcher matcherEIDD1 = patternEIDD1.matcher(data.getUserEmail());

		int matches = 0;
		while (matcherEIDD1.find()) {
			matches++;
		}
		// return matches;
		String EIDD2 = Domains.ZISPL.name() + ".com";
		Pattern patternEIDD2 = Pattern.compile(EIDD2.toLowerCase());
		Matcher matcherEIDD2 = patternEIDD2.matcher(data.getUserEmail());

		while (matcherEIDD2.find()) {
			matches++;
		}

		if (matches == 0) {
			String error = data.getErrorLog();
			if (error == null) {
				error = "";
			}
			data.setErrorLog(error + "::Invalid Email domains.");
			data.setErrorFound(true);
		}

		UserData existingUsr = userRepository.findByEmail(data.getUserEmail());
		if (existingUsr != null) {
			String error = data.getErrorLog();
			if (error == null) {
				error = "";
			}
			data.setErrorLog(error + "::Pre-Registered Email");
			data.setErrorFound(true);
		}

		if (emailOccurance > 1) {
			String error = data.getErrorLog();
			if (error == null) {
				error = "";
			}
			data.setErrorLog(error + "::Duplicate Entry." + "occurance:" + emailOccurance);
			data.setErrorFound(true);
		}
	}

	private void validateEMail(SuperAdminBulkUserUploadDto data, Pattern patternMail, int emailOccurance) {
		// null- empty check
		if (data.getUserEmail() == "" || data.getUserEmail().isEmpty()) {
			String error = data.getErrorLog();
			if (error == null) {
				error = "";
			}
			data.setErrorLog(error + "::Invalid EmailID.");
			data.setErrorFound(true);
		}
		// pattern checking.
		Matcher matcherEID = patternMail.matcher(data.getUserEmail());
		if (matcherEID.matches() == false) {
			String error = data.getErrorLog();
			if (error == null) {
				error = "";
			}
			data.setErrorLog(error + "::Invalid Email pattern.");
			data.setErrorFound(true);
		}
		// domains.
		String EIDD1 = Domains.GMAIL.name() + ".com";
		Pattern patternEIDD1 = Pattern.compile(EIDD1.toLowerCase());
		Matcher matcherEIDD1 = patternEIDD1.matcher(data.getUserEmail());

		int matches = 0;
		while (matcherEIDD1.find()) {
			matches++;
		}
		// return matches;
		String EIDD2 = Domains.ZISPL.name() + ".com";
		Pattern patternEIDD2 = Pattern.compile(EIDD2.toLowerCase());
		Matcher matcherEIDD2 = patternEIDD2.matcher(data.getUserEmail());

		while (matcherEIDD2.find()) {
			matches++;
		}

		if (matches == 0) {
			String error = data.getErrorLog();
			if (error == null) {
				error = "";
			}
			data.setErrorLog(error + "::Invalid Email domains.");
			data.setErrorFound(true);
		}

		UserData existingUsr = userRepository.findByEmail(data.getUserEmail());
		if (existingUsr != null) {
			String error = data.getErrorLog();
			if (error == null) {
				error = "";
			}
			data.setErrorLog(error + "::Pre-Registered Email");
			data.setErrorFound(true);
		}

		if (emailOccurance > 1) {
			String error = data.getErrorLog();
			if (error == null) {
				error = "";
			}
			data.setErrorLog(error + "::Duplicate Entry." + "occurance:" + emailOccurance);
			data.setErrorFound(true);
		}
	}

	private void validateLastName(AdminBulkUserUploadDto data, Pattern patternName) {
		// last name
		if (data.getUserLastName() == "" || data.getUserLastName().isEmpty()) {
			String error = data.getErrorLog();
			if (error == null) {
				error = "";
			}
			data.setErrorLog(error + "::Invalid LastName.");
			data.setErrorFound(true);
		}
		Matcher matcherLN = patternName.matcher(data.getUserFirstName());
		if (matcherLN.matches() == false) {
			String error = data.getErrorLog();
			if (error == null) {
				error = "";
			}
			data.setErrorLog(error + "Invalid LastName.");
			data.setErrorFound(true);
		}
	}

	private void validateLastName(SuperAdminBulkUserUploadDto data, Pattern patternName) {
		// last name
		if (data.getUserLastName() == "" || data.getUserLastName().isEmpty()) {
			String error = data.getErrorLog();
			if (error == null) {
				error = "";
			}
			data.setErrorLog(error + "::Invalid LastName.");
			data.setErrorFound(true);
		}
		Matcher matcherLN = patternName.matcher(data.getUserFirstName());
		if (matcherLN.matches() == false) {
			String error = data.getErrorLog();
			if (error == null) {
				error = "";
			}
			data.setErrorLog(error + "Invalid LastName.");
			data.setErrorFound(true);
		}
	}

	private void validateMiddleName(AdminBulkUserUploadDto data, Pattern patternName) {
		if (data.getUserMiddleName() != "" && !data.getUserMiddleName().isEmpty()) {
			Matcher matcherLN = patternName.matcher(data.getUserMiddleName());
			if (matcherLN.matches() == false) {
				String error = data.getErrorLog();
				if (error == null) {
					error = "";
				}
				data.setErrorLog(error + "::Invalid MiddleName.");
				data.setErrorFound(true);
			}
		}
	}

	private void validateMiddleName(SuperAdminBulkUserUploadDto data, Pattern patternName) {
		if (data.getUserMiddleName() != "" && !data.getUserMiddleName().isEmpty()) {
			Matcher matcherLN = patternName.matcher(data.getUserMiddleName());
			if (matcherLN.matches() == false) {
				String error = data.getErrorLog();
				if (error == null) {
					error = "";
				}
				data.setErrorLog(error + "::Invalid MiddleName.");
				data.setErrorFound(true);
			}
		}
	}

	private void validateFirstName(AdminBulkUserUploadDto data, Pattern patternName) {
		// firstName null-empty check.
		if (data.getUserFirstName() == "" || data.getUserFirstName().isEmpty()) {
			String error = data.getErrorLog();
			if (error == null) {
				error = "";
			}
			data.setErrorLog(error + "::Invalid FirstName.");
			data.setErrorFound(true);
		}
		// first name pattern check.
		Matcher matcherFN = patternName.matcher(data.getUserFirstName());
		if (matcherFN.matches() == false) {
			String error = data.getErrorLog();
			if (error == null) {
				error = "";
			}
			data.setErrorLog(error + "::Invalid FirstName.");
			data.setErrorFound(true);
		}
	}

	private void validateFirstName(SuperAdminBulkUserUploadDto data, Pattern patternName) {
		// firstName null-empty check.
		if (data.getUserFirstName() == "" || data.getUserFirstName().isEmpty()) {
			String error = data.getErrorLog();
			if (error == null) {
				error = "";
			}
			data.setErrorLog(error + "::Invalid FirstName.");
			data.setErrorFound(true);
		}
		// first name pattern check.
		Matcher matcherFN = patternName.matcher(data.getUserFirstName());
		if (matcherFN.matches() == false) {
			String error = data.getErrorLog();
			if (error == null) {
				error = "";
			}
			data.setErrorLog(error + "::Invalid FirstName.");
			data.setErrorFound(true);
		}
	}

	private Pattern getPatternName() {
		String namePattern = "^[a-zA-Z0-9]+$";
		Pattern patternName = Pattern.compile(namePattern);
		return patternName;
	}

	private Pattern getPatternMail() {
		// validate mail.
		String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
		Pattern patternEID = Pattern.compile(emailRegex);
		return patternEID;
	}

	private void sendVerificationEmail(UserData user, String siteURL)
			throws MessagingException, UnsupportedEncodingException {

		try {

			String subject = "Please verify your registration :: Gamification";
			String content = "Dear [[name]],<br>"
					+ "<br><br><br>Kindly use this link below for verifing registration with Gamification."
					+ "<h3><a href=\"[[URL]]\" target=\"_self\"><br>Proceed Verification :: Get Gamified</a></h3>"
					+ "Regards,<br>" + "ZIS-GAME";
			content = content.replace("[[name]]", user.getUserName());
			String verifyURL = siteURL + "/userManagement/verify?code=" + user.getVerificationCode();
			content = content.replace("[[URL]]", verifyURL);

			GamificationMailContents mail = new GamificationMailContents(user.getEmail(), subject, content);
			asynchronousWorker.sendMail(mail);

		} catch (InterruptedException e) {
			System.out.println(e);
		}

	}

}
