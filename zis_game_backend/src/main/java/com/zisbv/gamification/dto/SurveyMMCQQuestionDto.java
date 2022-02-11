package com.zisbv.gamification.dto;

import java.util.List;

public class SurveyMMCQQuestionDto {

	private Integer questionNumberInSurvey;

	private String question;

	private String option1;

	private String option2;

	private String option3;

	private String option4;

	private List<String> answers;

	public Integer getQuestionNumberInSurvey() {
		return questionNumberInSurvey;
	}

	public void setQuestionNumberInSurvey(Integer questionNumberInSurvey) {
		this.questionNumberInSurvey = questionNumberInSurvey;
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

	public List<String> getAnswer() {
		return answers;
	}

	public void setAnswer(List<String> answers) {
		this.answers = answers;
	}

	@Override
	public String toString() {
		return "SurveyMMCQQuestionDto [questionNumberInSurvey=" + questionNumberInSurvey + ", question=" + question
				+ ", option1=" + option1 + ", option2=" + option2 + ", option3=" + option3 + ", option4=" + option4
				+ ", answers=" + answers + "]";
	}

	// public SurveyMCQQuestionData dataFromPojo() {
	//
	// SurveyMCQQuestionData questionData = new SurveyMCQQuestionData();
	//
	// questionData.setQuestionNumberInSurvey(questionNumberInSurvey);
	// questionData.setQuestion(question);
	// questionData.setOption1(option1);
	// questionData.setOption2(option2);
	// questionData.setOption3(option3);
	// questionData.setOption4(option4);
	// questionData.setAnswer(answer);
	// questionData.setLast_modified(new Date());
	//
	// return questionData;
	//
	// }
}
