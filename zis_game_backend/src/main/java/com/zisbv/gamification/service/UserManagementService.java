package com.zisbv.gamification.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;

import com.zisbv.gamification.dto.SuperAdminDto;
import com.zisbv.gamification.dto.UserDto;
import com.zisbv.gamification.dto.UserRegisterDto;
import com.zisbv.gamification.entities.UserData;
import com.zisbv.gamification.exceptions.UserNotFoundException;
import com.zisbv.gamification.models.AppResponse;
import com.zisbv.gamification.models.UserModel;
import com.zisbv.gamification.models.UserResponseModel;

public interface UserManagementService {

	// user registration(for now learner only).
	void register(UserData user, String siteURL) throws UnsupportedEncodingException, MessagingException;

	// verify email code
	boolean verify(String verificationCode);

	// save new SuperAdmin.
	UserData registerSuperAdmin(SuperAdminDto user);

	// save new Learner.
	public UserData registerLearner(UserData oldUser, UserRegisterDto user);

	// Save new User.
	AppResponse save(UserDto user);

	// get all users
	List<UserData> findAll();

	// get user with email
	UserModel getUserModelbyEmail(String email);
	
	//find one with email
	UserData findOne(String email);

	// get user with id
	UserData findWithid(Long id);

	// get all user as UserResponseModel.
	UserResponseModel getUserResponseModel();
	
	//users based on country.
	UserResponseModel getUserResponseModelGroupedbyCountry(String country);
	
	//Admins
	UserResponseModel getUserResponseModelGroupedbyAdmins();

	// Update password
	void updateUserPassword(UserData newUser);

	// Update status
	void updateUserStatus(UserData user);
	
	 void updateOtherStatus(UserData user);

	// Update user
	void modifyUser(UserDto user,UserData existingUser);
	
	void updateAvathar(UserData updatedData);

	public void updateResetPasswordToken(String token, String email) throws UserNotFoundException;

	public void updateVerifiedUser(String email) throws UserNotFoundException;

	public UserData getByResetPasswordToken(String token);

	public void updatePassword(UserData customer, String newPassword);

	// Update status user in any session.
	UserData updateUserInAnySession(UserData user);

}
