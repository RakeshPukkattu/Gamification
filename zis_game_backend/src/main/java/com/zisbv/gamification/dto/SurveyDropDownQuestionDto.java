package com.zisbv.gamification.dto;

import java.util.ArrayList;
import java.util.List;

public class SurveyDropDownQuestionDto {

	/**
	 * "questionNumberInSurvey":"1", "question":"How much you like Gamification?",
	 * "Options":["25%","50%","75%","100%"], "labels":["L1","L2","L3","L4"],
	 * "answer":["100%","50%"]
	 **/

	private Integer questionNumberInSurvey;

	private String question;

	private List<String> options;

	private List<String> labels;

	private List<String> answer = new ArrayList<String>();

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

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public List<String> getAnswer() {
		return answer;
	}

	public void setAnswer(List<String> answer) {
		this.answer = answer;
	}

	@Override
	public String toString() {
		return "SurveyDropDownQuestionDto [questionNumberInSurvey=" + questionNumberInSurvey + ", question=" + question
				+ ", options=" + options + ", labels=" + labels + ", answer=" + answer + "]";
	}

	// public SurveyMCQQuestionData dataFromPojo() {
	//
	// SurveyMCQQuestionData questionData = new SurveyMCQQuestionData();
	// String answerString =
	// answer.stream().map(Object::toString).collect(Collectors.joining(","));
	//
	// questionData.setQuestionNumberInSurvey(questionNumberInSurvey);
	// questionData.setQuestion(question);
	// questionData.setOption1(option1);
	// questionData.setOption2(option2);
	// questionData.setOption3(option3);
	// questionData.setOption4(option4);
	// questionData.setAnswer(answerString);
	// questionData.setLast_modified(new Date());
	//
	// return questionData;
	//
	// }
}
