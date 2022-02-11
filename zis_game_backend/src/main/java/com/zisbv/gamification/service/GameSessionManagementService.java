package com.zisbv.gamification.service;

import java.util.List;

import com.zisbv.gamification.dto.GameSessionGroupDto;
import com.zisbv.gamification.entities.GameSessionGroupData;
import com.zisbv.gamification.entities.UserData;
import com.zisbv.gamification.models.GameSessionModel;
import com.zisbv.gamification.models.GameSessionResponseModel;
import com.zisbv.gamification.models.GroupInSessionModel;

public interface GameSessionManagementService {

	// Get all sessions from DataBase.
	List<GameSessionGroupData> findAll();

	// Get sessions as SessionResponseModel.
	GameSessionResponseModel getSessionResponseModel();

	// Get session with id.
	GameSessionGroupData findWithid(Long id);

	// country
	GameSessionResponseModel getSessionResponseModelGroupedbyCountry(String country);

	// Get individual session as SessionResponseModel.
	GameSessionModel getSession(Long id);

	// Get groups in session with Session_ID.
	List<GroupInSessionModel> getGroupInSession(Long id);

	// Update Enable Disable and Current Status.
	void updateStatus(GameSessionGroupData session);

//	// checks user is part if any session
//	Boolean checkUserInAnySession(GroupMember members);
//
//	// -for modify
//	public Boolean checkUserInAnySession(GroupMember member, Long id);

	// above is
	// new****************************************************************************

	// get session with name.
	GameSessionGroupData findWithName(String name);

	// create session.
	void createGameSession(GameSessionGroupData session, Long countryID);

	// modify
	void updateGameSession(GameSessionGroupDto dataFromUser, GameSessionGroupData existingSession,
			Long inputCountryID);

	// Check if user is free
	Boolean checkIfUserIsFreeForSession(UserData member, String newSessioStartTime);

	// Check if user is free while modify
	Boolean checkIfUserIsFreeForSession(UserData member, Long id, String newSessioStartTime);

}
