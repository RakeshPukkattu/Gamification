package com.zisbv.gamification.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CompanyAssignedComponents {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Long companyID;

	private String industryID;

	private Long themeID;

	private String componentID;

	private String gameID;

	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date last_modified;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCompanyID() {
		return companyID;
	}

	public void setCompanyID(Long companyID) {
		this.companyID = companyID;
	}

	public String getIndustryID() {
		return industryID;
	}

	public void setIndustryID(String industryID) {
		this.industryID = industryID;
	}

	public Long getThemeID() {
		return themeID;
	}

	public void setThemeID(Long themeID) {
		this.themeID = themeID;
	}

	public String getComponentID() {
		return componentID;
	}

	public void setComponentID(String componentID) {
		this.componentID = componentID;
	}

	public String getGameID() {
		return gameID;
	}

	public void setGameID(String gameID) {
		this.gameID = gameID;
	}

	public Date getLast_modified() {
		return last_modified;
	}

	public void setLast_modified(Date last_modified) {
		this.last_modified = last_modified;
	}

	@Override
	public String toString() {
		return "CompanyAssignedComponents [id=" + id + ", companyID=" + companyID + ", industryID=" + industryID
				+ ", themeID=" + themeID + ", componentID=" + componentID + ", gameID=" + gameID + ", last_modified="
				+ last_modified + "]";
	}

}
