package com.drofff.palindrome.dto;

public class RestAuthorizationTokenDto implements RestResponseDto {

    private String token;

    public RestAuthorizationTokenDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}
