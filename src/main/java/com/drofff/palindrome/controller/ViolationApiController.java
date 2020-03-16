package com.drofff.palindrome.controller;

import com.drofff.palindrome.document.Violation;
import com.drofff.palindrome.dto.RestMessageDto;
import com.drofff.palindrome.dto.RestResponseDto;
import com.drofff.palindrome.dto.RestValidationDto;
import com.drofff.palindrome.dto.ViolationDto;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.mapper.ViolationDtoMapper;
import com.drofff.palindrome.service.ViolationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.drofff.palindrome.constants.EndpointConstants.VIOLATION_API_BASE_ENDPOINT;

@RestController
@RequestMapping(VIOLATION_API_BASE_ENDPOINT)
public class ViolationApiController {

    private final ViolationService violationService;
    private final ViolationDtoMapper violationDtoMapper;

    @Autowired
    public ViolationApiController(ViolationService violationService, ViolationDtoMapper violationDtoMapper) {
        this.violationService = violationService;
        this.violationDtoMapper = violationDtoMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<RestResponseDto> createViolation(@RequestBody ViolationDto violationDto) {
        Violation violation = violationDtoMapper.toEntity(violationDto);
        try {
            violationService.addViolation(violation);
            RestMessageDto restMessageDto = new RestMessageDto("Violation has been successfully added");
            return ResponseEntity.ok(restMessageDto);
        } catch (ValidationException e) {
            RestValidationDto restValidationDto = RestValidationDto.fromValidationException(e);
            return ResponseEntity.badRequest()
                    .body(restValidationDto);
        }
    }

}
