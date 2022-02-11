package com.zisbv.gamification.models;

import java.util.List;

public class CountryLangModel {

	private Long countryID;

	private String countryName;

	private String alias;

	private Boolean countryStatus;

	private List<LanguageModel> languages;

	public Long getCountryID() {
		return countryID;
	}

	public void setCountryID(Long countryID) {
		this.countryID = countryID;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Boolean getCountryStatus() {
		return countryStatus;
	}

	public void setCountryStatus(Boolean countryStatus) {
		this.countryStatus = countryStatus;
	}

	public List<LanguageModel> getLanguages() {
		return languages;
	}

	public void setLanguages(List<LanguageModel> languages) {
		this.languages = languages;
	}

	@Override
	public String toString() {
		return "CountryLangModel [countryID=" + countryID + ", countryName=" + countryName + ", alias=" + alias
				+ ", countryStatus=" + countryStatus + ", languages=" + languages + "]";
	}

}
