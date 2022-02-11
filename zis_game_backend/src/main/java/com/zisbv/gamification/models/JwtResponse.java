package com.zisbv.gamification.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JwtResponse {

	private Long id;

	private String email;
	
	private String role;

	private String accessToken;

	private String refreshToken;

	private String tokenType;

	private String expiryDuration;

	public JwtResponse(Long id,String email,String role, String accessToken, String refreshToken, String expiryDuration) {
		this.id = id;
		this.email = email;
		this.role= role;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.expiryDuration = expiryDuration;
		tokenType = "Bearer";
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
	

	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getExpiryDuration() {
		return expiryDuration;
	}

	public void setExpiryDuration(String expiryDuration) {
		this.expiryDuration = expiryDuration;
	}

}
