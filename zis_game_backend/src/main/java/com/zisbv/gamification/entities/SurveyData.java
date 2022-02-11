package com.zisbv.gamification.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SurveyData {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String surveyName;

	private Long countryID;

	private String questionArrayID;

	private Boolean enableDisableStatus;

	private Boolean isAssigned;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSurveyName() {
		return surveyName;
	}

	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}

	public Long getCountryID() {
		return countryID;
	}

	public void setCountryID(Long countryID) {
		this.countryID = countryID;
	}

	public String getQuestionArrayID() {
		return questionArrayID;
	}

	public void setQuestionArrayID(String questionArrayID) {
		this.questionArrayID = questionArrayID;
	}

	public Boolean getEnableDisableStatus() {
		return enableDisableStatus;
	}

	public void setEnableDisableStatus(Boolean enableDisableStatus) {
		this.enableDisableStatus = enableDisableStatus;
	}

	public Boolean getIsAssigned() {
		return isAssigned;
	}

	public void setIsAssigned(Boolean isAssigned) {
		this.isAssigned = isAssigned;
	}

}
