package com.zisbv.gamification.dto;

import java.util.ArrayList;
import java.util.List;

public class SurveyDto {

	private String surveyName;

	private Integer totalNumbersOfMCQQuestions;

	private List<SurveyMCQQuestionDto> surveyMCQQuestions = new ArrayList<SurveyMCQQuestionDto>();

	private Integer totalNumbersOfMMCQQuestions;

	private List<SurveyMMCQQuestionDto> surveyMMCQQuestions = new ArrayList<SurveyMMCQQuestionDto>();

	private Integer totalNumbersOfDropDownQuestions;

	private List<SurveyDropDownQuestionDto> surveyDropDownQuestions = new ArrayList<SurveyDropDownQuestionDto>();

	public String getSurveyName() {
		return surveyName;
	}

	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}

	public Integer getTotalNumbersOfMCQQuestions() {
		return totalNumbersOfMCQQuestions;
	}

	public void setTotalNumbersOfMCQQuestions(Integer totalNumbersOfMCQQuestions) {
		this.totalNumbersOfMCQQuestions = totalNumbersOfMCQQuestions;
	}

	public List<SurveyMCQQuestionDto> getSurveyMCQQuestions() {
		return surveyMCQQuestions;
	}

	public void setSurveyMCQQuestions(List<SurveyMCQQuestionDto> surveyMCQQuestions) {
		this.surveyMCQQuestions = surveyMCQQuestions;
	}

	public Integer getTotalNumbersOfMMCQQuestions() {
		return totalNumbersOfMMCQQuestions;
	}

	public void setTotalNumbersOfMMCQQuestions(Integer totalNumbersOfMMCQQuestions) {
		this.totalNumbersOfMMCQQuestions = totalNumbersOfMMCQQuestions;
	}

	public List<SurveyMMCQQuestionDto> getSurveyMMCQQuestions() {
		return surveyMMCQQuestions;
	}

	public void setSurveyMMCQQuestions(List<SurveyMMCQQuestionDto> surveyMMCQQuestions) {
		this.surveyMMCQQuestions = surveyMMCQQuestions;
	}

	public Integer getTotalNumbersOfDropDownQuestions() {
		return totalNumbersOfDropDownQuestions;
	}

	public void setTotalNumbersOfDropDownQuestions(Integer totalNumbersOfDropDownQuestions) {
		this.totalNumbersOfDropDownQuestions = totalNumbersOfDropDownQuestions;
	}

	public List<SurveyDropDownQuestionDto> getSurveyDropDownQuestions() {
		return surveyDropDownQuestions;
	}

	public void setSurveyDropDownQuestions(List<SurveyDropDownQuestionDto> surveyDropDownQuestions) {
		this.surveyDropDownQuestions = surveyDropDownQuestions;
	}

	@Override
	public String toString() {
		return "SurveyDto [surveyName=" + surveyName + ", totalNumbersOfMCQQuestions=" + totalNumbersOfMCQQuestions
				+ ", surveyMCQQuestions=" + surveyMCQQuestions + ", totalNumbersOfMMCQQuestions="
				+ totalNumbersOfMMCQQuestions + ", surveyMMCQQuestions=" + surveyMMCQQuestions
				+ ", totalNumbersOfDropDownQuestions=" + totalNumbersOfDropDownQuestions + ", surveyDropDownQuestions="
				+ surveyDropDownQuestions + "]";
	}

//	public SurveyData dataFromPojo() {
//
//		SurveyData survey = new SurveyData();
//
//		survey.setSurveyName(surveyName);
//		survey.setNoOfQuestionsInMCQ(totalNumbersOfMCQQuestions);
//		survey.setNoOfQuestionsInMMCQ(totalNumbersOfMMCQQuestions);
//		survey.setNoOfQuestionsInDropDown(totalNumbersOfDropDownQuestions);
//		survey.setIsAssigned(false);
//		survey.setEnableDisableStatus(true);
//		survey.setLast_modified(new Date());
//
//		return survey;
//	}

//	public List<SurveyMCQQuestionData> mcqQuestionsFromPojo() {
//
//		List<SurveyMCQQuestionData> surveyMCQQuestionsList = new ArrayList<SurveyMCQQuestionData>();
//		Boolean surveyMCQQuestionsIsEmpty = surveyMCQQuestions.isEmpty();
//		if (!surveyMCQQuestionsIsEmpty) {
//
//			for (SurveyMCQQuestionDto question : surveyMCQQuestions) {
//
//				SurveyMCQQuestionData questionData = new SurveyMCQQuestionData();
//				questionData.setsID(question.getQuestionNumberInSurvey() + surveyName);
//				questionData.setQuestionNumberInSurvey(question.getQuestionNumberInSurvey());
//				questionData.setQuestion(question.getQuestion());
//				questionData.setOption1(question.getOption1());
//				questionData.setOption2(question.getOption2());
//				questionData.setOption3(question.getOption3());
//				questionData.setOption4(question.getOption4());
//				questionData.setAnswer(question.getAnswer());
//				questionData.setLast_modified(new Date());
//
//				surveyMCQQuestionsList.add(questionData);
//			}
//
//			return surveyMCQQuestionsList;
//		}
//
//		return surveyMCQQuestionsList;
//
//	}

//	public List<SurveyMMCQQuestionData> mmcqQuestionsFromPojo() {
//		List<SurveyMMCQQuestionData> surveyMMCQQuestionsList = new ArrayList<SurveyMMCQQuestionData>();
//		Boolean surveyMCQQuestionsIsEmpty = surveyMCQQuestions.isEmpty();
//		if (!surveyMCQQuestionsIsEmpty) {
//			for (SurveyMMCQQuestionDto question : surveyMMCQQuestions) {
//
//				SurveyMMCQQuestionData questionData = new SurveyMMCQQuestionData();
//				questionData.setsID(question.getQuestionNumberInSurvey() + surveyName);
//				questionData.setQuestionNumberInSurvey(question.getQuestionNumberInSurvey());
//				questionData.setQuestion(question.getQuestion());
//				questionData.setOption1(question.getOption1());
//				questionData.setOption2(question.getOption2());
//				questionData.setOption3(question.getOption3());
//				questionData.setOption4(question.getOption4());
//				String answerString = question.getAnswer().stream().map(Object::toString)
//						.collect(Collectors.joining(","));
//				questionData.setAnswer(answerString);
//				questionData.setLast_modified(new Date());
//
//				surveyMMCQQuestionsList.add(questionData);
//			}
//
//			return surveyMMCQQuestionsList;
//		}
//		return surveyMMCQQuestionsList;
//
//	}

//	public List<SurveyDropDownQuestionData> dropDownQuestionsFromPojo() {
//		List<SurveyDropDownQuestionData> surveyDropDownQuestionsList = new ArrayList<SurveyDropDownQuestionData>();
//		Boolean surveyMCQQuestionsIsEmpty = surveyMCQQuestions.isEmpty();
//		if (!surveyMCQQuestionsIsEmpty) {
//			for (SurveyDropDownQuestionDto question : surveyDropDownQuestions) {
//
//				SurveyDropDownQuestionData questionData = new SurveyDropDownQuestionData();
//				questionData.setsID(question.getQuestionNumberInSurvey() + surveyName);
//				questionData.setQuestionNumberInSurvey(question.getQuestionNumberInSurvey());
//				questionData.setQuestion(question.getQuestion());
//
//				String optionsString = question.getOptions().stream().map(Object::toString)
//						.collect(Collectors.joining(","));
//				questionData.setOptions(optionsString);
//
//				String labelsString = question.getLabels().stream().map(Object::toString)
//						.collect(Collectors.joining(","));
//				questionData.setLabels(labelsString);
//
//				String answerString = question.getAnswer().stream().map(Object::toString)
//						.collect(Collectors.joining(","));
//				questionData.setAnswers(answerString);
//				questionData.setLast_modified(new Date());
//
//				surveyDropDownQuestionsList.add(questionData);
//			}
//
//		}
//
//		return surveyDropDownQuestionsList;
//	}
}
