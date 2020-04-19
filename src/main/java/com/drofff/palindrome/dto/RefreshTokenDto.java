package com.drofff.palindrome.dto;

public class RefreshTokenDto implements RestResponseDto {

	private String userId;

	private String authenticationToken;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAuthenticationToken() {
		return authenticationToken;
	}

	public void setAuthenticationToken(String authenticationToken) {
		this.authenticationToken = authenticationToken;
	}

}
