package com.zisbv.gamification.models;

import java.util.ArrayList;
import java.util.List;

public class AssignedComponentsModel {

	// ThemeResponse theme;
	List<ThemeDataResponse> theme = new ArrayList<ThemeDataResponse>();

	List<IndustryModel> industries = new ArrayList<IndustryModel>();

	List<ComponentModel> components = new ArrayList<ComponentModel>();

	List<GameDataResponse> games = new ArrayList<GameDataResponse>();

	public List<ThemeDataResponse> getTheme() {
		return theme;
	}

	public void setTheme(List<ThemeDataResponse> theme) {
		this.theme = theme;
	}

	public List<IndustryModel> getIndustries() {
		return industries;
	}

	public void setIndustries(List<IndustryModel> industries) {
		this.industries = industries;
	}

	public List<ComponentModel> getComponents() {
		return components;
	}

	public void setComponents(List<ComponentModel> components) {
		this.components = components;
	}

	public List<GameDataResponse> getGames() {
		return games;
	}

	public void setGames(List<GameDataResponse> games) {
		this.games = games;
	}

	@Override
	public String toString() {
		return "AssignedComponentsModel [theme=" + theme + ", industries=" + industries + ", components=" + components
				+ ", games=" + games + "]";
	}

}
