package com.zisbv.gamification.models;

import java.util.ArrayList;
import java.util.List;

public class AssignedSurveyResponseModel {

	private Boolean surveyStatus;

	private Boolean isAssigned;
	
	private SurveyQuestionsResponseModel surveyQuestionsResponseModel;

	private List<AssignedSurveyUsersModel> assignedUsers = new ArrayList<AssignedSurveyUsersModel>();

	private List<AssignedSurveyGroupsModel> assignedGroups = new ArrayList<AssignedSurveyGroupsModel>();

	public SurveyQuestionsResponseModel getSurveyQuestionsResponseModel() {
		return surveyQuestionsResponseModel;
	}

	public void setSurveyQuestionsResponseModel(SurveyQuestionsResponseModel surveyQuestionsResponseModel) {
		this.surveyQuestionsResponseModel = surveyQuestionsResponseModel;
	}

	public Boolean getSurveyStatus() {
		return surveyStatus;
	}

	public void setSurveyStatus(Boolean surveyStatus) {
		this.surveyStatus = surveyStatus;
	}

	public List<AssignedSurveyUsersModel> getAssignedUsers() {
		return assignedUsers;
	}

	public void setAssignedUsers(List<AssignedSurveyUsersModel> assignedUsers) {
		this.assignedUsers = assignedUsers;
	}

	public List<AssignedSurveyGroupsModel> getAssignedGroups() {
		return assignedGroups;
	}

	public void setAssignedGroups(List<AssignedSurveyGroupsModel> assignedGroups) {
		this.assignedGroups = assignedGroups;
	}

	public Boolean getIsAssigned() {
		return isAssigned;
	}

	public void setIsAssigned(Boolean isAssigned) {
		this.isAssigned = isAssigned;
	}

	@Override
	public String toString() {
		return "AssignedSurveyResponseModel [surveyQuestionsResponseModel=" + surveyQuestionsResponseModel
				+ ", surveyStatus=" + surveyStatus + ", isAssigned=" + isAssigned + ", assignedUsers=" + assignedUsers
				+ ", assignedGroups=" + assignedGroups + "]";
	}

}
