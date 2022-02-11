package com.zisbv.gamification.models;

import java.util.ArrayList;
import java.util.List;

public class GameSessionUserResponseModel {

	private List<GameSessionUserModel> sessions = new ArrayList<GameSessionUserModel>();

	public List<GameSessionUserModel> getSessions() {
		return sessions;
	}

	public void setSessions(List<GameSessionUserModel> sessions) {
		this.sessions = sessions;
	}

	@Override
	public String toString() {
		return "GameSessionUserResponseModel [sessions=" + sessions + "]";
	}

}
