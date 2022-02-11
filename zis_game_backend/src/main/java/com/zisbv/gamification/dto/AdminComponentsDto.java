package com.zisbv.gamification.dto;

import java.util.Date;

import com.zisbv.gamification.entities.AdminComponents;

public class AdminComponentsDto {

	private String title;

	private String content;

	private String className;

	private String link;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public AdminComponents dataFromPojo() {
		AdminComponents data = new AdminComponents();
		data.setTitle(title);
		data.setContent(content);
		data.setClassName(className);
		data.setLink(link);
		data.setLast_modified(new Date());

		return data;
	}
}
