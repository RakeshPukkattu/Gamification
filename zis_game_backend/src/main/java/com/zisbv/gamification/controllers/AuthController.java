package com.zisbv.gamification.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zisbv.gamification.entities.GameSessionUserData;
import com.zisbv.gamification.entities.GroupData;
import com.zisbv.gamification.entities.RefreshToken;
import com.zisbv.gamification.entities.UserData;
import com.zisbv.gamification.entities.UserDevice;
import com.zisbv.gamification.event.OnUserLogoutSuccessEvent;
import com.zisbv.gamification.exceptions.TokenRefreshException;
import com.zisbv.gamification.exceptions.UserLogoutException;
import com.zisbv.gamification.handlers.UserManagementHandler;
import com.zisbv.gamification.models.ApiResponse;
import com.zisbv.gamification.models.JwtResponse;
import com.zisbv.gamification.models.LogOutRequest;
import com.zisbv.gamification.models.LoginForm;
import com.zisbv.gamification.models.TokenRefreshRequest;
import com.zisbv.gamification.repositories.RolesRepository;
import com.zisbv.gamification.repositories.UserRepository;
import com.zisbv.gamification.security.JwtProvider;
import com.zisbv.gamification.service.FileStorageService;
import com.zisbv.gamification.service.GameSessionManagementService;
import com.zisbv.gamification.service.GameSessionManagementUserService;
import com.zisbv.gamification.service.RefreshTokenService;
import com.zisbv.gamification.service.UserDeviceService;
import com.zisbv.gamification.service.UserManagementService;
import com.zisbv.gamification.service.UsersJourneyService;
import com.zisbv.gamification.service.impl.CurrentUser;
import com.zisbv.gamification.service.impl.UserPrincipal;
import com.zisbv.gamification.util.AppConstants;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/gamification/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	UserManagementService userManagementService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	FileStorageService fileStorageService;

	@Autowired
	RolesRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtProvider jwtProvider;

	@Autowired
	private RefreshTokenService refreshTokenService;

	@Autowired
	private UserDeviceService userDeviceService;
	
	@Autowired
	private UsersJourneyService usersJourneyService;
	
	@Autowired
	private GameSessionManagementService gameSessionManagementService;
	
	@Autowired
	private GameSessionManagementUserService gameSessionManagementUserService;

	ObjectMapper objectMapper = new ObjectMapper();

	private enum LearnerParams {
		SELF_PACED, FACILITATOR_LEAD, LEADER, PARTICIPANT
	}

	@CrossOrigin(origins = "*")
	@PostMapping(value = "/signin", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> authenticateUser(
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String userJson,
			@RequestParam(required = false, value = AppConstants.USER_FILE_PARAM) MultipartFile file,
			@RequestParam(required = false, value = "key") String avatharKey,
			@RequestParam(required = false, value = "role") String role,
			@RequestParam(required = false, value = "category") String category,
			@RequestParam(required = false, value = "learnerRole") String learnerRole)
			throws JsonParseException, JsonMappingException, IOException {

		// input data
		LoginForm loginRequest = objectMapper.readValue(userJson, LoginForm.class);

		// checking whether existing user or not
		String email = loginRequest.getEmail();
		UserData user = userRepository.findByEmail(email);
		if (user == null) {
			throw new RuntimeException("Fail! -> Cause: User not found.");
		}

		// checking for user role
		if (role == null) {
			throw new RuntimeException("Fail! -> Cause: role not found.");
		} else {
			String allRoles = user.getRoles();
			if (allRoles.contains(role))
				;
		}
		if (user.getUserStatus()) {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwtToken = jwtProvider.generateJwtToken(authentication);
			userDeviceService.findByUserId(user.getId()).map(UserDevice::getRefreshToken).map(RefreshToken::getId)
					.ifPresent(refreshTokenService::deleteById);

			if (jwtToken != null) {
				UserManagementHandler handler = new UserManagementHandler();
				if (file != null) {
					// uploading image
					String fileName = fileStorageService.storeFile(file);
					UserData existingUser = userManagementService.findOne(email);
					String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
							.path(AppConstants.DOWNLOAD_PATH).path(fileName).toUriString();
					UserData modifiedUser = handler.updateUserImageKey(existingUser, fileDownloadUri);
					userManagementService.updateAvathar(modifiedUser);
				}
				if (avatharKey != null) {
					// saving avathar address
					UserData existingUser = userManagementService.findOne(email);
					UserData modifiedUser = handler.updateUserImageKey(existingUser, avatharKey);
					userManagementService.updateAvathar(modifiedUser);
				}
			}

			// Learner validations
			if (role.equalsIgnoreCase("LEARNER")) {

				if (category == null) {
					throw new RuntimeException("Fail! -> Cause: learner need to select a category to proceed.");
				} else {
					// Self Paced
					if (category.equalsIgnoreCase(LearnerParams.SELF_PACED.toString())) {
						
						UserData dataInDB = userRepository.getUserWithEmail(email);
						usersJourneyService.validateUserSession(LearnerParams.SELF_PACED.toString(), dataInDB);

						

					}
					// Facilitator_lead
					if (category.equalsIgnoreCase(LearnerParams.FACILITATOR_LEAD.toString())) {

						if (learnerRole == null) {
							throw new RuntimeException(
									"Fail! -> Cause: learner need to select a role for for proceeding with facilitator_lead.");
						} else {

							// Leader
							if (learnerRole.equalsIgnoreCase(LearnerParams.LEADER.toString())) {

							}

							// Participant
							if (learnerRole.equalsIgnoreCase(LearnerParams.PARTICIPANT.toString())) {

							}
						}

					}
				}
			}

			UserDevice userDevice = userDeviceService.createUserDevice(loginRequest.getDeviceInfo());
			RefreshToken refreshToken = refreshTokenService.createRefreshToken();
			userDevice.setUser(user);
			userDevice.setRefreshToken(refreshToken);
			refreshToken.setUserDevice(userDevice);
			refreshToken = refreshTokenService.save(refreshToken);
			return ResponseEntity.ok(new JwtResponse(user.getId(), email, role, jwtToken, refreshToken.getToken(),
					jwtProvider.getExpiryDuration()));
		}
		return ResponseEntity.badRequest().body(new ApiResponse(false, "User has been deactivated/locked !!"));
	}

	@CrossOrigin(origins = "*")
	@PostMapping("/refresh/{email}")
	public ResponseEntity<?> refreshJwtToken(@PathVariable String email,
			@RequestParam(value = AppConstants.USER_JSON_PARAM, required = true) String userJson,
			@RequestParam(required = false, value = "role") String role)
			throws JsonParseException, JsonMappingException, IOException {

		UserData user = userRepository.findByEmail(email);
		Long userId = user.getId();
		TokenRefreshRequest tokenRefreshRequest = objectMapper.readValue(userJson, TokenRefreshRequest.class);
		String requestRefreshToken = tokenRefreshRequest.getRefreshToken();

		Optional<String> token = Optional.of(refreshTokenService.findByToken(requestRefreshToken).map(refreshToken -> {
			refreshTokenService.verifyExpiration(refreshToken);
			userDeviceService.verifyRefreshAvailability(refreshToken);
			refreshTokenService.increaseCount(refreshToken);
			return refreshToken;
		}).map(RefreshToken::getUserDevice).map(UserDevice::getUser).map(u -> jwtProvider.generateTokenFromUser(u))
				.orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
						"Missing refresh token in database. Please login again")));
		return ResponseEntity.ok().body(new JwtResponse(userId, email, role, token.get(),
				tokenRefreshRequest.getRefreshToken(), jwtProvider.getExpiryDuration()));
	}

	// Logout.
	@CrossOrigin(origins = "*")
	@PutMapping("/signout")
	public ResponseEntity<ApiResponse> logoutUser(@CurrentUser UserPrincipal currentUser,
			@Valid @RequestBody LogOutRequest logOutRequest) {
		String deviceId = logOutRequest.getDeviceInfo().getDeviceId();
		UserDevice userDevice = userDeviceService.findByUserId(currentUser.getId())
				.filter(device -> device.getDeviceId().equals(deviceId))
				.orElseThrow(() -> new UserLogoutException(logOutRequest.getDeviceInfo().getDeviceId(),
						"Invalid device Id supplied. No matching device found for the given user "));
		refreshTokenService.deleteById(userDevice.getRefreshToken().getId());

		OnUserLogoutSuccessEvent logoutSuccessEvent = new OnUserLogoutSuccessEvent(currentUser.getEmail(),
				logOutRequest.getToken(), logOutRequest);
		applicationEventPublisher.publishEvent(logoutSuccessEvent);
		return ResponseEntity.ok(new ApiResponse(true, "User has successfully logged out from the system!"));
	}
}
