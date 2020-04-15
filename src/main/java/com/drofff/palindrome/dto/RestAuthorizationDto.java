package com.drofff.palindrome.dto;

import org.springframework.http.ResponseEntity;

public class RestAuthorizationDto implements RestResponseDto {

    private String authorizationToken;

    private String refreshToken;

	public String getAuthorizationToken() {
		return authorizationToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public static class Builder {

		private RestAuthorizationDto restAuthorizationDto = new RestAuthorizationDto();

		public Builder withAuthorizationToken(String authorizationToken) {
			restAuthorizationDto.authorizationToken = authorizationToken;
			return this;
		}

		public Builder withRefreshToken(String refreshToken) {
			restAuthorizationDto.refreshToken = refreshToken;
			return this;
		}

		public ResponseEntity<RestResponseDto> asResponseEntityOk() {
			return ResponseEntity.ok(restAuthorizationDto);
		}

	}

}
