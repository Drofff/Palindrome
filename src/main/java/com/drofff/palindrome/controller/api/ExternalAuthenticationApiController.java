package com.drofff.palindrome.controller.api;

import com.drofff.palindrome.dto.RestExternalAuthDto;
import com.drofff.palindrome.dto.RestMessageDto;
import com.drofff.palindrome.dto.RestResponseDto;
import com.drofff.palindrome.service.ExternalAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/external-auth")
public class ExternalAuthenticationApiController {

    private final ExternalAuthenticationService externalAuthenticationService;

    @Autowired
    public ExternalAuthenticationApiController(ExternalAuthenticationService externalAuthenticationService) {
        this.externalAuthenticationService = externalAuthenticationService;
    }

    @PostMapping
    public ResponseEntity<RestResponseDto> completeExternalAuth(@RequestBody RestExternalAuthDto restExternalAuthDto) {
        externalAuthenticationService.completeAuthenticationWithOptionId(restExternalAuthDto.getOptionId(), restExternalAuthDto.getToken());
        RestMessageDto restMessageDto = new RestMessageDto("External authentication completion request has been successfully registered");
        return ResponseEntity.ok(restMessageDto);
    }

}
