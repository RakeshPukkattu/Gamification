package com.zisbv.gamification.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Country {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String countryName;

	private String aliasCountryName;

	private Boolean enableDisableStatus;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "cl_fID", referencedColumnName = "id")
	List<Language> languages = new ArrayList<Language>();

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "cr_fID", referencedColumnName = "id")
	List<Region> regions = new ArrayList<Region>();

	public Country() {

	}

	public Country(String countryName, String aliasCountryName, Boolean enableDisableStatus) {
		super();
		this.countryName = countryName;
		this.aliasCountryName = aliasCountryName;
		this.enableDisableStatus = enableDisableStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getAliasCountryName() {
		return aliasCountryName;
	}

	public void setAliasCountryName(String aliasCountryName) {
		this.aliasCountryName = aliasCountryName;
	}

	public Boolean getEnableDisableStatus() {
		return enableDisableStatus;
	}

	public void setEnableDisableStatus(Boolean enableDisableStatus) {
		this.enableDisableStatus = enableDisableStatus;
	}

	public List<Language> getLanguages() {
		return languages;
	}

	public void setLanguages(List<Language> languages) {
		this.languages = languages;
	}

	public List<Region> getRegions() {
		return regions;
	}

	public void setRegions(List<Region> regions) {
		this.regions = regions;
	}

}
