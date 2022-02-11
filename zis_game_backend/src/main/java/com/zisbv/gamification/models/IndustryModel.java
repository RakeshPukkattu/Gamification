package com.zisbv.gamification.models;

public class IndustryModel {

	private Long industryId;

	private String industryName;

	private Boolean industryStatus;

	public Long getIndustryId() {
		return industryId;
	}

	public void setIndustryId(Long industryId) {
		this.industryId = industryId;
	}

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

	@Override
	public String toString() {
		return "IndustryModel [industryId=" + industryId + ", industryName=" + industryName + ", industryStatus="
				+ industryStatus + "]";
	}

}
