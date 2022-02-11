package com.zisbv.gamification.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.zisbv.gamification.util.AppConstants;

@ConfigurationProperties(prefix = AppConstants.FILE_PROPERTIES_PREFIX)
public class FileStorageProperties {

	private String uploadDir;

	private String avatharDir;

	private String themeDir;

	private String componentDir;

	private String gameDir;

	public String getComponentDir() {
		return componentDir;
	}

	public void setComponentDir(String componentDir) {
		this.componentDir = componentDir;
	}

	public String getThemeDir() {
		return themeDir;
	}

	public void setThemeDir(String themeDir) {
		this.themeDir = themeDir;
	}

	public String getAvatharDir() {
		return avatharDir;
	}

	public void setAvatharDir(String avatharDir) {
		this.avatharDir = avatharDir;
	}

	public String getUploadDir() {
		return uploadDir;
	}

	public void setUploadDir(String uploadDir) {
		this.uploadDir = uploadDir;
	}

	public String getGameDir() {
		return gameDir;
	}

	public void setGameDir(String gameDir) {
		this.gameDir = gameDir;
	}

}
