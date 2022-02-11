package com.zisbv.gamification.models;

import java.util.ArrayList;
import java.util.List;

public class ModifyIndustryModel {

	private String industryName;

	private Boolean industryStatus;

	private List<String> themes = new ArrayList<String>();

	private List<String> games = new ArrayList<String>();

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

	public List<String> getThemes() {
		return themes;
	}

	public void setThemes(List<String> themes) {
		this.themes = themes;
	}

	public List<String> getGames() {
		return games;
	}

	public void setGames(List<String> games) {
		this.games = games;
	}

	@Override
	public String toString() {
		return "ModifyIndustryModel [industryName=" + industryName + ", industryStatus=" + industryStatus + ", themes="
				+ themes + ", games=" + games + "]";
	}

}
