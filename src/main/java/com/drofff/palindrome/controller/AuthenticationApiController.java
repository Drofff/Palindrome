package com.drofff.palindrome.controller;

import com.drofff.palindrome.document.Department;
import com.drofff.palindrome.document.Police;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.dto.*;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.mapper.RestPoliceDtoMapper;
import com.drofff.palindrome.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;

@RestController
@RequestMapping("/api")
public class AuthenticationApiController {

    private final AuthenticationService authenticationService;
    private final AuthorizationService authorizationService;
    private final PoliceService policeService;
    private final DepartmentService departmentService;
    private final PhotoService photoService;
    private final RestPoliceDtoMapper restPoliceDtoMapper;

    @Autowired
    public AuthenticationApiController(AuthenticationService authenticationService, AuthorizationService authorizationService,
                                       PoliceService policeService, DepartmentService departmentService,
                                       PhotoService photoService, RestPoliceDtoMapper restPoliceDtoMapper) {
        this.authenticationService = authenticationService;
        this.authorizationService = authorizationService;
        this.policeService = policeService;
	    this.departmentService = departmentService;
	    this.photoService = photoService;
	    this.restPoliceDtoMapper = restPoliceDtoMapper;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<RestResponseDto> authenticate(@RequestBody UserDto userDto) {
        try {
            User user = authenticationService.authenticateUser(userDto.getUsername(), userDto.getPassword());
            String token = authorizationService.generateTokenForUser(user);
            RestAuthorizationTokenDto restAuthorizationTokenDto = new RestAuthorizationTokenDto(token);
            return ResponseEntity.ok(restAuthorizationTokenDto);
        } catch (ValidationException e) {
            RestValidationDto restValidationDto = RestValidationDto.fromValidationException(e);
            return ResponseEntity.badRequest()
                    .body(restValidationDto);
        }
    }

    @GetMapping("/police-info")
    public ResponseEntity<RestResponseDto> getCurrentPolice() {
        try {
            User user = getCurrentUser();
            Police police = policeService.getPoliceByUserId(user.getId());
            RestPoliceDto restPoliceDto = toRestPoliceDto(police);
            return ResponseEntity.ok(restPoliceDto);
        } catch (ValidationException e) {
            RestValidationDto restValidationDto = RestValidationDto.fromValidationException(e);
            return ResponseEntity.badRequest()
                    .body(restValidationDto);
        }
    }

    private RestPoliceDto toRestPoliceDto(Police police) {
	    RestPoliceDto restPoliceDto = restPoliceDtoMapper.toDto(police);
	    String department = getNameOfDepartmentWithId(police.getDepartmentId());
	    restPoliceDto.setDepartment(department);
	    byte[] photo = photoService.loadPhotoByUri(police.getPhotoUri());
	    restPoliceDto.setPhoto(photo);
	    return restPoliceDto;
    }

    private String getNameOfDepartmentWithId(String id) {
	    Department department = departmentService.getDepartmentById(id);
	    return department.getName();
    }

}