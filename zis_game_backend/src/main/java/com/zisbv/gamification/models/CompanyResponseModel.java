package com.zisbv.gamification.models;

import java.util.ArrayList;
import java.util.List;

public class CompanyResponseModel {

	private List<CompanyModel> company = new ArrayList<>();

	public List<CompanyModel> getCompany() {
		return company;
	}

	public void setCompany(List<CompanyModel> company) {
		this.company = company;
	}

	@Override
	public String toString() {
		return "CompanyResponseModel [company=" + company + "]";
	}

}
