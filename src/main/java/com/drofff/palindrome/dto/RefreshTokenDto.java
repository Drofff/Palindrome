package com.drofff.palindrome.dto;

public class RefreshTokenDto implements RestResponseDto {

	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
