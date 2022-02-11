package com.zisbv.gamification.models;

public class GameDataResponse {

	private Long id;

	private String gameName;

	private String thumbNailKey;

	private String imageKey;

	private String zipFileKey;

	private String keyWords;

	private Boolean assessment;

	private Boolean status;

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

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "GameDataResponse [id=" + id + ", gameName=" + gameName + ", thumbNailKey=" + thumbNailKey
				+ ", imageKey=" + imageKey + ", zipFileKey=" + zipFileKey + ", keyWords=" + keyWords + ", assessment="
				+ assessment + ", status=" + status + "]";
	}

}
