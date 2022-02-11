package com.zisbv.gamification.dto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.zisbv.gamification.entities.GameSessionUserData;

public class GameSessionUserDto {

	private String sessionName;

	private String startDateandTime;

	private String endDateandTime;

	// private String userIds;
	private List<String> userIds = new ArrayList<String>();

	private Boolean sessionStatus;

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public String getStartDateandTime() {
		return startDateandTime;
	}

	public void setStartDateandTime(String startDateandTime) {
		this.startDateandTime = startDateandTime;
	}

	public String getEndDateandTime() {
		return endDateandTime;
	}

	public void setEndDateandTime(String endDateandTime) {
		this.endDateandTime = endDateandTime;
	}

	public List<String> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<String> userIds) {
		this.userIds = userIds;
	}

	public Boolean getSessionStatus() {
		return sessionStatus;
	}

	public void setSessionStatus(Boolean sessionStatus) {
		this.sessionStatus = sessionStatus;
	}

	@Override
	public String toString() {
		return "GameSessionUserDto [sessionName=" + sessionName + ", startDateandTime=" + startDateandTime
				+ ", endDateandTime=" + endDateandTime + ", userIds=" + userIds + ", sessionStatus=" + sessionStatus
				+ "]";
	}

	public GameSessionUserData dataFromPojo() {

		String finalStartT = startDateandTime + ":00z";
		String finalEndT = endDateandTime + ":00z";

		GameSessionUserData session = new GameSessionUserData();
		String usersString = userIds.stream().map(Object::toString).collect(Collectors.joining(","));

		session.setSessionName(sessionName);
		Instant instant = Instant.parse(finalStartT);
		LocalDateTime result = LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId()));
		session.setStartDateAndTime(result);
		Instant instant1 = Instant.parse(finalEndT);
		LocalDateTime result1 = LocalDateTime.ofInstant(instant1, ZoneId.of(ZoneOffset.UTC.getId()));
		session.setEndDateAndTime(result1);
		session.setUserIds(usersString);
		session.setSessionEnableDisable(true);
		session.setSessionCurrentStatus("not_Initiated");
		session.setLast_modified(new Date());

		return session;
	}
}
