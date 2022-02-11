package com.zisbv.gamification.dto;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.zisbv.gamification.entities.CompanyAssignedComponents;

public class AdminAssignedComponentsDto {

	private Long themeId;

	private List<String> industryId;

	private List<String> componentId;

	private List<String> gameId;

	@Override
	public String toString() {
		return "AdminAssignedComponentsDto [themeId=" + themeId + ", industryId=" + industryId + ", componentId="
				+ componentId + ", gameId=" + gameId + "]";
	}

	public Long getThemeId() {
		return themeId;
	}

	public void setThemeId(Long themeId) {
		this.themeId = themeId;
	}

	public List<String> getIndustryId() {
		return industryId;
	}

	public void setIndustryId(List<String> industryId) {
		this.industryId = industryId;
	}

	public List<String> getComponentId() {
		return componentId;
	}

	public void setComponentId(List<String> componentId) {
		this.componentId = componentId;
	}

	public List<String> getGameId() {
		return gameId;
	}

	public void setGameId(List<String> gameId) {
		this.gameId = gameId;
	}

	public CompanyAssignedComponents dataFromPojo() {
		CompanyAssignedComponents data = new CompanyAssignedComponents();

		String componentsIDString = componentId.stream().map(Object::toString).collect(Collectors.joining(","));
		String gamesIDString = gameId.stream().map(Object::toString).collect(Collectors.joining(","));
		String industryIDString = industryId.stream().map(Object::toString).collect(Collectors.joining(","));

		data.setThemeID(themeId);
		data.setIndustryID(industryIDString);
		data.setComponentID(componentsIDString);
		data.setGameID(gamesIDString);
		data.setLast_modified(new Date());

		return data;
	}

}
