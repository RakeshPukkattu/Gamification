package com.zisbv.gamification.models;

import java.util.ArrayList;
import java.util.List;

public class GameSessionResponseModel {

	private List<GameSessionModel> sessions = new ArrayList<GameSessionModel>();

	public List<GameSessionModel> getSessions() {
		return sessions;
	}

	public void setSessions(List<GameSessionModel> sessions) {
		this.sessions = sessions;
	}

	@Override
	public String toString() {
		return "GameSessionResponseModel [sessions=" + sessions + "]";
	}

}
