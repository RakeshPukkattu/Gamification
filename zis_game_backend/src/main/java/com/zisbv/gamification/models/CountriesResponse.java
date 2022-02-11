package com.zisbv.gamification.models;

import java.util.ArrayList;
import java.util.List;

import com.zisbv.gamification.entities.Country;

public class CountriesResponse {

	private List<Country> countries = new ArrayList<Country>();

	public List<Country> getCountries() {
		return countries;
	}

	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}

	@Override
	public String toString() {
		return "CountriesResponse [countries=" + countries + "]";
	}

}
