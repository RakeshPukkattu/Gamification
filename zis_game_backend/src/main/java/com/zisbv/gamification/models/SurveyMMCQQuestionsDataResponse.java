package com.zisbv.gamification.models;

import java.util.Arrays;

public class SurveyMMCQQuestionsDataResponse {

	private Long id;

	private Integer questionNumber;

	private String question;

	private String option1;

	private String option2;

	private String option3;

	private String option4;

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

	public String getOption1() {
		return option1;
	}

	public void setOption1(String option1) {
		this.option1 = option1;
	}

	public String getOption2() {
		return option2;
	}

	public void setOption2(String option2) {
		this.option2 = option2;
	}

	public String getOption3() {
		return option3;
	}

	public void setOption3(String option3) {
		this.option3 = option3;
	}

	public String getOption4() {
		return option4;
	}

	public void setOption4(String option4) {
		this.option4 = option4;
	}

	public String[] getAnswers() {
		return answers;
	}

	public void setAnswers(String[] answers) {
		this.answers = answers;
	}

	@Override
	public String toString() {
		return "SurveyMMCQQuestionsDataResponse [id=" + id + ", questionNumber=" + questionNumber + ", question="
				+ question + ", option1=" + option1 + ", option2=" + option2 + ", option3=" + option3 + ", option4="
				+ option4 + ", answers=" + Arrays.toString(answers) + "]";
	}

}
