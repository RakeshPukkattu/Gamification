package com.zisbv.gamification.models;

import java.util.ArrayList;
import java.util.List;

public class GroupsResponseModel {

	private List<GroupModel> groups = new ArrayList<>();

	public List<GroupModel> getGroups() {
		return groups;
	}

	public void setGroups(List<GroupModel> groups) {
		this.groups = groups;
	}

	@Override
	public String toString() {
		return "GroupsResponseModel [groups=" + groups + "]";
	}

}
