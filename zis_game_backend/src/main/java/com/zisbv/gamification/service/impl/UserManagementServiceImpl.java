package com.zisbv.gamification.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.zisbv.gamification.config.AsynchronousMailSender;
import com.zisbv.gamification.dto.SuperAdminDto;
import com.zisbv.gamification.dto.UserDto;
import com.zisbv.gamification.dto.UserRegisterDto;
import com.zisbv.gamification.entities.City;
import com.zisbv.gamification.entities.Country;
import com.zisbv.gamification.entities.Region;
import com.zisbv.gamification.entities.State;
import com.zisbv.gamification.entities.UserData;
import com.zisbv.gamification.exceptions.UserNotFoundException;
import com.zisbv.gamification.models.AppResponse;
import com.zisbv.gamification.models.GamificationMailContents;
import com.zisbv.gamification.models.UserModel;
import com.zisbv.gamification.models.UserResponseModel;
import com.zisbv.gamification.repositories.CityRepository;
import com.zisbv.gamification.repositories.CountryRepository;
import com.zisbv.gamification.repositories.RegionRepository;
import com.zisbv.gamification.repositories.StateRepository;
import com.zisbv.gamification.repositories.UserRepository;
import com.zisbv.gamification.service.UserManagementService;
import com.zisbv.gamification.util.AppConstants;

import net.bytebuddy.utility.RandomString;

@Service(value = "userManagementService")
public class UserManagementServiceImpl implements UserManagementService {

	@Value("${dev.url}")
	String devURl;

	@Autowired
	UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;

	@Autowired
	private AsynchronousMailSender asynchronousWorker;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private RegionRepository regionRepository;

	@Autowired
	private StateRepository stateRepository;

	@Autowired
	private CityRepository cityRepository;

	@Override
	public void register(UserData user, String siteURL) throws UnsupportedEncodingException, MessagingException {

		String encodedPassword = bcryptEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);

		String randomCode = RandomString.make(64);
		user.setVerificationCode(randomCode);
		user.setRoles("Learner");
		user.setUserStatus(false);
		user.setUserInAnyGroup(false);
		user.setUserInAnySession(false);

		userRepository.save(user);

		sendVerificationEmail(user, siteURL);

	}

	@Override
	public boolean verify(String verificationCode) {

		UserData user = userRepository.findByVerificationCode(verificationCode);

		if (user == null) {
			return false;
		} else {
			UserData modifiedUser = new UserData();

			modifiedUser.setId(user.getId());
			modifiedUser.setUserName(user.getUserName());
			modifiedUser.setEmail(user.getEmail());
			modifiedUser.setPassword(user.getPassword());
			modifiedUser.setImageKey(user.getImageKey());
			modifiedUser.setRoles(user.getRoles());
			modifiedUser.setCountryID(user.getCountryID());
			modifiedUser.setRegionID(user.getRegionID());
			modifiedUser.setStateID(user.getStateID());
			modifiedUser.setCityID(user.getCityID());
			modifiedUser.setUserStatus(true);
			modifiedUser.setUserInAnyGroup(false);
			modifiedUser.setUserInAnySession(false);
			modifiedUser.setResetPasswordToken(user.getResetPasswordToken());
			modifiedUser.setVerificationCode(null);
			modifiedUser.setGroups(user.getGroups());
			modifiedUser.setUserSessions(user.getUserSessions());

			userRepository.save(modifiedUser);
			return true;
		}
	}

	@Override
	public List<UserData> findAll() {
		List<UserData> list = new ArrayList<>();
		userRepository.findAll().iterator().forEachRemaining(list::add);
		return list;
	}

	@Override
	public UserResponseModel getUserResponseModel() {

		List<UserData> dataFromDb = userRepository.findAll();
		List<UserModel> responseModel = new ArrayList<UserModel>();
		UserResponseModel response = new UserResponseModel();

		// iterating database data and adding in to response model.
		for (UserData userData : dataFromDb) {

			UserModel responseData = new UserModel();
			responseData.setId(userData.getId().intValue());
			responseData.setName(userData.getUserName());
			responseData.setImageKey(userData.getImageKey());
			responseData.setEmail(userData.getEmail());

			String roleStringFromDb = userData.getRoles();
			String[] rolesArray = roleStringFromDb.split(",", 0);
			responseData.setRoles(rolesArray);

			for (String role : rolesArray) {
				if (role.equalsIgnoreCase("SuperAdmin")) {
					responseData.setCountry("ALL COUNTRIES");
					break;
				} else {
					Country countryData = countryRepository.getCountryWithID(userData.getCountryID());
					responseData.setCountry(countryData.getCountryName());
				}
			}

			Region regionData = regionRepository.getRegionWithID(userData.getRegionID());
			if (regionData == null) {
				responseData.setRegion("");
			} else {
				responseData.setRegion(regionData.getRegionName());
			}

			State stateData = stateRepository.getStateWithID(userData.getStateID());
			if (stateData == null) {
				responseData.setState("");
			} else {
				responseData.setState(stateData.getStateName());
			}

			City cityData = cityRepository.getCityWithID(userData.getCityID());
			if (cityData == null) {
				responseData.setCity("");
				responseData.setCityLatitude("");
				responseData.setCityLongitude("");
			} else {
				responseData.setCity(cityData.getCityName());
				responseData.setCityLatitude(cityData.getCityLattitude());
				responseData.setCityLongitude(cityData.getCityLongitude());
			}

			responseData.setStatus(userData.getUserStatus());
			responseData.setUser_In_Group(userData.getUserInAnyGroup());
			responseData.setUser_In_Any_Session(userData.getUserInAnySession());

			responseModel.add(responseData);
		}

		response.setUsers(responseModel);
		return response;
	}

	@Override
	public UserResponseModel getUserResponseModelGroupedbyAdmins() {

		List<UserData> dataFromDb = userRepository.findAll();
		List<UserModel> responseModel = new ArrayList<UserModel>();
		UserResponseModel response = new UserResponseModel();

		for (UserData userData : dataFromDb) {
			if (userData != null) {
				String roles = userData.getRoles();
				String[] rolesArray = roles.split(",");
				for (String currentRole : rolesArray) {
					if (currentRole.equalsIgnoreCase("Admin")) {

						UserModel responseData = new UserModel();
						responseData.setId(userData.getId().intValue());
						responseData.setName(userData.getUserName());
						responseData.setImageKey(userData.getImageKey());
						responseData.setEmail(userData.getEmail());

						String roleStringFromDb = userData.getRoles();
						String[] rolesINArray = roleStringFromDb.split(",", 0);
						responseData.setRoles(rolesINArray);

						Country countryData = countryRepository.getCountryWithID(userData.getCountryID());
						for (String role : rolesINArray) {
							if (role.equalsIgnoreCase("SuperAdmin")) {
								responseData.setCountry("ALL COUNTRIES");
								break;
							} else {
								responseData.setCountry(countryData.getAliasCountryName());
							}
						}

						Region regionData = regionRepository.getRegionWithID(userData.getRegionID());
						if (regionData == null) {
							responseData.setRegion("");
						} else {
							responseData.setRegion(regionData.getRegionName());
						}

						State stateData = stateRepository.getStateWithID(userData.getStateID());
						if (stateData == null) {
							responseData.setState("");
						} else {
							responseData.setState(stateData.getStateName());
						}

						City cityData = cityRepository.getCityWithID(userData.getCityID());
						if (cityData == null) {
							responseData.setCity("");
							responseData.setCityLatitude("");
							responseData.setCityLongitude("");
						} else {
							responseData.setCity(cityData.getCityName());
							responseData.setCityLatitude(cityData.getCityLattitude());
							responseData.setCityLongitude(cityData.getCityLongitude());
						}

						responseData.setStatus(userData.getUserStatus());
						responseData.setUser_In_Group(userData.getUserInAnyGroup());
						responseData.setUser_In_Any_Session(userData.getUserInAnySession());
						responseModel.add(responseData);

					}
					response.setUsers(responseModel);
				}
			}
		}
		return response;
	}

	@Override
	public UserResponseModel getUserResponseModelGroupedbyCountry(String countryName) {
		List<UserData> dataFromDb = userRepository.findAll();

		List<UserModel> responseModel = new ArrayList<UserModel>();
		UserResponseModel response = new UserResponseModel();

		for (UserData userData : dataFromDb) {
			Country countryData = countryRepository.getCountryWithName(countryName);
			Long countryID = countryData.getId();
			if (userData != null) {
				if (userData.getCountryID().equals(countryID)) {

					UserModel responseData = new UserModel();
					responseData.setId(userData.getId().intValue());
					responseData.setName(userData.getUserName());
					responseData.setImageKey(userData.getImageKey());
					responseData.setEmail(userData.getEmail());

					String roleStringFromDb = userData.getRoles();
					String[] rolesArray = roleStringFromDb.split(",", 0);
					responseData.setRoles(rolesArray);

					for (String role : rolesArray) {
						if (role.equalsIgnoreCase("SuperAdmin")) {
							responseData.setCountry("ALL COUNTRIES");
							break;
						} else {
							responseData.setCountry(countryData.getAliasCountryName());
						}
					}

					Region regionData = regionRepository.getRegionWithID(userData.getRegionID());
					if (regionData == null) {
						responseData.setRegion("");
					} else {
						responseData.setRegion(regionData.getRegionName());
					}

					State stateData = stateRepository.getStateWithID(userData.getStateID());
					if (stateData == null) {
						responseData.setState("");
					} else {
						responseData.setState(stateData.getStateName());
					}

					City cityData = cityRepository.getCityWithID(userData.getCityID());
					if (cityData == null) {
						responseData.setCity("");
						responseData.setCityLatitude("");
						responseData.setCityLongitude("");
					} else {
						responseData.setCity(cityData.getCityName());
						responseData.setCityLatitude(cityData.getCityLattitude());
						responseData.setCityLongitude(cityData.getCityLongitude());
					}

					responseData.setStatus(userData.getUserStatus());
					responseData.setUser_In_Group(userData.getUserInAnyGroup());
					responseData.setUser_In_Any_Session(userData.getUserInAnySession());
					responseModel.add(responseData);

				}
				response.setUsers(responseModel);
			}
		}

		return response;
	}

	@Override
	public UserData findOne(String email) {
		UserData userFromDB = userRepository.findByEmail(email);
		return userFromDB;
	}

	@Override
	public UserModel getUserModelbyEmail(String email) {

		UserData userFromDB = userRepository.findByEmail(email);
		UserModel user = new UserModel();

		user.setId(userFromDB.getId().intValue());
		user.setName(userFromDB.getUserName());
		user.setImageKey(userFromDB.getImageKey());
		user.setEmail(userFromDB.getEmail());

		String rolesString = userFromDB.getRoles();
		String[] rolesArray = rolesString.split(",", 0);
		user.setRoles(rolesArray);

		for (String role : rolesArray) {
			if (role.equalsIgnoreCase("SuperAdmin")) {
				user.setCountry("ALL COUNTRIES");
				break;
			} else {
				Country countryData = countryRepository.getCountryWithID(userFromDB.getCountryID());
				user.setCountry(countryData.getCountryName());
			}
		}

		Region regionData = regionRepository.getRegionWithID(userFromDB.getRegionID());
		if (regionData == null) {
			user.setRegion("");
		} else {
			user.setRegion(regionData.getRegionName());
		}

		State stateData = stateRepository.getStateWithID(userFromDB.getStateID());
		if (stateData == null) {
			user.setState("");
		} else {
			user.setState(stateData.getStateName());
		}

		City cityData = cityRepository.getCityWithID(userFromDB.getCityID());
		if (cityData == null) {
			user.setCity("");
			user.setCityLatitude("");
			user.setCityLongitude("");
		} else {
			user.setCity(cityData.getCityName());
			user.setCityLatitude(cityData.getCityLattitude());
			user.setCityLongitude(cityData.getCityLongitude());
		}

		user.setStatus(userFromDB.getUserStatus());
		user.setUser_In_Group(userFromDB.getUserInAnyGroup());
		user.setUser_In_Any_Session(userFromDB.getUserInAnySession());
		return user;

	}

	@Override
	public UserData findWithid(Long id) {
		UserData data = userRepository.findById(id);
		return data;
	}

	@Override
	public AppResponse save(UserDto user) {
		UserData data = user.dataFromPojo();

		// Checking for existing user.
		UserData existingUser = findOne(data.getEmail());

		if (existingUser == null) {

			String randomCode = RandomString.make(64);
			data.setVerificationCode(randomCode);
			data.setUserStatus(false);
			data.setUserInAnyGroup(false);
			data.setUserInAnySession(false);

			userRepository.save(data);

			// send mail to user
			try {
				sendVerificationEmail(data, devURl);
			} catch (UnsupportedEncodingException | MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return new AppResponse(AppConstants.SUCCESS_CODE, AppConstants.SUCCESS_MSG);
		}

		return new AppResponse("401", "Using a registered email_id : " + data.getEmail());
	}

	@Override
	public UserData registerSuperAdmin(SuperAdminDto user) {
		UserData newSuperAdmin = new UserData();
		UserData data = user.dataFromPojo();

		// Checking for existing user.
		UserData existingUser = findOne(data.getEmail());
		if (existingUser == null) {
			data.setPassword(bcryptEncoder.encode(user.getPassword()));
			newSuperAdmin = userRepository.save(data);
		}

		return newSuperAdmin;
	}

	public UserData registerLearner(UserData existingUser, UserRegisterDto user) {

		existingUser.setUserName(user.getName());
		existingUser.setEmail(user.getEmail());
		existingUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		existingUser.setCountryID(user.getCountryID());
		existingUser.setRegionID(user.getRegionID());
		existingUser.setStateID(user.getStateID());
		existingUser.setCityID(user.getCityID());
		existingUser.setRoles("Learner");
		existingUser.setUserStatus(true);
		existingUser.setUserInAnyGroup(false);
		userRepository.save(existingUser);

		return existingUser;
	}

	@Override
	public void updateUserPassword(UserData newUser) {
		userRepository.save(newUser);
	}

	@Override
	public void modifyUser(UserDto userInput, UserData existingUser) {

		UserData newUser = new UserData();
		newUser.setId(existingUser.getId());

		newUser.setUserName(userInput.getName());
		newUser.setEmail(userInput.getEmail());
		if (existingUser.getPassword() != null) {
			newUser.setPassword(existingUser.getPassword());
		}
		newUser.setImageKey(existingUser.getImageKey());
		String rolesString = userInput.getRoles().stream().map(Object::toString).collect(Collectors.joining(","));
		newUser.setRoles(rolesString);
		newUser.setCountryID(userInput.getCountryID());
		newUser.setRegionID(userInput.getRegionID());
		newUser.setStateID(userInput.getStateID());
		newUser.setCityID(userInput.getCityID());
		if (existingUser.getUserStatus() != null) {
			newUser.setUserStatus(existingUser.getUserStatus());
		}
		if (existingUser.getUserInAnyGroup() != null) {
			newUser.setUserInAnyGroup(existingUser.getUserInAnyGroup());
		}
		if (existingUser.getUserInAnySession() != null) {
			newUser.setUserInAnySession(existingUser.getUserInAnySession());
		}
		newUser.setResetPasswordToken(existingUser.getResetPasswordToken());
		newUser.setVerificationCode(existingUser.getVerificationCode());
		newUser.setGroups(existingUser.getGroups());
		newUser.setUserSessions(existingUser.getUserSessions());
		userRepository.save(newUser);

		// send mail to user
		try {
			sendUserDetailsChangeEmail(newUser, devURl);
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateAvathar(UserData updatedData) {
		userRepository.save(updatedData);

		// send mail to user
		try {
			sendUserDetailsChangeEmail(updatedData, devURl);
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void updateOtherStatus(UserData user) {
		userRepository.save(user);
	}

	@Override
	public void updateUserStatus(UserData user) {

		userRepository.save(user);
		// send mail to user
		try {
			sendStatusChangeEmail(user, devURl);
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateResetPasswordToken(String token, String email) throws UserNotFoundException {
		UserData user = userRepository.findByEmail(email);
		if (user != null) {
			user.setResetPasswordToken(token);
			userRepository.save(user);
		} else {
			throw new UserNotFoundException("Could not find any user with the email " + email);
		}

	}

	@Override
	public void updateVerifiedUser(String email) throws UserNotFoundException {
		UserData user = userRepository.findByEmail(email);
		if (user == null) {
			UserData newUser = new UserData();
			newUser.setEmail(email);
			newUser.setUserStatus(true);
			newUser.setUserInAnyGroup(false);
			userRepository.save(newUser);
		} else {
			throw new UserNotFoundException("Using a registered email_id : " + email);
		}

	}

	@Override
	public UserData getByResetPasswordToken(String token) {
		return userRepository.findByResetPasswordToken(token);
	}

	@Override
	public void updatePassword(UserData user, String newPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(newPassword);
		user.setPassword(encodedPassword);

		user.setResetPasswordToken(null);
		userRepository.save(user);

	}

	@Override
	public UserData updateUserInAnySession(UserData user) {
		return userRepository.save(user);
	}

	// private area.
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

//	private void sendPasswordChangeEmail(UserData user, String siteURL)
//			throws MessagingException, UnsupportedEncodingException {
//
//		try {
//
//			String subject = "Password change notification :: Gamification.";
//			String content = "Dear [[name]],<br>"
//					+ "<br><br><br>Gamification password hasbeen updated during [[modifiedTime]]."
//					+ "<br>If you are not aware of this change kindly contact Admin."
//					+ "<h3><a href=\"[[URL]]\" target=\"_self\"><br>Get Gamified</a></h3>" + "Regards,<br>"
//					+ "ZIS-GAME";
//			content = content.replace("[[name]]", user.getName());
//			content = content.replace("[[URL]]", siteURL);
//			content = content.replace("[[modifiedTime]]", user.getLast_modified().toString());
//
//			GamificationMailContents mail = new GamificationMailContents(user.getEmail(), subject, content);
//			asynchronousWorker.sendMail(mail);
//
//		} catch (InterruptedException e) {
//			System.out.println(e);
//		}
//
//	}

	private void sendStatusChangeEmail(UserData user, String siteURL)
			throws MessagingException, UnsupportedEncodingException {

		try {

			String subject = "Status change notification :: Gamification.";
			String content = "Dear [[name]],<br>" + "<br><br><br>Gamification user status hasbeen updated."
					+ "<br>If you are not aware of this change kindly contact Admin."
					+ "<h3><a href=\"[[URL]]\" target=\"_self\"><br>Get Gamified</a></h3>" + "Regards,<br>"
					+ "ZIS-GAME";
			content = content.replace("[[name]]", user.getUserName());
			content = content.replace("[[URL]]", siteURL);

			GamificationMailContents mail = new GamificationMailContents(user.getEmail(), subject, content);
			asynchronousWorker.sendMail(mail);

		} catch (InterruptedException e) {
			System.out.println(e);
		}

	}

	private void sendUserDetailsChangeEmail(UserData user, String siteURL)
			throws MessagingException, UnsupportedEncodingException {

		try {

			String subject = "User details modified :: Gamification.";
			String content = "Dear [[name]],<br>" + "<br><br><br>Gamification user deatils hasbeen updated or change."
					+ "<br>If you are not aware of this change kindly contact Admin."
					+ "<h3><a href=\"[[URL]]\" target=\"_self\"><br>Get Gamified</a></h3>" + "Regards,<br>"
					+ "ZIS-GAME";
			content = content.replace("[[name]]", user.getUserName());
			content = content.replace("[[URL]]", siteURL);

			GamificationMailContents mail = new GamificationMailContents(user.getEmail(), subject, content);
			asynchronousWorker.sendMail(mail);

		} catch (InterruptedException e) {
			System.out.println(e);
		}

	}

}
