package com.zisbv.gamification.dto;

import java.util.ArrayList;
import java.util.List;

public class ThemeDataDto {

	private String themeName;

	private List<String> keywords = new ArrayList<String>();

	public String getThemeName() {
		return themeName;
	}

	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	@Override
	public String toString() {
		return "ThemeDataDto [themeName=" + themeName + ", keywords=" + keywords + "]";
	}
}
