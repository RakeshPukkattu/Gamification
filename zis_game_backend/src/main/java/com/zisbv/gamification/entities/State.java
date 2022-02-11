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
public class State {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String stateName;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "sc_fID", referencedColumnName = "id")
	List<City> cities = new ArrayList<City>();

	public State() {

	}

	public State(String stateName) {
		super();
		this.stateName = stateName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public List<City> getCities() {
		return cities;
	}

	public void setCities(List<City> cities) {
		this.cities = cities;
	}

}
