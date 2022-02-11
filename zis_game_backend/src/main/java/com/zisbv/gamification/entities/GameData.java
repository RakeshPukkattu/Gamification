package com.zisbv.gamification.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class GameData {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String gameName;

	private String thumbNailKey;

	private String imageKey;

	private String zipFileKey;

	private String keyWords;

	private Boolean assessment;

	private Boolean gameStatus;

	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date last_modified;

	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getGameName() {
		return gameName;
	}


	public void setGameName(String gameName) {
		this.gameName = gameName;
	}


	public String getThumbNailKey() {
		return thumbNailKey;
	}


	public void setThumbNailKey(String thumbNailKey) {
		this.thumbNailKey = thumbNailKey;
	}


	public String getImageKey() {
		return imageKey;
	}


	public void setImageKey(String imageKey) {
		this.imageKey = imageKey;
	}


	public String getZipFileKey() {
		return zipFileKey;
	}


	public void setZipFileKey(String zipFileKey) {
		this.zipFileKey = zipFileKey;
	}


	public String getKeyWords() {
		return keyWords;
	}


	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}


	public Boolean getAssessment() {
		return assessment;
	}


	public void setAssessment(Boolean assessment) {
		this.assessment = assessment;
	}


	public Boolean getGameStatus() {
		return gameStatus;
	}


	public void setGameStatus(Boolean gameStatus) {
		this.gameStatus = gameStatus;
	}


	public Date getLast_modified() {
		return last_modified;
	}


	public void setLast_modified(Date last_modified) {
		this.last_modified = last_modified;
	}


	@Override
	public String toString() {
		return "GameData [id=" + id + ", gameName=" + gameName + ", thumbNailKey=" + thumbNailKey + ", imageKey="
				+ imageKey + ", zipFileKey=" + zipFileKey + ", keyWords=" + keyWords + ", assessment=" + assessment
				+ ", gameStatus=" + gameStatus + ", last_modified=" + last_modified + "]";
	}

}
