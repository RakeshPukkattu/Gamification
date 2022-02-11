package com.zisbv.gamification.models;

import java.util.ArrayList;
import java.util.List;

public class GameSessionGroupResponseModel {

	List<GroupInSessionModel> gameSessionGroupResponse = new ArrayList<GroupInSessionModel>();

	public List<GroupInSessionModel> getGameSessionGroupResponse() {
		return gameSessionGroupResponse;
	}

	public void setGameSessionGroupResponse(List<GroupInSessionModel> gameSessionGroupResponse) {
		this.gameSessionGroupResponse = gameSessionGroupResponse;
	}

	@Override
	public String toString() {
		return "GameSessionGroupResponseModel [gameSessionGroupResponse=" + gameSessionGroupResponse + "]";
	}

}
