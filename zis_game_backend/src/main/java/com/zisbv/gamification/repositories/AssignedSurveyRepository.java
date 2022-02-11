package com.zisbv.gamification.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zisbv.gamification.entities.AssignedSurveyData;

@Repository("assignedSurveyRepository")
public interface AssignedSurveyRepository extends JpaRepository<AssignedSurveyData, Long> {

	@Query("SELECT a FROM AssignedSurveyData a WHERE a.surveyID = ?1")
	public AssignedSurveyData getAssignedSurveyWithSurveyID(Long surveyID);

	@Query("SELECT a FROM AssignedSurveyData a WHERE a.userID = ?1")
	public AssignedSurveyData getAssignedSurveyWithUserID(Long userID);

	@Query("SELECT a FROM AssignedSurveyData a WHERE a.groupID = ?1")
	public AssignedSurveyData getAssignedSurveyWithGroupID(Long id);
}
