package com.drofff.palindrome.controller.advice;

import com.drofff.palindrome.dto.RestMessageDto;
import com.drofff.palindrome.dto.RestResponseDto;
import com.drofff.palindrome.dto.RestValidationDto;
import com.drofff.palindrome.exception.PalindromeException;
import com.drofff.palindrome.exception.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice(basePackages = "com.drofff.palindrome.controller.api")
public class ApiExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<RestResponseDto> validationExceptionHandler(ValidationException validationException) {
        RestValidationDto restValidationDto = RestValidationDto.fromValidationException(validationException);
        return ResponseEntity.badRequest()
                .body(restValidationDto);
    }

    @ExceptionHandler
    public ResponseEntity<RestResponseDto> palindromeExceptionHandler(PalindromeException palindromeException) {
        RestMessageDto restMessageDto = new RestMessageDto(palindromeException.getMessage());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(restMessageDto);
    }

}
