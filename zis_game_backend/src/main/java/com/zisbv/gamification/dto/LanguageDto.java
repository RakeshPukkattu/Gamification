package com.zisbv.gamification.dto;

public class LanguageDto {

	private String language;

	private Boolean status;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "LanguageDto [language=" + language + ", status=" + status + "]";
	}

}
