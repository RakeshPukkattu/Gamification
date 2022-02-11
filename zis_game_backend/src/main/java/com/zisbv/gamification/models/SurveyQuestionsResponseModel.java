package com.zisbv.gamification.models;

import java.util.ArrayList;
import java.util.List;

public class SurveyQuestionsResponseModel {

	private Long id;

	private String name;

	private List<QuestionDataResponse> questions = new ArrayList<QuestionDataResponse>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<QuestionDataResponse> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QuestionDataResponse> questions) {
		this.questions = questions;
	}

	@Override
	public String toString() {
		return "SurveyQuestionsResponseModel [id=" + id + ", name=" + name + ", questions=" + questions + "]";
	}

}
