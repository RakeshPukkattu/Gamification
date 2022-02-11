package com.zisbv.gamification.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zisbv.gamification.entities.SurveyMMCQQuestionData;

@Repository("surveyMMCQQuestionRepository")
public interface SurveyMMCQQuestionRepository extends JpaRepository<SurveyMMCQQuestionData, Long> {

	@Query("SELECT s FROM SurveyMMCQQuestionData s WHERE s.id = ?1")
	public SurveyMMCQQuestionData getSurveyQuestionWithID(Long id);

	@Query("SELECT s FROM SurveyMMCQQuestionData s WHERE s.sID = ?1")
	public SurveyMMCQQuestionData getSurveyQuestionWithSID(String sID);

}
