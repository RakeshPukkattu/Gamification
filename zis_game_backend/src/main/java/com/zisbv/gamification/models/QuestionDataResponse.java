package com.zisbv.gamification.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class QuestionDataResponse {

	private String questionNumber;

	private String questionType;

	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Object question;

	public String getQuestionNumber() {
		return questionNumber;
	}

	public void setQuestionNumber(String questionNumber) {
		this.questionNumber = questionNumber;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public Object getQuestion() {
		return question;
	}

	public void setQuestion(Object question) {
		this.question = question;
	}

	@Override
	public String toString() {
		return "QuestionDataResponse [questionNumber=" + questionNumber + ", questionType=" + questionType
				+ ", question=" + question + "]";
	}

}
