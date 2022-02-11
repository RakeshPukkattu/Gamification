package com.zisbv.gamification.models;

import java.util.ArrayList;
import java.util.List;

public class GameDataResponseList {

	List<GameDataResponse> games = new ArrayList<GameDataResponse>();

	public List<GameDataResponse> getGames() {
		return games;
	}

	public void setGames(List<GameDataResponse> games) {
		this.games = games;
	}

	@Override
	public String toString() {
		return "GameDataResponseList [games=" + games + "]";
	}

}
