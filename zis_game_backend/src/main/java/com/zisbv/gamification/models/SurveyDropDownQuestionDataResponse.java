package com.zisbv.gamification.models;

import java.util.Arrays;

public class SurveyDropDownQuestionDataResponse {

	private Long id;

	private Integer questionNumber;

	private String question;

	private String[] options;

	private String[] labels;

	private String[] answers;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getQuestionNumber() {
		return questionNumber;
	}

	public void setQuestionNumber(Integer questionNumber) {
		this.questionNumber = questionNumber;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String[] getOptions() {
		return options;
	}

	public void setOptions(String[] options) {
		this.options = options;
	}

	public String[] getLabels() {
		return labels;
	}

	public void setLabels(String[] labels) {
		this.labels = labels;
	}

	public String[] getAnswers() {
		return answers;
	}

	public void setAnswers(String[] answers) {
		this.answers = answers;
	}

	@Override
	public String toString() {
		return "SurveyDropDownQuestionDataResponse [id=" + id + ", questionNumber=" + questionNumber + ", question="
				+ question + ", options=" + Arrays.toString(options) + ", labels=" + Arrays.toString(labels)
				+ ", answers=" + Arrays.toString(answers) + "]";
	}

}
