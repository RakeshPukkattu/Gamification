package com.zisbv.gamification.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ThemeData {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String themeName;

	private String thumbNailKey;

	private String imageKey;

	private String zipFileKey;

	private String keyWords;

	private Boolean themeStatus;

	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date last_modified;

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

	public Boolean getThemeStatus() {
		return themeStatus;
	}

	public void setThemeStatus(Boolean themeStatus) {
		this.themeStatus = themeStatus;
	}

	public Date getLast_modified() {
		return last_modified;
	}

	public void setLast_modified(Date last_modified) {
		this.last_modified = last_modified;
	}

	@Override
	public String toString() {
		return "ThemeData [id=" + id + ", themeName=" + themeName + ", thumbNailKey=" + thumbNailKey + ", imageKey="
				+ imageKey + ", zipFileKey=" + zipFileKey + ", keyWords=" + keyWords + ", themeStatus=" + themeStatus
				+ ", last_modified=" + last_modified + "]";
	}

}
