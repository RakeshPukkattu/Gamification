package com.zisbv.gamification.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zisbv.gamification.entities.SurveyQuestionData;

@Repository("surveyQuestionDataRepository")
public interface SurveyQuestionDataRepository extends JpaRepository<SurveyQuestionData, Long> {

	@Query("SELECT s FROM SurveyQuestionData s WHERE s.id = ?1")
	public SurveyQuestionData getSurveyQuestionWithID(Long id);

	@Query("SELECT s FROM SurveyQuestionData s WHERE s.surveyName = ?1")
	public SurveyQuestionData getSurveyQuestionWithName(String surveyName);
}
