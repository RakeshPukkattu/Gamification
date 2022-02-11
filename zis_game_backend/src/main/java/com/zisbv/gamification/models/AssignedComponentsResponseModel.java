package com.zisbv.gamification.models;

import java.util.ArrayList;
import java.util.List;

public class AssignedComponentsResponseModel {

	List<AssignedComponentsModel> assignedComponents = new ArrayList<AssignedComponentsModel>();

	public List<AssignedComponentsModel> getAssignedComponents() {
		return assignedComponents;
	}

	public void setAssignedComponents(List<AssignedComponentsModel> assignedComponents) {
		this.assignedComponents = assignedComponents;
	}

	@Override
	public String toString() {
		return "AssignedComponentsResponseModel [assignedComponents=" + assignedComponents + "]";
	}

}
