package com.zisbv.gamification.models;

import java.util.ArrayList;
import java.util.List;

public class CountryLangResponseModel {

	private List<CountryLangModel> countries = new ArrayList<CountryLangModel>();

	public List<CountryLangModel> getCountries() {
		return countries;
	}

	public void setCountries(List<CountryLangModel> countries) {
		this.countries = countries;
	}

	@Override
	public String toString() {
		return "CountryLangResponseModel [countries=" + countries + "]";
	}

}
