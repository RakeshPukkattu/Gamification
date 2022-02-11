package com.zisbv.gamification.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zisbv.gamification.entities.AdminComponents;

@Repository("adminComponentsRepository")
public interface AdminComponentsRepository extends JpaRepository<AdminComponents, Long> {

	@Query("SELECT a FROM AdminComponents a WHERE a.id = ?1")
	public AdminComponents getAdminComponentsWithID(Long id);
	
	@Query("SELECT a FROM AdminComponents a WHERE a.title = ?1")
	public AdminComponents getAdminComponentsWithTitle(String title);
}
