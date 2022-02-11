package com.zisbv.gamification.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.zisbv.gamification.entities.IndustryData;

public interface IndustryDataRepository extends JpaRepository<IndustryData, Long> {

	@Query("SELECT i FROM IndustryData i WHERE i.id = ?1")
	public IndustryData getIndustryWithID(Long id);

	@Query("SELECT i FROM IndustryData i WHERE i.industryName = ?1")
	public IndustryData getIndustryWithName(String industryName);
}
