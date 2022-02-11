package com.zisbv.gamification.models;

public class GamificationMailContents {

	String recipientEmail;
	String subject;
	String content;

	public GamificationMailContents() {
		
	}
	
	public GamificationMailContents(String recipientEmail, String subject, String content) {
		super();
		this.recipientEmail = recipientEmail;
		this.subject = subject;
		this.content = content;
	}

	public String getRecipientEmail() {
		return recipientEmail;
	}

	public void setRecipientEmail(String recipientEmail) {
		this.recipientEmail = recipientEmail;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "GamificationMailContents [recipientEmail=" + recipientEmail + ", subject=" + subject + ", content="
				+ content + "]";
	}

}
