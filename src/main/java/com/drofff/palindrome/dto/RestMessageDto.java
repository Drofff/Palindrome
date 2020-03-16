package com.drofff.palindrome.dto;

public class RestMessageDto implements RestResponseDto {

    private String message;

    public RestMessageDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
