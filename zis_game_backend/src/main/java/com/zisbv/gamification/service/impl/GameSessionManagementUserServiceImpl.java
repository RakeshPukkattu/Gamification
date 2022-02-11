package com.zisbv.gamification.service.impl;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.zisbv.gamification.config.AsynchronousMailSender;
import com.zisbv.gamification.dto.GameSessionUserDto;
import com.zisbv.gamification.entities.Country;
import com.zisbv.gamification.entities.GameSessionUserData;
import com.zisbv.gamification.entities.UserData;
import com.zisbv.gamification.models.GameSessionUserModel;
import com.zisbv.gamification.models.GameSessionUserResponseModel;
import com.zisbv.gamification.models.GamificationMailContents;
import com.zisbv.gamification.models.UserInGroupSessionModel;
import com.zisbv.gamification.repositories.CountryRepository;
import com.zisbv.gamification.repositories.GameSessionUserRepository;
import com.zisbv.gamification.service.GameSessionManagementUserService;
import com.zisbv.gamification.service.UserManagementService;

@Service(value = "gameSessionManagementUserService")
public class GameSessionManagementUserServiceImpl implements GameSessionManagementUserService {

	@Value("${dev.url}")
	String devURl;

	@Autowired
	private AsynchronousMailSender asynchronousWorker;

	@Autowired
	GameSessionUserRepository sessionRepository;

	@Autowired
	UserManagementService userManagementService;

	@Autowired
	CountryRepository countryRepository;

	@Override
	public List<GameSessionUserData> findAll() {
		List<GameSessionUserData> list = new ArrayList<>();
		sessionRepository.findAll().iterator().forEachRemaining(list::add);
		return list;
	}

	@Override
	public GameSessionUserResponseModel getSessionUserResponseModel() {
		// final response
		GameSessionUserResponseModel gameSessionUserResponseModel = new GameSessionUserResponseModel();
		List<GameSessionUserModel> responseModel = new ArrayList<GameSessionUserModel>();

		// Data from db
		List<GameSessionUserData> dataFromDb = findAll();

		// Iterating data from db
		for (GameSessionUserData gameSession : dataFromDb) {

			GameSessionUserModel session = new GameSessionUserModel();
			List<UserInGroupSessionModel> userInSessionList = new ArrayList<UserInGroupSessionModel>();

			session.setSessionId(gameSession.getId());
			session.setSessionName(gameSession.getSessionName());
			session.setCountryID(gameSession.getCountryID());

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String formattedStartDateTime = gameSession.getStartDateAndTime().format(formatter);
			session.setStartTime(formattedStartDateTime);

			String formattedEndDateTime = gameSession.getEndDateAndTime().format(formatter);
			session.setEndTime(formattedEndDateTime);

			session.setSessionCurrentStatus(gameSession.getSessionCurrentStatus());
			session.setSessionEnableDisable(gameSession.getSessionEnableDisable());

			Set<UserData> usersInses = gameSession.getUsers();

			for (UserData s : usersInses) {

				UserInGroupSessionModel userInGroupSessionModel = new UserInGroupSessionModel();

				userInGroupSessionModel.setId(s.getId());
				userInGroupSessionModel.setUserName(s.getUserName());
				userInGroupSessionModel.setUserEmail(s.getEmail());
				userInGroupSessionModel.setAvatharKey(s.getImageKey());

				userInSessionList.add(userInGroupSessionModel);
			}

//			// Getting groupIds from session.
//			String userIds = gameSession.getUserIds();
//			String[] userIdsArray = userIds.split(",");
//
//			// iterating groups
//			for (String userID : userIdsArray) {
//
//				UserInGroupSessionModel userInGroupSessionModel = new UserInGroupSessionModel();
//
//				// Data from DataBase
//				UserData userDataFromDb = userManagementService.findWithid(Long.parseLong(userID));
//
//				userInGroupSessionModel.setId(userDataFromDb.getId());
//				userInGroupSessionModel.setUserName(userDataFromDb.getName());
//				userInGroupSessionModel.setUserEmail(userDataFromDb.getEmail());
//				userInGroupSessionModel.setAvatharKey(userDataFromDb.getImageKey());
//
//				userInSessionList.add(userInGroupSessionModel);
//
//			}

			session.setGetUsersInSession(userInSessionList);

			responseModel.add(session);

			gameSessionUserResponseModel.setSessions(responseModel);
		}
		return gameSessionUserResponseModel;
	}

	@Override
	public GameSessionUserResponseModel getUserSessionResponseModelGroupedbyCountry(String country) {

		// final response
		GameSessionUserResponseModel gameSessionUserResponseModel = new GameSessionUserResponseModel();
		List<GameSessionUserModel> responseModel = new ArrayList<GameSessionUserModel>();

		// Data from db
		List<GameSessionUserData> dataFromDb = findAll();

		// Iterating data from db
		for (GameSessionUserData gameSession : dataFromDb) {

			Country countryData = countryRepository.getCountryWithID(gameSession.getCountryID());
			if (countryData.getCountryName().equalsIgnoreCase(country)) {

				GameSessionUserModel session = new GameSessionUserModel();
				List<UserInGroupSessionModel> userInSessionList = new ArrayList<UserInGroupSessionModel>();

				session.setSessionId(gameSession.getId());
				session.setSessionName(gameSession.getSessionName());
				session.setCountryID(gameSession.getCountryID());

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				String formattedStartDateTime = gameSession.getStartDateAndTime().format(formatter);
				session.setStartTime(formattedStartDateTime);

				String formattedEndDateTime = gameSession.getEndDateAndTime().format(formatter);
				session.setEndTime(formattedEndDateTime);

				session.setSessionCurrentStatus(gameSession.getSessionCurrentStatus());
				session.setSessionEnableDisable(gameSession.getSessionEnableDisable());

//				session.setUsers(gameSession.getUsers());

				// Getting groupIds from session.
				String userIds = gameSession.getUserIds();
				String[] userIdsArray = userIds.split(",");

				// iterating groups
				for (String userID : userIdsArray) {

					UserInGroupSessionModel userInGroupSessionModel = new UserInGroupSessionModel();

					// Data from DataBase
					UserData userDataFromDb = userManagementService.findWithid(Long.parseLong(userID));

					userInGroupSessionModel.setId(userDataFromDb.getId());
					userInGroupSessionModel.setUserName(userDataFromDb.getUserName());
					userInGroupSessionModel.setUserEmail(userDataFromDb.getEmail());
					userInGroupSessionModel.setAvatharKey(userDataFromDb.getImageKey());

					userInSessionList.add(userInGroupSessionModel);

				}

				session.setGetUsersInSession(userInSessionList);

				responseModel.add(session);

				gameSessionUserResponseModel.setSessions(responseModel);
			}

		}
		return gameSessionUserResponseModel;
	}

	@Override
	public GameSessionUserData findWithid(Long id) {
		GameSessionUserData dataFromDB = sessionRepository.getSessionWithID(id);
		return dataFromDB;
	}

	@Override
	public GameSessionUserModel getSession(Long id) {
		// Data from DataBase.
		GameSessionUserData dataFromDb = findWithid(id);

		GameSessionUserModel response = new GameSessionUserModel();
		response.setSessionId(dataFromDb.getId());
		response.setSessionName(dataFromDb.getSessionName());
		response.setCountryID(dataFromDb.getCountryID());
		response.setStartTime(dataFromDb.getStartDateAndTime().toString());
		response.setEndTime(dataFromDb.getEndDateAndTime().toString());
		response.setSessionCurrentStatus(dataFromDb.getSessionCurrentStatus());
		response.setSessionEnableDisable(dataFromDb.getSessionEnableDisable());

		// groups in session
		response.setGetUsersInSession(getuserInSession(Long.valueOf(dataFromDb.getId())));

		return response;
	}

	@Override
	public List<UserInGroupSessionModel> getuserInSession(Long id) {
		// Final response
		List<UserInGroupSessionModel> userInSessionList = new ArrayList<UserInGroupSessionModel>();

		// Getting groupIds from session.
		GameSessionUserData gameSessionUserData = sessionRepository.getSessionWithID(id);

		String userIds = gameSessionUserData.getUserIds();
		String[] userIdsArray = userIds.split(",");

		// iterating users
		for (String userID : userIdsArray) {

			UserData memberFromDb = userManagementService.findWithid(Long.parseLong(userID));

			UserInGroupSessionModel user = new UserInGroupSessionModel();
			long idLocal = memberFromDb.getId();
			user.setId(idLocal);
			user.setUserName(memberFromDb.getUserName());
			user.setUserEmail(memberFromDb.getEmail());
			user.setAvatharKey(memberFromDb.getImageKey());

			// Adding object to response list
			userInSessionList.add(user);

		}

		// Returning response
		return userInSessionList;
	}

	@Override
	public void updateStatus(GameSessionUserData session) {

		sessionRepository.save(session);

		String userInSession = session.getUserIds();
		String[] userInSessionArray = userInSession.split(",");

		for (String userIdInSession : userInSessionArray) {

			UserData userData = userManagementService.findWithid(Long.parseLong(userIdInSession));

			// sending notification mail as user is now part of session.
			sendStatusChangeEmail(userData, devURl);
		}

	}

	@Override
	public GameSessionUserData findWithName(String name) {
		GameSessionUserData data = sessionRepository.getSessionWithName(name);
		return data;
	}

	@Override
	public void createGameSession(GameSessionUserDto session, Long countryID) {
		GameSessionUserData data = session.dataFromPojo();
		data.setCountryID(countryID);

		// Checking for existing session.
		GameSessionUserData existingSession = findWithid(data.getId());
		if (existingSession == null) {

			// update groupInAnySession to true
			String userIdString = data.getUserIds();
			String[] userIdArray = userIdString.split(",");

			for (String id : userIdArray) {

				UserData userModel = userManagementService.findWithid(Long.parseLong(id));

				data.getUsers().add(userModel);

				UserData userData = new UserData();

				userData.setId(userModel.getId());
				userData.setUserName(userModel.getUserName());
				userData.setEmail(userModel.getEmail());
				userData.setPassword(userModel.getPassword());
				userData.setImageKey(userModel.getImageKey());
				userData.setRoles(userModel.getRoles());
				userData.setCountryID(userModel.getCountryID());
				userData.setRegionID(userModel.getRegionID());
				userData.setStateID(userModel.getStateID());
				userData.setCityID(userModel.getCityID());
				userData.setUserStatus(userModel.getUserStatus());
				userData.setUserInAnyGroup(userModel.getUserInAnyGroup());
				userData.setUserInAnySession(true);
				userData.setResetPasswordToken(userModel.getResetPasswordToken());
				userData.setVerificationCode(userModel.getVerificationCode());

				userManagementService.updateUserInAnySession(userData);

				// sending notification mail as user is now part of session.
				sendAddedToSessionEmail(userData, devURl);

			}

			sessionRepository.save(data);

		}

	}

	@Override
	public GameSessionUserData updateGameUserSession(GameSessionUserData session, GameSessionUserData existingSession) {

		// getting previous session data
		GameSessionUserData previousSessionData = existingSession;

		// email notification
		String userIdInExistingSession = previousSessionData.getUserIds();
		String[] userIdInExistingSessionArray = userIdInExistingSession.split(",");

		String userIdInNewSession = session.getUserIds();
		String[] userIdInNewSessionArray = userIdInNewSession.split(",");

		for (int i = 0; i < userIdInExistingSessionArray.length; i++) {

			if (Arrays.stream(userIdInNewSessionArray).anyMatch(userIdInExistingSessionArray[i]::equalsIgnoreCase)) {
				UserData userModel = userManagementService.findWithid(Long.parseLong(userIdInExistingSessionArray[i]));
				sendModifiedToSessionEmail(userModel, devURl);
			}
			if (!Arrays.stream(userIdInNewSessionArray).anyMatch(userIdInExistingSessionArray[i]::equalsIgnoreCase)) {
				UserData userModel = userManagementService.findWithid(Long.parseLong(userIdInExistingSessionArray[i]));
				sendSessionEndedEmail(userModel, devURl);
			}
		}

		for (int i = 0; i < userIdInNewSessionArray.length; i++) {
			if (!Arrays.stream(userIdInExistingSessionArray).anyMatch(userIdInNewSessionArray[i]::equalsIgnoreCase)) {
				UserData userModel = userManagementService.findWithid(Long.parseLong(userIdInNewSessionArray[i]));
				sendAddedToSessionEmail(userModel, devURl);
			}
		}

		// update userInAnySession to true
		String userIdString = session.getUserIds();
		String[] userIdArray = userIdString.split(",");

		for (String id : userIdArray) {

			UserData userModel = userManagementService.findWithid(Long.parseLong(id));

			UserData userData = new UserData();

			userData.setId(userModel.getId());
			userData.setUserName(userModel.getUserName());
			userData.setEmail(userModel.getEmail());
			userData.setPassword(userModel.getPassword());
			userData.setImageKey(userModel.getImageKey());
			userData.setRoles(userModel.getRoles());
			userData.setCountryID(userModel.getCountryID());
			userData.setRegionID(userModel.getRegionID());
			userData.setStateID(userModel.getStateID());
			userData.setCityID(userModel.getCityID());
			userData.setUserStatus(userModel.getUserStatus());
			userData.setUserInAnyGroup(userModel.getUserInAnyGroup());
			userData.setUserInAnySession(true);
			userData.setResetPasswordToken(userModel.getResetPasswordToken());
			userData.setVerificationCode(userModel.getVerificationCode());

			userManagementService.updateUserInAnySession(userData);

			// sendModifiedToSessionEmail(userData, devURl);

		}

		String previousUserIdString = previousSessionData.getUserIds();
		String[] previousUserIdArray = previousUserIdString.split(",");
		boolean found = true;
		// int in = 0;

		for (String id : userIdArray) {

			for (int i = 0; i < previousUserIdArray.length; i++) {
				if (id.equals(previousUserIdArray[i])) {
					// in = i;
					found = false;
					break;
				}
			}

			if (!found) {
				UserData userModel = userManagementService.findWithid(Long.parseLong(id));

				UserData userData = new UserData();

				userData.setId(userModel.getId());
				userData.setUserName(userModel.getUserName());
				userData.setEmail(userModel.getEmail());
				userData.setPassword(userModel.getPassword());
				userData.setImageKey(userModel.getImageKey());
				userData.setRoles(userModel.getRoles());
				userData.setCountryID(userModel.getCountryID());
				userData.setRegionID(userModel.getRegionID());
				userData.setStateID(userModel.getStateID());
				userData.setCityID(userModel.getCityID());
				userData.setUserStatus(userModel.getUserStatus());
				userData.setUserInAnyGroup(userModel.getUserInAnyGroup());
				userData.setUserInAnySession(false);
				userData.setResetPasswordToken(userModel.getResetPasswordToken());
				userData.setVerificationCode(userModel.getVerificationCode());

				userManagementService.updateUserInAnySession(userData);

				// sendSessionEndedEmail(userData,devURl);
			}

		}

		// updating session data
		GameSessionUserData finalResponse = sessionRepository.save(session);

		return finalResponse;

	}

	@Override
	public Boolean checkIfUserIsFreeForSession(UserData member, String newSessioStartTime) {
		String time = newSessioStartTime + ":00z";
		Instant instant = Instant.parse(time);
		LocalDateTime resultTime = LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId()));
		boolean userIsFreeForSession = true;

		// getting all session details.
		List<GameSessionUserData> sessionList = findAll();

		// iterating each session.
		for (GameSessionUserData session : sessionList) {

			LocalDateTime userFreeTime = session.getEndDateAndTime();
			String userIdString = session.getUserIds();
			String[] userIdArray = userIdString.split(",");

			// iterating group
			for (String id : userIdArray) {

				int memFromDbId = Integer.valueOf(id);

				long val = member.getId();
				int newMemId = (int) val;

				if (memFromDbId == newMemId) {
					// checking
					boolean userIsFree = resultTime.isAfter(userFreeTime);
					if (!userIsFree) {
						userIsFreeForSession = false;
					}
				}

			}
		}
		return userIsFreeForSession;

	}

	@Override
	public Boolean checkIfUserIsFreeForSession(UserData member, Long sessionId, String newSessioStartTime) {
		String time = newSessioStartTime + ":00z";
		Instant instant = Instant.parse(time);
		LocalDateTime resultTime = LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId()));
		boolean userIsFreeForSession = true;

		// getting all session details.
		List<GameSessionUserData> sessionList = findAll();

		// iterating each session.
		for (GameSessionUserData session : sessionList) {

			Long sessionIdDB = session.getId();
			// skipping if same session.
			if (!sessionIdDB.equals(sessionId)) {

				LocalDateTime userFreeTime = session.getEndDateAndTime();
				String userIdString = session.getUserIds();
				String[] userIdArray = userIdString.split(",");

				// iterating group
				for (String id : userIdArray) {

					int memFromDbId = Integer.valueOf(id);

					long val = member.getId();
					int newMemId = (int) val;

					if (memFromDbId == newMemId) {
						// checking
						boolean userIsFree = resultTime.isAfter(userFreeTime);
						if (!userIsFree) {
							userIsFreeForSession = false;
						}
					}

				}
			}
		}
		return userIsFreeForSession;
	}

	// private

	// private
	private void sendAddedToSessionEmail(UserData user, String siteURL) {

		try {

			String subject = "Added to Session :: Gamification.";
			String content = "Dear [[name]],<br>" + "<br><br><br>You have been added to a new session in Gamification."
					+ "<br>For more details regarding sessions or gamification kindly contact admin or login. "
					+ "<h3><a href=\"[[URL]]\" target=\"_self\"><br>Get Gamified</a></h3>" + "Regards,<br>"
					+ "ZIS-GAME";
			content = content.replace("[[name]]", user.getUserName());
			content = content.replace("[[URL]]", siteURL);

			GamificationMailContents mail = new GamificationMailContents(user.getEmail(), subject, content);
			asynchronousWorker.sendMail(mail);

		} catch (InterruptedException | MessagingException | UnsupportedEncodingException e) {
			System.out.println(e);
		}

	}

	// private
	private void sendModifiedToSessionEmail(UserData user, String siteURL) {

		try {

			String subject = "Session Modified :: Gamification.";
			String content = "Dear [[name]],<br>" + "<br><br><br>Your session has been modified in Gamification."
					+ "<br>For more details regarding sessions or gamification kindly contact admin or login. "
					+ "<h3><a href=\"[[URL]]\" target=\"_self\"><br>Get Gamified</a></h3>" + "Regards,<br>"
					+ "ZIS-GAME";
			content = content.replace("[[name]]", user.getUserName());
			content = content.replace("[[URL]]", siteURL);

			GamificationMailContents mail = new GamificationMailContents(user.getEmail(), subject, content);
			asynchronousWorker.sendMail(mail);

		} catch (InterruptedException | MessagingException | UnsupportedEncodingException e) {
			System.out.println(e);
		}

	}

	// private
	private void sendStatusChangeEmail(UserData user, String siteURL) {

		try {

			String subject = "Game session modified :: Gamification.";
			String content = "Dear [[name]],<br>"
					+ "<br><br><br>Your Game session status has been changed for Gamification."
					+ "<br>For more details regarding sessions or gamification kindly contact admin or login. "
					+ "<h3><a href=\"[[URL]]\" target=\"_self\"><br>Get Gamified</a></h3>" + "Regards,<br>"
					+ "ZIS-GAME";
			content = content.replace("[[name]]", user.getUserName());
			content = content.replace("[[URL]]", siteURL);

			GamificationMailContents mail = new GamificationMailContents(user.getEmail(), subject, content);
			asynchronousWorker.sendMail(mail);

		} catch (InterruptedException | MessagingException | UnsupportedEncodingException e) {
			System.out.println(e);
		}

	}

	// private
	private void sendSessionEndedEmail(UserData user, String siteURL) {

		try {

			String subject = "Game session Completed :: Gamification.";
			String content = "Dear [[name]],<br>"
					+ "<br><br><br>Your Game session has been ended or expired for Gamification."
					+ "<br>For more details regarding sessions or gamification kindly contact admin or login. "
					+ "<h3><a href=\"[[URL]]\" target=\"_self\"><br>Get Gamified</a></h3>" + "Regards,<br>"
					+ "ZIS-GAME";
			content = content.replace("[[name]]", user.getUserName());
			content = content.replace("[[URL]]", siteURL);

			GamificationMailContents mail = new GamificationMailContents(user.getEmail(), subject, content);
			asynchronousWorker.sendMail(mail);

		} catch (InterruptedException | MessagingException | UnsupportedEncodingException e) {
			System.out.println(e);
		}

	}
}
