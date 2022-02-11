package com.zisbv.gamification.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zisbv.gamification.entities.UserData;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<UserData, Integer> {

	@Query("SELECT u FROM UserData u WHERE u.email = ?1")
	public UserData getUserWithEmail(String email);

	@Query("SELECT u.password FROM UserData u WHERE u.email = ?1")
	public String getPasswordWithEmail(String email);

	@Query("SELECT u.userStatus FROM UserData u WHERE u.email = ?1")
	public Boolean getStatusWithEmail(String email);

	@Query("SELECT u FROM UserData u WHERE u.verificationCode = ?1")
	public UserData findByVerificationCode(String code);

	public UserData findByResetPasswordToken(String token);

	public UserData findById(Long id);

	public UserData findByEmail(String email);

	public Boolean existsByEmail(String email);
}
