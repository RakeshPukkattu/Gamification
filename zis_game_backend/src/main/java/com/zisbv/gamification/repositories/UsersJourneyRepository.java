package com.zisbv.gamification.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zisbv.gamification.entities.UsersJourney;

@Repository("usersJourneyRepository")
public interface UsersJourneyRepository extends JpaRepository<UsersJourney, Long> {

	@Query("SELECT u FROM UsersJourney u WHERE u.userID = ?1")
	public UsersJourney getUsersJourneyWithID(Long userID);

}
