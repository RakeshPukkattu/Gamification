package com.zisbv.gamification.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.zisbv.gamification.entities.GameSessionGroupData;
import com.zisbv.gamification.entities.GameSessionUserData;
import com.zisbv.gamification.entities.GroupData;
import com.zisbv.gamification.entities.UserData;
import com.zisbv.gamification.entities.UsersJourney;
import com.zisbv.gamification.models.AppResponse;
import com.zisbv.gamification.repositories.UsersJourneyRepository;
import com.zisbv.gamification.util.AppConstants;

@Service(value = "usersJourneyService")
public class UsersJourneyService {

	@Autowired
	UsersJourneyRepository usersJourneyRepository;

	public UsersJourney findWithUsersId(Long id) {
		UsersJourney dataFromDB = usersJourneyRepository.getUsersJourneyWithID(id);
		return dataFromDB;
	}

	public void createUsersJourney(Long userID, String role, String playMode) {
		UsersJourney usersJourney = new UsersJourney();

		usersJourney.setUserID(userID);
		usersJourney.setUserRole(role);
		usersJourney.setPlayMode(playMode);
		usersJourney.setJourneyStatus("");

		usersJourneyRepository.save(usersJourney);
	}

	public AppResponse updateJourneyStatus(String journeyStatus, Long userID) {

		UsersJourney usersJourneyData = usersJourneyRepository.getUsersJourneyWithID(userID);
		UsersJourney updatedUsersJourneyData = new UsersJourney();

		updatedUsersJourneyData.setId(usersJourneyData.getId());
		updatedUsersJourneyData.setUserID(usersJourneyData.getUserID());
		updatedUsersJourneyData.setUserRole(usersJourneyData.getUserRole());
		updatedUsersJourneyData.setPlayMode(usersJourneyData.getPlayMode());
		updatedUsersJourneyData.setJourneyStatus(usersJourneyData.getJourneyStatus());

		usersJourneyRepository.save(usersJourneyData);
		return new AppResponse(AppConstants.SUCCESS_CODE, "modified successfully");

	}
	
	public ResponseEntity<?> validateUserSession(String playMode,UserData user){
		
		Date currentDate = new Date();
		LocalDateTime currentLocalDateTime = currentDate.toInstant()
	      .atZone(ZoneId.systemDefault())
	      .toLocalDateTime();
		
		//group sessions
		List<GroupData> userGroups = user.getGroups();	
		for(GroupData group:userGroups) {			
			List<GameSessionGroupData> groupSessions = group.getGroupSessions();			
			if(groupSessions != null) {				
				List<GameSessionGroupData> todaysGroupsSessions = new ArrayList<>();
				for(GameSessionGroupData groupSession:groupSessions) {		
					LocalDateTime sessionStartTime = groupSession.getStartDateAndTime();
					LocalDateTime tempDateTime = sessionStartTime;
					
					long years = sessionStartTime.until( currentLocalDateTime, ChronoUnit.YEARS );
					tempDateTime = tempDateTime.plusYears( years );
					long months = tempDateTime.until( sessionStartTime, ChronoUnit.MONTHS );
					tempDateTime = tempDateTime.plusMonths( months );
					long days = tempDateTime.until( sessionStartTime, ChronoUnit.DAYS );
					tempDateTime = tempDateTime.plusDays( days );
					long hours = tempDateTime.until( sessionStartTime, ChronoUnit.HOURS );
					tempDateTime = tempDateTime.plusHours( hours );
					long minutes = tempDateTime.until( sessionStartTime, ChronoUnit.MINUTES );
					tempDateTime = tempDateTime.plusMinutes( minutes );
					long seconds = tempDateTime.until( sessionStartTime, ChronoUnit.SECONDS );
					
					if(years == 0) {if(months == 0) {if(days == 0) {todaysGroupsSessions.add(groupSession);}}}	
				}
				
				for(GameSessionGroupData todaysGroupSession:todaysGroupsSessions) {
					
					LocalDateTime sessionStartTime = todaysGroupSession.getStartDateAndTime();
					LocalDateTime tempDateTime = sessionStartTime;
					long hours = tempDateTime.until( sessionStartTime, ChronoUnit.HOURS );
					tempDateTime = tempDateTime.plusHours( hours );
					long minutes = tempDateTime.until( sessionStartTime, ChronoUnit.MINUTES );
					tempDateTime = tempDateTime.plusMinutes( minutes );		
					
				}
				}
				}
		// Group Sessions end.
		
		//user sessions
		List<GameSessionUserData> userSessions = user.getUserSessions();
		if(userSessions != null) {
			
			List<GameSessionUserData> todaysSessions = new ArrayList<>();
			for(GameSessionUserData userSession:userSessions) {
				
				LocalDateTime sessionStartTime = userSession.getStartDateAndTime();
				LocalDateTime tempDateTime = sessionStartTime;
				
				long years = sessionStartTime.until( currentLocalDateTime, ChronoUnit.YEARS );
				tempDateTime = tempDateTime.plusYears( years );
				
				long months = tempDateTime.until( sessionStartTime, ChronoUnit.MONTHS );
				tempDateTime = tempDateTime.plusMonths( months );

				long days = tempDateTime.until( sessionStartTime, ChronoUnit.DAYS );
				tempDateTime = tempDateTime.plusDays( days );


				long hours = tempDateTime.until( sessionStartTime, ChronoUnit.HOURS );
				tempDateTime = tempDateTime.plusHours( hours );

				long minutes = tempDateTime.until( sessionStartTime, ChronoUnit.MINUTES );
				tempDateTime = tempDateTime.plusMinutes( minutes );

				long seconds = tempDateTime.until( sessionStartTime, ChronoUnit.SECONDS );
				
				if(years == 0) {
					if(months == 0) {
						if(days == 0) {
								todaysSessions.add(userSession);
						}
					}
				}
				
			}
			
			for(GameSessionUserData sessionToday:todaysSessions) {
				
				LocalDateTime sessionStartTime = sessionToday.getStartDateAndTime();
				LocalDateTime tempDateTime = sessionStartTime;
				
				long hours = tempDateTime.until( sessionStartTime, ChronoUnit.HOURS );
				tempDateTime = tempDateTime.plusHours( hours );

				long minutes = tempDateTime.until( sessionStartTime, ChronoUnit.MINUTES );
				tempDateTime = tempDateTime.plusMinutes( minutes );
				
				
			}
		}

		
		
		return (ResponseEntity<?>) ResponseEntity.ok();
	}

}
