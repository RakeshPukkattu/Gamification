package com.zisbv.gamification.models;

public class AuthToken {

	private Long id;

	private String email;

	private String accessToken;

	public AuthToken(String token, String email, Long id) {

		this.accessToken = token;
		this.email = email;
		this.id= id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public AuthToken(String token) {
		this.accessToken = token;
	}

	public String getToken() {
		return accessToken;
	}

	public void setToken(String token) {
		this.accessToken = token;
	}

	
	@Override
	public String toString() {
		return "AuthToken [id=" + id + ", email=" + email + ", accessToken=" + accessToken + "]";
	}

}
