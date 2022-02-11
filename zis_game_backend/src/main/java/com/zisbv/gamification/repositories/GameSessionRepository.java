package com.zisbv.gamification.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zisbv.gamification.entities.GameSessionGroupData;

@Repository("gameSessionRepository")
public interface GameSessionRepository extends JpaRepository<GameSessionGroupData, Integer> {

	@Query("SELECT s FROM GameSessionGroupData s WHERE s.id = ?1")
	public GameSessionGroupData getSessionWithID(Long id);

	@Query("SELECT s FROM GameSessionGroupData s WHERE s.sessionName = ?1")
	public GameSessionGroupData getSessionWithName(String sessionName);

}
