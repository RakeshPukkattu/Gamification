package com.zisbv.gamification.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zisbv.gamification.dto.SuperAdminDto;
import com.zisbv.gamification.dto.UserDto;
import com.zisbv.gamification.entities.Avathars;
import com.zisbv.gamification.entities.Domains;
import com.zisbv.gamification.entities.UserData;
import com.zisbv.gamification.handlers.UserManagementHandler;
import com.zisbv.gamification.models.AppResponse;
import com.zisbv.gamification.models.UserCredsModel;
import com.zisbv.gamification.models.UserModel;
import com.zisbv.gamification.models.UserResponseModel;
import com.zisbv.gamification.service.AvatharsService;
import com.zisbv.gamification.service.DomainService;
import com.zisbv.gamification.service.FileStorageService;
import com.zisbv.gamification.service.UserManagementService;
import com.zisbv.gamification.util.AppConstants;

@RestController
@RequestMapping(value = AppConstants.USER_SERVICE_URI)
public class UserController {

	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;

	@Autowired
	private DomainService domainService;

	@Autowired
	private AvatharsService avatharsService;

	@Autowired
	private FileStorageService fileStorageService;

	private final UserManagementService userManagementService;

	public UserController(UserManagementService userManagementService) {
		this.userManagementService = userManagementService;
	}

	UserManagementHandler handler = new UserManagementHandler();
	ObjectMapper objectMapper = new ObjectMapper();

	// Register Learner.
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/learnerRegistration")
	public String registerLearner(@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String userJson,
			HttpServletRequest request) throws UnsupportedEncodingException, MessagingException, JsonParseException,
			JsonMappingException, IOException {

		UserData user = objectMapper.readValue(userJson, UserData.class);
		UserData existingUser = userManagementService.findOne(user.getEmail());

		if (existingUser != null) {
			String email = existingUser.getEmail();
			if (email != null) {
				return "\n" + email + " - is not available or maybe a registerd one.";
			}
		}

		userManagementService.register(user, getSiteURL(request));
		return "register_success";
	}

	@CrossOrigin(origins = "*")
	@GetMapping("/verify")
	public ResponseEntity<Void> verifyUser(@Param("code") String code, Model model)
			throws URISyntaxException, IOException {

		boolean verified = userManagementService.verify(code);

		// return (verified
		// ?
		// ResponseEntity.status(HttpStatus.FOUND).location(URI.create("https://zisplgamification.netlify.app/"))
		// .build()
		// : ResponseEntity.status(HttpStatus.BAD_REQUEST)
		// .location(URI.create("https://zisplgamification.netlify.app/")).build());
		return (verified
				? ResponseEntity.status(HttpStatus.FOUND).location(URI.create("http://localhost:3000/")).build()
				: ResponseEntity.status(HttpStatus.BAD_REQUEST).location(URI.create("http://localhost:3000/")).build());

	}

	// Register SuperAdmin.
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/superAdminRegistration")
	public AppResponse registerSuperAdmin(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String userJson)
			throws JsonParseException, JsonMappingException, IOException {

		SuperAdminDto user = objectMapper.readValue(userJson, SuperAdminDto.class);
		userManagementService.registerSuperAdmin(user);
		return new AppResponse(AppConstants.SUCCESS_CODE, " Added new Super Admin successfully.");
	}

	// Get all users.
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/users")
	public UserResponseModel getAllUsers() {
		return userManagementService.getUserResponseModel();
	}

	// Get all users based on country.
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/usersInCountry/{country}")
	public UserResponseModel getAllUsersInCountry(@PathVariable String country) {
		return userManagementService.getUserResponseModelGroupedbyCountry(country);
	}
	
	// Get all admins.
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/admins")
	public UserResponseModel getAllAdmins() {
		return userManagementService.getUserResponseModelGroupedbyAdmins();
	}

	// Get user using Email.
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/getUser/{email}")
	public UserModel getUser(@PathVariable String email) throws Exception {
		try {

			UserModel user = userManagementService.getUserModelbyEmail(email);
			return user;

		} catch (Exception e) {
			throw new FileNotFoundException("Not Found");
		}
	}

	// Add new individual user.
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/addNewUser")
	public AppResponse saveUser(@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String userJson)
			throws JsonParseException, JsonMappingException, IOException {

		UserDto user = objectMapper.readValue(userJson, UserDto.class);
		AppResponse response = userManagementService.save(user);
		return response;
//		return new AppResponse(AppConstants.SUCCESS_CODE, AppConstants.SUCCESS_MSG);
	}

	// Add-Update password.
	@CrossOrigin(origins = "*")
	@PutMapping(value = "/updatePassword/{email}")
	public AppResponse updatePassword(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String userJson,
			@PathVariable String email) throws JsonParseException, JsonMappingException, IOException {

		UserData user = objectMapper.readValue(userJson, UserData.class);
		String password = bcryptEncoder.encode(user.getPassword());

		UserData existingUser = userManagementService.findOne(email);
		UserData newUser = handler.updateNewPassword(existingUser, password);
		userManagementService.updateUserPassword(newUser);
		return new AppResponse("200 - OK", "Password Updated.");
	}

	// Enable-Disable user.
	@CrossOrigin(origins = "*")
	@PutMapping(value = AppConstants.USER_UPDATE_STATUS_URI)
	public AppResponse updateStatus(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String userJson,
			@PathVariable String email) throws JsonParseException, JsonMappingException, IOException {

		UserData user = objectMapper.readValue(userJson, UserData.class);
		Boolean status = user.getUserStatus();

		UserData existingUser = userManagementService.findOne(email);
		UserData newUser = handler.updateStatus(existingUser, status);
		userManagementService.updateUserStatus(newUser);
		return new AppResponse("200 - OK", "Status Updated.");

	}

	// Modify-Update user details.
	@CrossOrigin(origins = "*")
	@PutMapping(value = AppConstants.USER_MODIFY_URI)
	public AppResponse modifyUser(@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String userJson,
			@PathVariable(required = true) Long id) throws JsonMappingException, JsonProcessingException, IOException {

		// Checking for existing user with id.
		UserData existingUser = userManagementService.findWithid(id);
		if (existingUser == null) {
			return new AppResponse("404-NOT FOUND", "User not found with id " + id);
		}

		UserDto user = objectMapper.readValue(userJson, UserDto.class);
		String newMail = user.getEmail(); // new mail given
		String existingMail = existingUser.getEmail(); // new mail given

		// Checking new email is registered or not.
		if (userManagementService.findOne(newMail) != null) {
			Boolean matcher = newMail.equalsIgnoreCase(existingMail);
			if (!matcher) {
				return new AppResponse("406-NOT ACCEPTABLE", "Using a registered Email ID " + newMail);
			}
		}

		userManagementService.modifyUser(user, existingUser);
		return new AppResponse("200 - OK", "User modified.");

	}

	// Get permitted domains
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/domains")
	public List<Domains> getAllDomains() {
		return domainService.findAll();
	}

	// Get avathar thumb nails.
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/avathars")
	public List<Avathars> getAllAvathars() {
		return avatharsService.findAll();
	}

	// Not for deployment.Just to add avathar Thumbnails.
	@CrossOrigin(origins = "*")
	@PutMapping(value = "/uploadAvathar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public AppResponse addAvatharThumbNails(
			@RequestParam(required = true, value = AppConstants.USER_FILE_PARAM) MultipartFile file)
			throws JsonMappingException, JsonProcessingException, IOException {

		// uploading image
		// String fileName = avatharsService.storeAvatharThumbs(file);
		String fileName = fileStorageService.storeFile(file);
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path(AppConstants.DOWNLOAD_PATH)
				.path(fileName).toUriString();

		// avatharsService.save(fileDownloadUri);
		avatharsService.save(fileDownloadUri);
		return new AppResponse("200 - OK", "Avathar Added.");

	}

	// Save-Upload user avathar-image.
	@CrossOrigin(origins = "*")
	@PutMapping(value = "/userAvatharImage/{email}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public AppResponse selectAvathar(
			@RequestParam(required = false, value = AppConstants.USER_FILE_PARAM) MultipartFile file,
			@RequestParam(required = false, value = "key") String avatharKey,
			@PathVariable(required = true) String email)
			throws JsonMappingException, JsonProcessingException, IOException {

		AppResponse appResponse = new AppResponse();
		// --
		if (file != null) {
			UserData existingUser = userManagementService.findOne(email);
			// uploading image
			String fileName = fileStorageService.storeFile(file);
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
					.path(AppConstants.DOWNLOAD_PATH).path(fileName).toUriString();
			UserData modifiedUser = handler.updateUserImageKey(existingUser, fileDownloadUri);
			userManagementService.updateAvathar(modifiedUser);
			appResponse.setCode("200-OK");
			appResponse.setMessage("User Avathar Modified");
		}
		if (avatharKey != null) {
			// saving avathar address
			UserData existingUser = userManagementService.findOne(email);
			UserData modifiedUser = handler.updateUserImageKey(existingUser, avatharKey);
			userManagementService.updateAvathar(modifiedUser);
			appResponse.setCode("200-OK");
			appResponse.setMessage("User Avathar Modified");
		}
		// --
		return appResponse;

	}

	// Download imagefiles.
	@CrossOrigin(origins = "*")
	@GetMapping(value = AppConstants.DOWNLOAD_URI)
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {

		// String nameString = fileName.toString();
		// String regex = "\\Bavathar|avathar\\B";
		//
		// Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		// Matcher matcher = pattern.matcher(nameString);

		Resource resource = fileStorageService.loadFileAsResource(fileName);
		//
		// if (matcher.find()) {
		// resource = avatharsService.loadAvatharFileAsResource(fileName);
		// } else {
		// resource = fileStorageService.loadFileAsResource(fileName);
		// }
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

	// Get user credentials.
	@CrossOrigin(origins = "*")
	@GetMapping(value = AppConstants.USER_CRED_URI)
	public ResponseEntity<?> getCreds(@PathVariable String email) {

		UserData userData = new UserData();
		UserCredsModel responsedata = new UserCredsModel();
		UserManagementHandler handler = new UserManagementHandler();
		userData = userManagementService.findOne(email);
		if (userData == null) {
			return ResponseEntity.ok(responsedata);
		}
		responsedata = handler.convertToCredsResponseModel(userData);
		return ResponseEntity.ok(responsedata);
	}

	/**
	 * end
	 */

	// checking
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/adminping", method = RequestMethod.GET)
	public String userPing() {
		return "Any User Can Read This";
	}

	// checking
	@PreAuthorize("hasRole('LISTENER')")
	@RequestMapping(value = "/listenerping", method = RequestMethod.GET)
	public String listenerPing() {
		return "Any User Can Read This";
	}

	// checking
	@PreAuthorize("hasRole('REVIEWER')")
	@RequestMapping(value = "/reviewerping", method = RequestMethod.GET)
	public String reviewerPing() {
		return "Any User Can Read This";
	}

	private String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	}

}
