package com.drofff.palindrome.controller;

import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.drofff.palindrome.document.Department;
import com.drofff.palindrome.document.Police;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.dto.RestAuthorizationTokenDto;
import com.drofff.palindrome.dto.RestPoliceDto;
import com.drofff.palindrome.dto.RestResponseDto;
import com.drofff.palindrome.dto.RestValidationDto;
import com.drofff.palindrome.dto.UserDto;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.mapper.RestPoliceDtoMapper;
import com.drofff.palindrome.service.AuthenticationService;
import com.drofff.palindrome.service.AuthorizationService;
import com.drofff.palindrome.service.DepartmentService;
import com.drofff.palindrome.service.PhotoService;
import com.drofff.palindrome.service.PoliceService;

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
            String token = authorizationService.generateAuthorizationTokenForUser(user);
            RestAuthorizationTokenDto restAuthorizationTokenDto = new RestAuthorizationTokenDto(token);
            return ResponseEntity.ok(restAuthorizationTokenDto);
        } catch (ValidationException e) {
            RestValidationDto restValidationDto = RestValidationDto.fromValidationException(e);
            return ResponseEntity.badRequest()
                    .body(restValidationDto);
        }
    }

    @GetMapping("/police-info")
    @PreAuthorize("hasAuthority('POLICE')")
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