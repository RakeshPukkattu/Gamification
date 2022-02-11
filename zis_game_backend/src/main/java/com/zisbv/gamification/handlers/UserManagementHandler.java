package com.zisbv.gamification.handlers;

import com.zisbv.gamification.entities.UserData;
import com.zisbv.gamification.models.UserCredsModel;

public class UserManagementHandler {

	public UserData updateNewPassword(UserData existingUser, String password) {

		UserData user = new UserData();
		user.setId(existingUser.getId());
		user.setUserName(existingUser.getUserName());
		user.setEmail(existingUser.getEmail());
		user.setPassword(password);
		user.setImageKey(existingUser.getImageKey());
		user.setRoles(existingUser.getRoles());
		user.setCountryID(existingUser.getCountryID());
		user.setRegionID(existingUser.getRegionID());
		user.setStateID(existingUser.getStateID());
		user.setCityID(existingUser.getCityID());
		user.setUserStatus(existingUser.getUserStatus());
		user.setUserInAnyGroup(existingUser.getUserInAnyGroup());
		user.setUserInAnySession(existingUser.getUserInAnySession());
		user.setResetPasswordToken(existingUser.getResetPasswordToken());
		user.setVerificationCode(existingUser.getVerificationCode());
		user.setGroups(existingUser.getGroups());
		user.setUserSessions(existingUser.getUserSessions());

		return user;
	}

	public UserData updateStatus(UserData existingUser, Boolean status) {

		UserData user = new UserData();
		user.setId(existingUser.getId());
		user.setUserName(existingUser.getUserName());
		user.setEmail(existingUser.getEmail());
		user.setPassword(existingUser.getPassword());
		user.setImageKey(existingUser.getImageKey());
		user.setRoles(existingUser.getRoles());
		user.setCountryID(existingUser.getCountryID());
		user.setRegionID(existingUser.getRegionID());
		user.setStateID(existingUser.getStateID());
		user.setCityID(existingUser.getCityID());
		user.setUserStatus(status);
		user.setUserInAnyGroup(existingUser.getUserInAnyGroup());
		user.setUserInAnySession(existingUser.getUserInAnySession());
		user.setResetPasswordToken(existingUser.getResetPasswordToken());
		user.setVerificationCode(existingUser.getVerificationCode());
		user.setGroups(existingUser.getGroups());
		user.setUserSessions(existingUser.getUserSessions());

		return user;
	}

	public UserData updateUserImageKey(UserData existingUser, String fileDownloadUri) {

		UserData user = new UserData();
		user.setId(existingUser.getId());
		user.setUserName(existingUser.getUserName());
		user.setEmail(existingUser.getEmail());
		user.setPassword(existingUser.getPassword());
		user.setImageKey(fileDownloadUri);
		user.setRoles(existingUser.getRoles());
		user.setCountryID(existingUser.getCountryID());
		user.setRegionID(existingUser.getRegionID());
		user.setStateID(existingUser.getStateID());
		user.setCityID(existingUser.getCityID());
		user.setUserStatus(existingUser.getUserStatus());
		user.setUserInAnyGroup(existingUser.getUserInAnyGroup());
		user.setUserInAnySession(existingUser.getUserInAnySession());
		user.setResetPasswordToken(existingUser.getResetPasswordToken());
		user.setVerificationCode(existingUser.getVerificationCode());
		user.setGroups(existingUser.getGroups());
		user.setUserSessions(existingUser.getUserSessions());

		return user;
	}

	public UserCredsModel convertToCredsResponseModel(UserData userData) {
		UserCredsModel response = new UserCredsModel();
		response.setId(userData.getId());
		response.setEmailId(userData.getEmail());
		response.setPassword(userData.getPassword());
		response.setImageKey(userData.getImageKey());
		String roleString = userData.getRoles();
		String[] rolesArray = roleString.split(",", 0);
		response.setRoles(rolesArray);
		response.setCountryID(userData.getCountryID());
		return response;
	}

}
