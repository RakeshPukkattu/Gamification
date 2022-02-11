package com.zisbv.gamification.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zisbv.gamification.entities.SurveyMCQQuestionData;

@Repository("surveyMCQQuestionsRepository")
public interface SurveyMCQQuestionsRepository extends JpaRepository<SurveyMCQQuestionData, Long> {

	@Query("SELECT s FROM SurveyMCQQuestionData s WHERE s.id = ?1")
	public SurveyMCQQuestionData getSurveyQuestionWithID(Long id);

	@Query("SELECT s FROM SurveyMCQQuestionData s WHERE s.sID = ?1")
	public SurveyMCQQuestionData getSurveyQuestionWithSID(String sID);

}
