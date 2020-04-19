package com.drofff.palindrome.dto;

import org.springframework.http.ResponseEntity;

public class RestAuthorizationDto implements RestResponseDto {

    private String authorizationToken;

    private String authenticationToken;

	public String getAuthorizationToken() {
		return authorizationToken;
	}

	public String getAuthenticationToken() {
		return authenticationToken;
	}

	public static class Builder {

		private final RestAuthorizationDto restAuthorizationDto = new RestAuthorizationDto();

		public Builder withAuthorizationToken(String authorizationToken) {
			restAuthorizationDto.authorizationToken = authorizationToken;
			return this;
		}

		public Builder withAuthenticationToken(String authenticationToken) {
			restAuthorizationDto.authenticationToken = authenticationToken;
			return this;
		}

		public ResponseEntity<RestResponseDto> asResponseEntityOk() {
			return ResponseEntity.ok(restAuthorizationDto);
		}

	}

}
