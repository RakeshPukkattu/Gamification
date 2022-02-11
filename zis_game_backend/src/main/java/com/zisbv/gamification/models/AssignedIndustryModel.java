package com.zisbv.gamification.models;

import java.util.ArrayList;
import java.util.List;

public class AssignedIndustryModel {

	private Long industryID;

	private String industryName;

	private Boolean industryStatus;

	private List<ThemeDataResponse> themes = new ArrayList<ThemeDataResponse>();

	private List<GameDataResponse> games = new ArrayList<GameDataResponse>();

	public Long getIndustryID() {
		return industryID;
	}

	public void setIndustryID(Long industryID) {
		this.industryID = industryID;
	}

	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public Boolean getIndustryStatus() {
		return industryStatus;
	}

	public void setIndustryStatus(Boolean industryStatus) {
		this.industryStatus = industryStatus;
	}

	public List<ThemeDataResponse> getThemes() {
		return themes;
	}

	public void setThemes(List<ThemeDataResponse> themes) {
		this.themes = themes;
	}

	public List<GameDataResponse> getGames() {
		return games;
	}

	public void setGames(List<GameDataResponse> games) {
		this.games = games;
	}

	@Override
	public String toString() {
		return "AssignedIndustryModel [industryID=" + industryID + ", industryName=" + industryName
				+ ", industryStatus=" + industryStatus + ", themes=" + themes + ", games=" + games + "]";
	}

}
