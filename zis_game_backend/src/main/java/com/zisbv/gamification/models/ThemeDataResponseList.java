package com.zisbv.gamification.models;

import java.util.ArrayList;
import java.util.List;

public class ThemeDataResponseList {

	List<ThemeDataResponse> themes = new ArrayList<ThemeDataResponse>();

	public List<ThemeDataResponse> getThemes() {
		return themes;
	}

	public void setThemes(List<ThemeDataResponse> themes) {
		this.themes = themes;
	}

	@Override
	public String toString() {
		return "ThemeDataResponseList [themes=" + themes + "]";
	}

}
