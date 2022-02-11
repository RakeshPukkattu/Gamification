package com.zisbv.gamification.models;

public class ComponentModel {

	private Long id;

	private String title;

	private String context;

	private String imageKey;

	private String className;

	private String link;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getImageKey() {
		return imageKey;
	}

	public void setImageKey(String imageKey) {
		this.imageKey = imageKey;
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

	@Override
	public String toString() {
		return "ComponentModel [id=" + id + ", title=" + title + ", context=" + context + ", imageKey=" + imageKey
				+ ", className=" + className + ", link=" + link + "]";
	}

}
