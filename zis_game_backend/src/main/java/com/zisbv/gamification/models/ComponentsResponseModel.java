package com.zisbv.gamification.models;

import java.util.ArrayList;
import java.util.List;

public class ComponentsResponseModel {

	List<ComponentModel> components = new ArrayList<ComponentModel>();

	public List<ComponentModel> getComponents() {
		return components;
	}

	public void setComponents(List<ComponentModel> components) {
		this.components = components;
	}

	@Override
	public String toString() {
		return "ComponentsResponseModel [components=" + components + "]";
	}

}
