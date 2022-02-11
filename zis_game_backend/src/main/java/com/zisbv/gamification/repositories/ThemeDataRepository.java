package com.zisbv.gamification.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zisbv.gamification.entities.ThemeData;

@Repository("themeDataRepository")
public interface ThemeDataRepository extends JpaRepository<ThemeData, Long> {

	@Query("SELECT a FROM ThemeData a WHERE a.id = ?1")
	public ThemeData getThemeDataWithID(Long id);

	@Query("SELECT a FROM ThemeData a WHERE a.themeName = ?1")
	public ThemeData getThemeDataWithName(String themeName);
}
