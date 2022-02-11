package com.zisbv.gamification.models;

import java.util.ArrayList;
import java.util.List;

public class SurveyModel {

	private Long surveyId;

	private String surveyName;

	private String countryName;

	private Boolean enableDisableStatus;

	private Boolean isAssigned;

	private List<QuestionDataResponse> questions = new ArrayList<QuestionDataResponse>();

	public Long getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(Long surveyId) {
		this.surveyId = surveyId;
	}

	public String getSurveyName() {
		return surveyName;
	}

	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
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

	public List<QuestionDataResponse> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QuestionDataResponse> questions) {
		this.questions = questions;
	}

	@Override
	public String toString() {
		return "SurveyModel [surveyId=" + surveyId + ", surveyName=" + surveyName + ", countryName=" + countryName
				+ ", enableDisableStatus=" + enableDisableStatus + ", isAssigned=" + isAssigned + ", questions="
				+ questions + "]";
	}

}
