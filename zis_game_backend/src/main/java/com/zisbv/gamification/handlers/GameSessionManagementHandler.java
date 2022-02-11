package com.zisbv.gamification.handlers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.stream.Collectors;

import com.zisbv.gamification.dto.GameSessionUserDto;
import com.zisbv.gamification.entities.GameSessionGroupData;
import com.zisbv.gamification.entities.GameSessionUserData;

public class GameSessionManagementHandler {

	public GameSessionGroupData updateEnableDisable(GameSessionGroupData existingSession, Boolean status) {

		GameSessionGroupData session = new GameSessionGroupData();

		session.setId(existingSession.getId());
		session.setSessionName(existingSession.getSessionName());
		session.setCountryID(existingSession.getCountryID());
		session.setStartDateAndTime(existingSession.getStartDateAndTime());
		session.setEndDateAndTime(existingSession.getEndDateAndTime());
		session.setGroups(existingSession.getGroups());
		session.setSessionCurrentStatus(existingSession.getSessionCurrentStatus());
		session.setSessionEnableDisable(status);

		return session;
	}

	public GameSessionUserData updateEnableDisable(GameSessionUserData existingSession, Boolean status) {

		GameSessionUserData session = new GameSessionUserData();

		session.setId(existingSession.getId());
		session.setSessionName(existingSession.getSessionName());
		session.setCountryID(existingSession.getCountryID());
		session.setStartDateAndTime(existingSession.getStartDateAndTime());
		session.setEndDateAndTime(existingSession.getEndDateAndTime());
		session.setUserIds(existingSession.getUserIds());
		session.setSessionCurrentStatus(existingSession.getSessionCurrentStatus());
		session.setSessionEnableDisable(status);
		session.setLast_modified(new Date());

		return session;
	}

	public GameSessionGroupData updateCurrentSessionStatus(GameSessionGroupData existingSession, String status) {

		GameSessionGroupData session = new GameSessionGroupData();

		session.setId(existingSession.getId());
		session.setSessionName(existingSession.getSessionName());
		session.setCountryID(existingSession.getCountryID());
		session.setStartDateAndTime(existingSession.getStartDateAndTime());
		session.setEndDateAndTime(existingSession.getEndDateAndTime());
		session.setGroups(existingSession.getGroups());
		session.setSessionCurrentStatus(status);
		session.setSessionEnableDisable(existingSession.getSessionEnableDisable());

		return session;
	}

	public GameSessionUserData updateCurrentSessionStatus(GameSessionUserData existingSession, String status) {

		GameSessionUserData session = new GameSessionUserData();

		session.setId(existingSession.getId());
		session.setSessionName(existingSession.getSessionName());
		session.setCountryID(existingSession.getCountryID());
		session.setStartDateAndTime(existingSession.getStartDateAndTime());
		session.setEndDateAndTime(existingSession.getEndDateAndTime());
		session.setUserIds(existingSession.getUserIds());
		session.setSessionCurrentStatus(status);
		session.setSessionEnableDisable(existingSession.getSessionEnableDisable());
		session.setLast_modified(new Date());

		return session;
	}

	public GameSessionUserData modifySession(GameSessionUserDto dataFromUser, GameSessionUserData existingSession,
			Long countryID) {

		// new changes for response
		GameSessionUserData newSession = new GameSessionUserData();
		newSession.setId(existingSession.getId());

		// if name is not given.
		if (dataFromUser.getSessionName() != null) {
			newSession.setSessionName(dataFromUser.getSessionName());
		} else {
			newSession.setSessionName(existingSession.getSessionName());
		}

		// country
		if (countryID != null) {
			newSession.setCountryID(countryID);
		} else {
			newSession.setCountryID(existingSession.getCountryID());
		}

		// if members id is not given
		if (dataFromUser.getUserIds() != null) {
			String usersString = dataFromUser.getUserIds().stream().map(Object::toString)
					.collect(Collectors.joining(","));
			newSession.setUserIds(usersString);
		} else {
			newSession.setUserIds(existingSession.getUserIds());
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

		newSession.setLast_modified(new Date());
		return newSession;
	}

}
