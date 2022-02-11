package com.zisbv.gamification.dto;

import java.util.ArrayList;
import java.util.List;

public class GameDataDto {

	private String gameName;

	private List<String> keywords = new ArrayList<String>();

	private Boolean assessment;

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public Boolean getAssessment() {
		return assessment;
	}

	public void setAssessment(Boolean assessment) {
		this.assessment = assessment;
	}

	@Override
	public String toString() {
		return "GameDataDto [gameName=" + gameName + ", keywords=" + keywords + ", assessment=" + assessment + "]";
	}

}
