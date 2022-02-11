package com.zisbv.gamification.models;

import java.util.ArrayList;
import java.util.List;

public class SurveyResponseModel {

	private List<SurveyModel> surveys = new ArrayList<SurveyModel>();

	public List<SurveyModel> getSurveys() {
		return surveys;
	}

	public void setSurveys(List<SurveyModel> surveys) {
		this.surveys = surveys;
	}

	@Override
	public String toString() {
		return "SurveyResponseModel [surveys=" + surveys + "]";
	}

}
