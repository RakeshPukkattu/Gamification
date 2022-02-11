package com.zisbv.gamification.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zisbv.gamification.entities.SurveyDropDownQuestionData;

@Repository("surveyDropDownQuestionRepository")
public interface SurveyDropDownQuestionRepository extends JpaRepository<SurveyDropDownQuestionData, Long> {

	@Query("SELECT s FROM SurveyDropDownQuestionData s WHERE s.id = ?1")
	public SurveyDropDownQuestionData getSurveyQuestionWithID(Long id);

	@Query("SELECT s FROM SurveyDropDownQuestionData s WHERE s.sID = ?1")
	public SurveyDropDownQuestionData getSurveyQuestionWithSID(String sID);

}
