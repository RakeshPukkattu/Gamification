package com.zisbv.gamification.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SurveyQuestionData {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String surveyName;

	private String questionNumbers;

	private String questionTypes;

	private String questionIDs;

	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date last_modified;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSurveyName() {
		return surveyName;
	}

	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}

	public String getQuestionNumbers() {
		return questionNumbers;
	}

	public void setQuestionNumbers(String questionNumbers) {
		this.questionNumbers = questionNumbers;
	}

	public String getQuestionTypes() {
		return questionTypes;
	}

	public void setQuestionTypes(String questionTypes) {
		this.questionTypes = questionTypes;
	}

	public String getQuestionIDs() {
		return questionIDs;
	}

	public void setQuestionIDs(String questionIDs) {
		this.questionIDs = questionIDs;
	}

	public Date getLast_modified() {
		return last_modified;
	}

	public void setLast_modified(Date last_modified) {
		this.last_modified = last_modified;
	}

	@Override
	public String toString() {
		return "SurveyQuestionData [id=" + id + ", surveyName=" + surveyName + ", questionNumbers=" + questionNumbers
				+ ", questionTypes=" + questionTypes + ", questionIDs=" + questionIDs + ", last_modified="
				+ last_modified + "]";
	}

}
