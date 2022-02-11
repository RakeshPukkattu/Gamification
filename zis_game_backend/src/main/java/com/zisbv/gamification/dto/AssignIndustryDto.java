package com.zisbv.gamification.dto;

import java.util.List;

public class AssignIndustryDto {

	private List<String> themeId;

	private List<String> gamesId;

	public List<String> getThemeId() {
		return themeId;
	}

	public void setThemeId(List<String> themeId) {
		this.themeId = themeId;
	}

	public List<String> getGamesId() {
		return gamesId;
	}

	public void setGamesId(List<String> gamesId) {
		this.gamesId = gamesId;
	}

	@Override
	public String toString() {
		return " themeId=" + themeId + ", gamesId=" + gamesId + "]";
	}
}
