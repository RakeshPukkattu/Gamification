package com.zisbv.gamification.models;

import java.util.ArrayList;
import java.util.List;

public class AssignedIndustryModelResponse {

	List<AssignedIndustryModel> industries = new ArrayList<AssignedIndustryModel>();

	public List<AssignedIndustryModel> getIndustries() {
		return industries;
	}

	public void setIndustries(List<AssignedIndustryModel> industries) {
		this.industries = industries;
	}

	@Override
	public String toString() {
		return "AssignedIndustryModelResponse [industries=" + industries + "]";
	}

}
