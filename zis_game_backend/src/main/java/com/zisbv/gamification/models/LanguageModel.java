package com.zisbv.gamification.models;

public class LanguageModel {

	private Long languageId;

	private String languageName;

	private Boolean languageStatus;

	public Long getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Long languageId) {
		this.languageId = languageId;
	}

	public String getLanguageName() {
		return languageName;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

	public Boolean getLanguageStatus() {
		return languageStatus;
	}

	public void setLanguageStatus(Boolean languageStatus) {
		this.languageStatus = languageStatus;
	}

	@Override
	public String toString() {
		return "LanguageModel [languageId=" + languageId + ", languageName=" + languageName + ", languageStatus="
				+ languageStatus + "]";
	}

}
