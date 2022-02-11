package com.zisbv.gamification.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AssignedSurveyData {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Long surveyID;

	private String userID;

	private String groupID;

	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date last_modified;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSurveyID() {
		return surveyID;
	}

	public void setSurveyID(Long surveyID) {
		this.surveyID = surveyID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getGroupID() {
		return groupID;
	}

	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}

	public Date getLast_modified() {
		return last_modified;
	}

	public void setLast_modified(Date last_modified) {
		this.last_modified = last_modified;
	}

	@Override
	public String toString() {
		return "AssignedSurveyData [id=" + id + ", surveyID=" + surveyID + ", userID=" + userID + ", groupID=" + groupID
				+ ", last_modified=" + last_modified + "]";
	}

}
