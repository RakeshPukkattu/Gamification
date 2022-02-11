package com.zisbv.gamification.service;

import java.util.List;

import com.zisbv.gamification.dto.GameSessionUserDto;
import com.zisbv.gamification.entities.GameSessionUserData;
import com.zisbv.gamification.entities.UserData;
import com.zisbv.gamification.models.GameSessionUserModel;
import com.zisbv.gamification.models.GameSessionUserResponseModel;
import com.zisbv.gamification.models.UserInGroupSessionModel;

public interface GameSessionManagementUserService {

	// Get all sessions from DataBase for users.
	List<GameSessionUserData> findAll();

	// Get sessions as GameSessionUserResponseModel.
	GameSessionUserResponseModel getSessionUserResponseModel();

	// Get sessions as GameSessionUserResponseModel based on country.
	GameSessionUserResponseModel getUserSessionResponseModelGroupedbyCountry(String country); 
	
	// Get session with id.
	GameSessionUserData findWithid(Long id);

	// Get individual session as SessionResponseModel.
	GameSessionUserModel getSession(Long id);

	// Get users in session with Session_ID.
	List<UserInGroupSessionModel> getuserInSession(Long id);

	// Update Enable Disable and Current Status.
	void updateStatus(GameSessionUserData session);

	// get session with name.
	GameSessionUserData findWithName(String name);

	// create session.
	void createGameSession(GameSessionUserDto session,Long countryID);

	// modify
	GameSessionUserData updateGameUserSession(GameSessionUserData session, GameSessionUserData existingSession);

	// Check if user is free
	Boolean checkIfUserIsFreeForSession(UserData member, String newSessioStartTime);

	// Check if user is free while modify
	Boolean checkIfUserIsFreeForSession(UserData member, Long id, String newSessioStartTime);

}
