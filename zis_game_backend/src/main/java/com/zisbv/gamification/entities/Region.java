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
public class Region {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String regionName;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "rs_fID", referencedColumnName = "id")
	List<State> states = new ArrayList<State>();

	public Region() {

	}

	public Region(String regionName) {
		super();
		this.regionName = regionName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public List<State> getStates() {
		return states;
	}

	public void setStates(List<State> states) {
		this.states = states;
	}

}
