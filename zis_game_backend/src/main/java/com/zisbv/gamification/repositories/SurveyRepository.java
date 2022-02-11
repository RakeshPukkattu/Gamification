package com.zisbv.gamification.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zisbv.gamification.entities.SurveyData;

@Repository("surveyRepository")
public interface SurveyRepository extends JpaRepository<SurveyData, Long> {

	@Query("SELECT s FROM SurveyData s WHERE s.id = ?1")
	public SurveyData getSurveyWithID(Long id);

	@Query("SELECT s FROM SurveyData s WHERE s.surveyName = ?1")
	public SurveyData getSurveyWithName(String surveyName);
}
