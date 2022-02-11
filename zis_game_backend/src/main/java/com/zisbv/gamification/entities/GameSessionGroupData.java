package com.zisbv.gamification.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class GameSessionGroupData {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String sessionName;

	private Boolean sessionEnableDisable;

	private String sessionCurrentStatus;

	private LocalDateTime startDateAndTime;

	private LocalDateTime endDateAndTime;

	private Long countryID;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "GroupsAndSessions", joinColumns = { @JoinColumn(name = "sessions_ids") }, inverseJoinColumns = {
			@JoinColumn(name = "groups_ids") })
	private List<GroupData> groups = new ArrayList<>();

	public GameSessionGroupData() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public Boolean getSessionEnableDisable() {
		return sessionEnableDisable;
	}

	public void setSessionEnableDisable(Boolean sessionEnableDisable) {
		this.sessionEnableDisable = sessionEnableDisable;
	}

	public String getSessionCurrentStatus() {
		return sessionCurrentStatus;
	}

	public void setSessionCurrentStatus(String sessionCurrentStatus) {
		this.sessionCurrentStatus = sessionCurrentStatus;
	}

	public LocalDateTime getStartDateAndTime() {
		return startDateAndTime;
	}

	public void setStartDateAndTime(LocalDateTime startDateAndTime) {
		this.startDateAndTime = startDateAndTime;
	}

	public LocalDateTime getEndDateAndTime() {
		return endDateAndTime;
	}

	public void setEndDateAndTime(LocalDateTime endDateAndTime) {
		this.endDateAndTime = endDateAndTime;
	}

	public Long getCountryID() {
		return countryID;
	}

	public void setCountryID(Long countryID) {
		this.countryID = countryID;
	}

	public List<GroupData> getGroups() {
		return groups;
	}

	public void setGroups(List<GroupData> groups) {
		this.groups = groups;
	}

}
