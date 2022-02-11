package com.zisbv.gamification.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class IndustryData {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String industryName;

	private Boolean status;

	private String themeID;

	private String gameID;

	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date last_modified;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getThemeID() {
		return themeID;
	}

	public void setThemeID(String themeID) {
		this.themeID = themeID;
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
		return "IndustryData [id=" + id + ", industryName=" + industryName + ", status=" + status + ", themeID="
				+ themeID + ", gameID=" + gameID + ", last_modified=" + last_modified + "]";
	}

}
