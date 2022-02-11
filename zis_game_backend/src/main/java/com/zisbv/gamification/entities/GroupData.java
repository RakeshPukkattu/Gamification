package com.zisbv.gamification.entities;

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
public class GroupData {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long groupID;

	private String groupName;

	private Long countryID;

	private Boolean groupStatus;

	private Boolean groupInAnySession;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "usersAndGroups", joinColumns = { @JoinColumn(name = "groups_ids") }, inverseJoinColumns = {
			@JoinColumn(name = "users_ids") })
	private List<UserData> members = new ArrayList<>();

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "groups")
	private List<GameSessionGroupData> groupSessions = new ArrayList<>();

	public GroupData() {

	}

	public Long getGroupID() {
		return groupID;
	}

	public void setGroupID(Long groupID) {
		this.groupID = groupID;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Long getCountryID() {
		return countryID;
	}

	public void setCountryID(Long countryID) {
		this.countryID = countryID;
	}

	public Boolean getGroupStatus() {
		return groupStatus;
	}

	public void setGroupStatus(Boolean groupStatus) {
		this.groupStatus = groupStatus;
	}

	public Boolean getGroupInAnySession() {
		return groupInAnySession;
	}

	public void setGroupInAnySession(Boolean groupInAnySession) {
		this.groupInAnySession = groupInAnySession;
	}

	public List<UserData> getMembers() {
		return members;
	}

	public void setMembers(List<UserData> members) {
		this.members = members;
	}

	public List<GameSessionGroupData> getGroupSessions() {
		return groupSessions;
	}

	public void setGroupSessions(List<GameSessionGroupData> groupSessions) {
		this.groupSessions = groupSessions;
	}

}
