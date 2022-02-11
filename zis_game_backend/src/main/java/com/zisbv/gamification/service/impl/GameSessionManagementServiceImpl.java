package com.zisbv.gamification.service.impl;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.zisbv.gamification.config.AsynchronousMailSender;
import com.zisbv.gamification.dto.GameSessionGroupDto;
import com.zisbv.gamification.entities.Country;
import com.zisbv.gamification.entities.GameSessionGroupData;
import com.zisbv.gamification.entities.GroupData;
import com.zisbv.gamification.entities.UserData;
import com.zisbv.gamification.models.GameSessionModel;
import com.zisbv.gamification.models.GameSessionResponseModel;
import com.zisbv.gamification.models.GamificationMailContents;
import com.zisbv.gamification.models.GroupInSessionModel;
import com.zisbv.gamification.models.UserInGroupSessionModel;
import com.zisbv.gamification.repositories.CountryRepository;
import com.zisbv.gamification.repositories.GameSessionRepository;
import com.zisbv.gamification.service.GameSessionManagementService;
import com.zisbv.gamification.service.GroupManagementService;
import com.zisbv.gamification.service.UserManagementService;

@Service(value = "gameSessionManagementService")
public class GameSessionManagementServiceImpl implements GameSessionManagementService {

	@Value("${dev.url}")
	String devURl;

	@Autowired
	private AsynchronousMailSender asynchronousWorker;

	@Autowired
	GameSessionRepository sessionRepository;

	@Autowired
	UserManagementService userManagementService;

	@Autowired
	CountryRepository countryRepository;

	@Autowired
	GroupManagementService groupManagementService;

	@Override
	public List<GameSessionGroupData> findAll() {
		List<GameSessionGroupData> list = new ArrayList<>();
		sessionRepository.findAll().iterator().forEachRemaining(list::add);
		return list;
	}

	@Override
	public GameSessionResponseModel getSessionResponseModel() {

		// final response
		GameSessionResponseModel gameSessionResponseModel = new GameSessionResponseModel();
		List<GameSessionModel> responseModel = new ArrayList<GameSessionModel>();

		// Data from db
		List<GameSessionGroupData> dataFromDb = findAll();

		// Iterating data from db
		for (GameSessionGroupData gameSession : dataFromDb) {

			GameSessionModel session = new GameSessionModel();
			List<GroupInSessionModel> groupInSessionList = new ArrayList<GroupInSessionModel>();

			session.setSessionId(gameSession.getId());
			session.setSessionName(gameSession.getSessionName());

			Country countryData = countryRepository.getCountryWithID(gameSession.getCountryID());
			session.setCountryName(countryData.getCountryName());

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String formattedStartDateTime = gameSession.getStartDateAndTime().format(formatter);
			session.setStartTime(formattedStartDateTime);

			String formattedEndDateTime = gameSession.getEndDateAndTime().format(formatter);
			session.setEndTime(formattedEndDateTime);

			session.setSessionCurrentStatus(gameSession.getSessionCurrentStatus());
			session.setSessionEnableDisable(gameSession.getSessionEnableDisable());

			// new
			List<GroupData> sessionGroups = gameSession.getGroups();
			for (GroupData sessionGroup : sessionGroups) {

				GroupInSessionModel groupInSessionModel = new GroupInSessionModel();

				// Adding to response
				groupInSessionModel.setGroupId(sessionGroup.getGroupID());
				groupInSessionModel.setGroupName(sessionGroup.getGroupName());

				// Getting group members
				List<UserData> groupMembers = sessionGroup.getMembers();
				List<UserInGroupSessionModel> userInGroupList = new ArrayList<UserInGroupSessionModel>();
				for (UserData groupMember : groupMembers) {

					UserInGroupSessionModel userInGroup = new UserInGroupSessionModel();

					userInGroup.setId(groupMember.getId());
					userInGroup.setUserName(groupMember.getUserName());
					userInGroup.setUserEmail(groupMember.getEmail());
					userInGroup.setAvatharKey(groupMember.getImageKey());

					userInGroupList.add(userInGroup);
				}

				// Adding to group to response object
				groupInSessionModel.setGroupMembers(userInGroupList);

				// Adding group to response list
				groupInSessionList.add(groupInSessionModel);

			}
			// end

			session.setGetGroupsInSession(groupInSessionList);

			responseModel.add(session);

			gameSessionResponseModel.setSessions(responseModel);
		}
		return gameSessionResponseModel;
	}

	@Override
	public GameSessionResponseModel getSessionResponseModelGroupedbyCountry(String country) {

		// final response
		GameSessionResponseModel gameSessionResponseModel = new GameSessionResponseModel();
		List<GameSessionModel> responseModel = new ArrayList<GameSessionModel>();

		// Data from db
		List<GameSessionGroupData> dataFromDb = findAll();

		// Iterating data from db
		for (GameSessionGroupData gameSession : dataFromDb) {

			Long countryIDDB = gameSession.getCountryID();
			Country countryDataDB = countryRepository.getCountryWithID(countryIDDB);
			String countryNameDB = countryDataDB.getCountryName();
			if (countryNameDB.equalsIgnoreCase(country)) {

				GameSessionModel session = new GameSessionModel();
				List<GroupInSessionModel> groupInSessionList = new ArrayList<GroupInSessionModel>();

				session.setSessionId(gameSession.getId());
				session.setSessionName(gameSession.getSessionName());

				Country countryData = countryRepository.getCountryWithID(gameSession.getCountryID());
				session.setCountryName(countryData.getCountryName());

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				String formattedStartDateTime = gameSession.getStartDateAndTime().format(formatter);
				session.setStartTime(formattedStartDateTime);

				String formattedEndDateTime = gameSession.getEndDateAndTime().format(formatter);
				session.setEndTime(formattedEndDateTime);

				session.setSessionCurrentStatus(gameSession.getSessionCurrentStatus());
				session.setSessionEnableDisable(gameSession.getSessionEnableDisable());

				// new
				List<GroupData> sessionGroups = gameSession.getGroups();
				for (GroupData sessionGroup : sessionGroups) {

					GroupInSessionModel groupInSessionModel = new GroupInSessionModel();

					// Adding to response
					groupInSessionModel.setGroupId(sessionGroup.getGroupID());
					groupInSessionModel.setGroupName(sessionGroup.getGroupName());

					// Getting group members
					List<UserData> groupMembers = sessionGroup.getMembers();
					List<UserInGroupSessionModel> userInGroupList = new ArrayList<UserInGroupSessionModel>();
					for (UserData groupMember : groupMembers) {
						UserInGroupSessionModel userInGroup = new UserInGroupSessionModel();

						userInGroup.setId(groupMember.getId());
						userInGroup.setUserName(groupMember.getUserName());
						userInGroup.setUserEmail(groupMember.getEmail());
						userInGroup.setAvatharKey(groupMember.getImageKey());

						userInGroupList.add(userInGroup);
					}

					// Adding to group to response object
					groupInSessionModel.setGroupMembers(userInGroupList);

					// Adding group to response list
					groupInSessionList.add(groupInSessionModel);
				}
				// end

				session.setGetGroupsInSession(groupInSessionList);

				responseModel.add(session);

				gameSessionResponseModel.setSessions(responseModel);
			}
		}

		return gameSessionResponseModel;
	}

	@Override
	public GameSessionGroupData findWithid(Long id) {
		GameSessionGroupData dataFromDB = sessionRepository.getSessionWithID(id);
		return dataFromDB;
	}

	@Override
	public GameSessionModel getSession(Long id) {

		// Data from DataBase.
		GameSessionGroupData dataFromDb = findWithid(id);

		GameSessionModel response = new GameSessionModel();
		response.setSessionId(dataFromDb.getId());
		response.setSessionName(dataFromDb.getSessionName());
		
		Country countryData = countryRepository.getCountryWithID(dataFromDb.getCountryID());
		response.setCountryName(countryData.getCountryName());
		
		response.setStartTime(dataFromDb.getStartDateAndTime().toString());
		response.setEndTime(dataFromDb.getEndDateAndTime().toString());
		response.setSessionCurrentStatus(dataFromDb.getSessionCurrentStatus());
		response.setSessionEnableDisable(dataFromDb.getSessionEnableDisable());

		// groups in session
		response.setGetGroupsInSession(getGroupInSession(dataFromDb.getId()));

		return response;

	}

	@Override
	public List<GroupInSessionModel> getGroupInSession(Long id) {

		// Final response
		List<GroupInSessionModel> groupInSessionList = new ArrayList<GroupInSessionModel>();

		GameSessionGroupData gameSession = sessionRepository.getSessionWithID(id);

		// new
		List<GroupData> sessionGroups = gameSession.getGroups();
		for (GroupData sessionGroup : sessionGroups) {

			GroupInSessionModel groupInSessionModel = new GroupInSessionModel();

			// Adding to response
			groupInSessionModel.setGroupId(sessionGroup.getGroupID());
			groupInSessionModel.setGroupName(sessionGroup.getGroupName());

			// Getting group members
			List<UserData> groupMembers = sessionGroup.getMembers();
			List<UserInGroupSessionModel> userInGroupList = new ArrayList<UserInGroupSessionModel>();
			for (UserData groupMember : groupMembers) {
				UserInGroupSessionModel userInGroup = new UserInGroupSessionModel();

				userInGroup.setId(groupMember.getId());
				userInGroup.setUserName(groupMember.getUserName());
				userInGroup.setUserEmail(groupMember.getEmail());
				userInGroup.setAvatharKey(groupMember.getImageKey());

				userInGroupList.add(userInGroup);
			}

			// Adding to group to response object
			groupInSessionModel.setGroupMembers(userInGroupList);

			// Adding group to response list
			groupInSessionList.add(groupInSessionModel);
		}
		// end

		// Returning response
		return groupInSessionList;
	}

	@Override
	public GameSessionGroupData findWithName(String name) {
		GameSessionGroupData data = sessionRepository.getSessionWithName(name);
		return data;
	}

	@Override
	public void createGameSession(GameSessionGroupData data, Long countryID) {

		data.setCountryID(countryID);
		// Checking for existing session.
		GameSessionGroupData existingSession = findWithid(data.getId());
		if (existingSession == null) {
			sessionRepository.save(data);

			// update groupInAnySession to true
			List<GroupData> sessionGroups = data.getGroups();
			for (GroupData sessionGroup : sessionGroups) {

				GroupData groupData = new GroupData();
				groupData.setGroupID(sessionGroup.getGroupID());
				groupData.setGroupName(sessionGroup.getGroupName());
				groupData.setGroupStatus(sessionGroup.getGroupStatus());
				groupData.setCountryID(sessionGroup.getCountryID());
				groupData.setGroupInAnySession(true);
				groupData.setMembers(sessionGroup.getMembers());

				List<UserData> groupMembers = sessionGroup.getMembers();
				for (UserData groupmember : groupMembers) {
					
					UserData user = new UserData();
					user.setId(groupmember.getId());
					user.setUserName(groupmember.getUserName());
					user.setEmail(groupmember.getEmail());
					user.setPassword(groupmember.getPassword());
					user.setImageKey(groupmember.getImageKey());
					user.setRoles(groupmember.getRoles());
					user.setCountryID(groupmember.getCountryID());
					user.setRegionID(groupmember.getRegionID());
					user.setCityID(groupmember.getCityID());
					user.setStateID(groupmember.getStateID());
					user.setUserStatus(groupmember.getUserStatus());
					user.setUserInAnyGroup(groupmember.getUserInAnyGroup());
					user.setUserInAnySession(true);
					user.setResetPasswordToken(groupmember.getResetPasswordToken());
					user.setVerificationCode(groupmember.getVerificationCode());
					user.setGroups(groupmember.getGroups());
					user.setUserSessions(groupmember.getUserSessions());					

					userManagementService. updateOtherStatus(user);
				}

				groupManagementService.updateGroupInAnySession(groupData);

				// sending notification mails to new group members
				sendAddedToSessionEmail(groupData, devURl);
			}

		}
	}

	@Override
	public void updateStatus(GameSessionGroupData session) {

		sessionRepository.save(session);

		// new
		List<GroupData> sessionGroups = session.getGroups();
		for (GroupData sessionGroup : sessionGroups) {

			List<UserData> groupMembers = sessionGroup.getMembers();
			for (UserData groupMember : groupMembers) {

				// sending notification mail as user is now part of session.
				sendStatusChangeEmail(groupMember, devURl);
			}
		}
		// end
	}

	@Override
	public void updateGameSession(GameSessionGroupDto dataFromUser, GameSessionGroupData existingSession,
			Long inputCountryID) {

		// update groupInAnySession and userInAnySession to false.
		// getting previous session data
		GameSessionGroupData previousSessionData = sessionRepository.getSessionWithID(existingSession.getId());
		List<GroupData> previousGroupSessions = previousSessionData.getGroups();
		for (GroupData previousGroupSession : previousGroupSessions) {

			previousGroupSession.setGroupInAnySession(false);

			List<UserData> groupMembers = previousGroupSession.getMembers();
			for (UserData groupMember : groupMembers) {
				groupMember.setUserInAnySession(false);
			}
			sendSessionEndedEmail(previousGroupSession, devURl);
		}

		// new changes
		GameSessionGroupData newSession = new GameSessionGroupData();
		newSession.setId(existingSession.getId());

		// if name is not given.
		if (dataFromUser.getSessionName() != null) {
			newSession.setSessionName(dataFromUser.getSessionName());
		} else {
			newSession.setSessionName(existingSession.getSessionName());
		}

		// country.
		if (inputCountryID != null) {
			newSession.setCountryID(inputCountryID);
		} else {

			newSession.setCountryID(existingSession.getCountryID());
		}

		// if members id is not given
		if (dataFromUser.getGroupIds() != null) {
			for (String groupID : dataFromUser.getGroupIds()) {
				GroupData sessionGroup = groupManagementService.findWithid(Long.parseLong(groupID));
				newSession.getGroups().add(sessionGroup);
			}

		} else {
			newSession.setGroups(existingSession.getGroups());
		}

		// Enable-Disable
		if (existingSession.getSessionEnableDisable() != null) {
			newSession.setSessionEnableDisable(existingSession.getSessionEnableDisable());
		}

		// Current status
		newSession.setSessionCurrentStatus(existingSession.getSessionCurrentStatus());

		// Start time
		if (dataFromUser.getStartDateandTime() != null) {
			String finalStartT = dataFromUser.getStartDateandTime() + ":00z";
			Instant instant = Instant.parse(finalStartT);
			LocalDateTime result = LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId()));
			newSession.setStartDateAndTime(result);
		} else {
			newSession.setStartDateAndTime(existingSession.getStartDateAndTime());
		}

		// End time
		if (dataFromUser.getEndDateandTime() != null) {
			String finalEndT = dataFromUser.getEndDateandTime() + ":00z";
			Instant instant = Instant.parse(finalEndT);
			LocalDateTime result = LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId()));
			newSession.setEndDateAndTime(result);
		} else {
			newSession.setEndDateAndTime(existingSession.getEndDateAndTime());
		}

		// save
		// updating session data
		sessionRepository.save(newSession);

		// update groupInAnySession to true
		List<GroupData> sessionGroups = newSession.getGroups();
		for (GroupData sessionGroup : sessionGroups) {

			GroupData groupData = new GroupData();
			groupData.setGroupID(sessionGroup.getGroupID());
			groupData.setGroupName(sessionGroup.getGroupName());
			groupData.setGroupStatus(sessionGroup.getGroupStatus());
			groupData.setCountryID(sessionGroup.getCountryID());
			groupData.setGroupInAnySession(true);
			groupData.setMembers(sessionGroup.getMembers());

			List<UserData> groupMembers = sessionGroup.getMembers();
			for (UserData groupMember : groupMembers) {
				
				UserData user = new UserData();
				user.setId(groupMember.getId());
				user.setUserName(groupMember.getUserName());
				user.setEmail(groupMember.getEmail());
				user.setPassword(groupMember.getPassword());
				user.setImageKey(groupMember.getImageKey());
				user.setRoles(groupMember.getRoles());
				user.setCountryID(groupMember.getCountryID());
				user.setRegionID(groupMember.getRegionID());
				user.setCityID(groupMember.getCityID());
				user.setStateID(groupMember.getStateID());
				user.setUserStatus(groupMember.getUserStatus());
				user.setUserInAnyGroup(groupMember.getUserInAnyGroup());
				user.setUserInAnySession(true);
				user.setResetPasswordToken(groupMember.getResetPasswordToken());
				user.setVerificationCode(groupMember.getVerificationCode());
				user.setGroups(groupMember.getGroups());
				user.setUserSessions(groupMember.getUserSessions());					

				userManagementService. updateOtherStatus(user);
				
			}

			groupManagementService.updateGroupInAnySession(groupData);

			sendAddedToSessionEmail(groupData, devURl);

		}

	}

	@Override
	public Boolean checkIfUserIsFreeForSession(UserData member, String newSessioStartTime) {

		String time = newSessioStartTime + ":00z";
		Instant instant = Instant.parse(time);
		LocalDateTime resultTime = LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId()));
		boolean userIsFreeForSession = true;

		// getting all session details.
		List<GameSessionGroupData> sessionList = findAll();

		// iterating each session.
		for (GameSessionGroupData session : sessionList) {

			LocalDateTime userFreeTime = session.getEndDateAndTime();
			List<GroupData> sessionGroups = session.getGroups();
			for (GroupData sessionGroup : sessionGroups) {
				List<UserData> groupMembers = sessionGroup.getMembers();
				for (UserData groupMember : groupMembers) {

					Long groupMemberID = groupMember.getId();
					Long newMemId = member.getId();

					if (groupMemberID == newMemId) {
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

	@Override
	public Boolean checkIfUserIsFreeForSession(UserData member, Long sessionId, String newSessioStartTime) {

		String time = newSessioStartTime + ":00z";
		Instant instant = Instant.parse(time);
		LocalDateTime resultTime = LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId()));
		boolean userIsFreeForSession = true;

		// getting all session details.
		List<GameSessionGroupData> sessionList = findAll();

		// iterating each session.
		for (GameSessionGroupData session : sessionList) {

			Long sessionIdDB = session.getId();
			// skipping if same session.
			if (!sessionIdDB.equals(sessionId)) {

				LocalDateTime userFreeTime = session.getEndDateAndTime();
				List<GroupData> sessionGroups = session.getGroups();
				for (GroupData sessionGroup : sessionGroups) {
					List<UserData> groupMembers = sessionGroup.getMembers();
					for (UserData groupMember : groupMembers) {

						Long memFromDbId = groupMember.getId();
						Long newMemId = member.getId();

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
		}
		return userIsFreeForSession;
	}

	// private
	private void sendAddedToSessionEmail(GroupData group, String siteURL) {

		try {

			List<UserData> groupMembers = group.getMembers();
			for (UserData groupMember : groupMembers) {

				String subject = "Group added to Session :: Gamification.";
				String content = "Dear [[name]],<br>"
						+ "<br><br><br>Your group - [[group]] have been added to a new session in Gamification."
						+ "<br>For more details regarding groups,sessions or gamification kindly contact admin or login. "
						+ "<h3><a href=\"[[URL]]\" target=\"_self\"><br>Get Gamified</a></h3>" + "Regards,<br>"
						+ "ZIS-GAME";
				content = content.replace("[[name]]", groupMember.getUserName());
				content = content.replace("[[group]]", group.getGroupName());
				content = content.replace("[[URL]]", siteURL);

				GamificationMailContents mail = new GamificationMailContents(groupMember.getEmail(), subject, content);
				asynchronousWorker.sendMail(mail);
			}

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
	private void sendSessionEndedEmail(GroupData group, String siteURL) {

		try {

			List<UserData> groupMembers = group.getMembers();
			for (UserData groupMember : groupMembers) {
				String subject = "Group Session Completed :: Gamification.";
				String content = "Dear [[name]],<br>"
						+ "<br><br><br>Session for your group - [[group]] have been completed in Gamification."
						+ "<br>For more details regarding groups,sessions or gamification kindly contact admin or login. "
						+ "<h3><a href=\"[[URL]]\" target=\"_self\"><br>Get Gamified</a></h3>" + "Regards,<br>"
						+ "ZIS-GAME";
				content = content.replace("[[name]]", groupMember.getUserName());
				content = content.replace("[[group]]", group.getGroupName());
				content = content.replace("[[URL]]", siteURL);

				GamificationMailContents mail = new GamificationMailContents(groupMember.getEmail(), subject, content);
				asynchronousWorker.sendMail(mail);
			}

		} catch (InterruptedException | MessagingException | UnsupportedEncodingException e) {
			System.out.println(e);
		}

	}

}
