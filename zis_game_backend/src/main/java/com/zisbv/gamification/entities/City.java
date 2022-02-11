package com.zisbv.gamification.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class City {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String cityName;

	private String cityLattitude;

	private String cityLongitude;

	public City() {

	}

	public City(String cityName, String cityLattitude, String cityLongitude) {
		super();
		this.cityName = cityName;
		this.cityLattitude = cityLattitude;
		this.cityLongitude = cityLongitude;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityLattitude() {
		return cityLattitude;
	}

	public void setCityLattitude(String cityLattitude) {
		this.cityLattitude = cityLattitude;
	}

	public String getCityLongitude() {
		return cityLongitude;
	}

	public void setCityLongitude(String cityLongitude) {
		this.cityLongitude = cityLongitude;
	}

}
