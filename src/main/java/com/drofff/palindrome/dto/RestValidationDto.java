package com.drofff.palindrome.dto;

import com.drofff.palindrome.exception.ValidationException;

import java.util.HashMap;
import java.util.Map;

public class RestValidationDto implements RestResponseDto {

    private String message;

    private Map<String, String> fieldErrors = new HashMap<>();

    public static RestValidationDto fromValidationException(ValidationException e) {
        RestValidationDto restValidationDto = new RestValidationDto();
        restValidationDto.message = e.getMessage();
        restValidationDto.fieldErrors = e.getFieldErrors();
        return restValidationDto;
    }

    public static RestValidationDto withMessage(String message) {
        RestValidationDto restValidationDto = new RestValidationDto();
        restValidationDto.message = message;
        return restValidationDto;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

}
