package com.zisbv.gamification.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zisbv.gamification.entities.GameData;

@Repository("gameDataRepository")
public interface GameDataRepository extends JpaRepository<GameData, Long> {

	@Query("SELECT a FROM GameData a WHERE a.id = ?1")
	public GameData getGameDataWithID(Long id);

	@Query("SELECT a FROM GameData a WHERE a.gameName = ?1")
	public GameData getGameDataWithName(String gameName);
}
