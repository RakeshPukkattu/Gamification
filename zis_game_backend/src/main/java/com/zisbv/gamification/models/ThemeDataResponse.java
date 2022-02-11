package com.zisbv.gamification.models;

public class ThemeDataResponse {

	private Long id;

	private String themeName;

	private String thumbNailKey;

	private String imageKey;

	private String zipFileKey;

	private String keyWords;

	private Boolean status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getThemeName() {
		return themeName;
	}

	public void setThemeName(String themeName) {
		this.themeName = themeName;
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

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ThemeDataResponse [id=" + id + ", themeName=" + themeName + ", thumbNailKey=" + thumbNailKey
				+ ", imageKey=" + imageKey + ", zipFileKey=" + zipFileKey + ", keyWords=" + keyWords + ", status="
				+ status + "]";
	}

}
