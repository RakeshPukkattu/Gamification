//package com.zisbv.gamification.handlers;
//
//import java.util.Date;
//
//import com.zisbv.gamification.entities.SurveyData;
//import com.zisbv.gamification.entities.SurveyMCQQuestionData;
//
//public class SurveyManagementHandler {
//
//	public SurveyData modifySurvey(SurveyData survey, SurveyData existingSurvey) {
//
//		SurveyData newSurvey = new SurveyData();
//
//		newSurvey.setId(existingSurvey.getId());
//		newSurvey.setSurveyName(survey.getSurveyName());
//		newSurvey.setNoOfQuestionsInMCQ(survey.getNoOfQuestionsInMCQ());
//		newSurvey.setQuestionIdsMCQ(survey.getQuestionIdsMCQ());
//		newSurvey.setNoOfQuestionsInMMCQ(survey.getNoOfQuestionsInMMCQ());
//		newSurvey.setQuestionIdsMMCQ(survey.getQuestionIdsMMCQ());
//		newSurvey.setNoOfQuestionsInDropDown(survey.getNoOfQuestionsInDropDown());
//		newSurvey.setQuestionIdsDropDown(survey.getQuestionIdsDropDown());
//		newSurvey.setEnableDisableStatus(existingSurvey.getEnableDisableStatus());
//		newSurvey.setIsAssigned(existingSurvey.getIsAssigned());
//		newSurvey.setLast_modified(new Date());
//
//		return newSurvey;
//	}
//
//	public SurveyData updateSurveyStatus(SurveyData existingSurvey, Boolean status) {
//
//		SurveyData newSurvey = new SurveyData();
//
//		newSurvey.setId(existingSurvey.getId());
//		newSurvey.setSurveyName(existingSurvey.getSurveyName());
//		newSurvey.setNoOfQuestionsInMCQ(existingSurvey.getNoOfQuestionsInMCQ());
//		newSurvey.setQuestionIdsMCQ(existingSurvey.getQuestionIdsMCQ());
//		newSurvey.setNoOfQuestionsInMMCQ(existingSurvey.getNoOfQuestionsInMMCQ());
//		newSurvey.setQuestionIdsMMCQ(existingSurvey.getQuestionIdsMMCQ());
//		newSurvey.setNoOfQuestionsInDropDown(existingSurvey.getNoOfQuestionsInDropDown());
//		newSurvey.setQuestionIdsDropDown(existingSurvey.getQuestionIdsDropDown());
//		newSurvey.setEnableDisableStatus(status);
//		newSurvey.setIsAssigned(existingSurvey.getIsAssigned());
//		newSurvey.setLast_modified(new Date());
//
//		return newSurvey;
//	}
//
//	public SurveyMCQQuestionData modifySurveyQuestion(SurveyMCQQuestionData data, SurveyMCQQuestionData existingQuestion) {
//
//		SurveyMCQQuestionData modifiedData = new SurveyMCQQuestionData();
//		modifiedData.setId(existingQuestion.getId());
//
//		if (data.getQuestionNumberInSurvey() != null) {
//			modifiedData.setQuestionNumberInSurvey(data.getQuestionNumberInSurvey());
//		} else {
//			modifiedData.setQuestionNumberInSurvey(existingQuestion.getQuestionNumberInSurvey());
//		}
//
//		if (data.getQuestion() != null) {
//			modifiedData.setQuestion(data.getQuestion());
//		} else {
//			modifiedData.setQuestion(existingQuestion.getQuestion());
//		}
//
//		if (data.getOption1() != null) {
//			modifiedData.setOption1(data.getOption1());
//		} else {
//			modifiedData.setOption1(existingQuestion.getOption1());
//		}
//
//		if (data.getOption1() != null) {
//			modifiedData.setOption1(data.getOption1());
//		} else {
//			modifiedData.setOption1(existingQuestion.getOption1());
//		}
//
//		if (data.getOption2() != null) {
//			modifiedData.setOption2(data.getOption2());
//		} else {
//			modifiedData.setOption2(existingQuestion.getOption2());
//		}
//
//		if (data.getOption3() != null) {
//			modifiedData.setOption3(data.getOption3());
//		} else {
//			modifiedData.setOption3(existingQuestion.getOption3());
//		}
//
//		if (data.getOption4() != null) {
//			modifiedData.setOption4(data.getOption4());
//		} else {
//			modifiedData.setOption4(existingQuestion.getOption4());
//		}
//
//		modifiedData.setLast_modified(new Date());
//
//		return modifiedData;
//	}
//
//}
