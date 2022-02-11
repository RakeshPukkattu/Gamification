package com.zisbv.gamification.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zisbv.gamification.entities.GameSessionUserData;

@Repository("gameSessionUserRepository")
public interface GameSessionUserRepository extends JpaRepository<GameSessionUserData, Integer> {

	@Query("SELECT s FROM GameSessionUserData s WHERE s.id = ?1")
	public GameSessionUserData getSessionWithID(Long id);

	@Query("SELECT s FROM GameSessionUserData s WHERE s.sessionName = ?1")
	public GameSessionUserData getSessionWithName(String sessionName);
}
