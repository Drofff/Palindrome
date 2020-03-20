package com.drofff.palindrome.controller;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.Violation;
import com.drofff.palindrome.document.ViolationType;
import com.drofff.palindrome.dto.*;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.mapper.ViolationDtoMapper;
import com.drofff.palindrome.service.CarService;
import com.drofff.palindrome.service.DriverService;
import com.drofff.palindrome.service.ViolationService;
import com.drofff.palindrome.service.ViolationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.drofff.palindrome.constants.EndpointConstants.VIOLATION_API_BASE_ENDPOINT;

@RestController
@RequestMapping(VIOLATION_API_BASE_ENDPOINT)
public class ViolationApiController {

    private final ViolationService violationService;
    private final ViolationTypeService violationTypeService;
    private final CarService carService;
    private final DriverService driverService;
    private final ViolationDtoMapper violationDtoMapper;

    @Autowired
    public ViolationApiController(ViolationService violationService, ViolationTypeService violationTypeService,
                                  CarService carService, DriverService driverService,
                                  ViolationDtoMapper violationDtoMapper) {
        this.violationService = violationService;
	    this.violationTypeService = violationTypeService;
        this.carService = carService;
        this.driverService = driverService;
        this.violationDtoMapper = violationDtoMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<RestResponseDto> createViolation(@RequestBody ViolationDto violationDto) {
        Violation violation = violationDtoMapper.toEntity(violationDto);
        try {
            Car violatorCar = carService.getCarByNumber(violationDto.getCarNumber());
            violation.setCarId(violatorCar.getId());
            Driver violator = driverService.getOwnerOfCar(violatorCar);
            violation.setViolatorId(violator.getUserId());
            violationService.addViolation(violation);
            RestMessageDto restMessageDto = new RestMessageDto("Violation has been successfully added");
            return ResponseEntity.ok(restMessageDto);
        } catch (ValidationException e) {
            RestValidationDto restValidationDto = RestValidationDto.fromValidationException(e);
            return ResponseEntity.badRequest()
                    .body(restValidationDto);
        }
    }

    @GetMapping("/types")
	public ResponseEntity<RestResponseDto> getViolationTypes() {
    	List<ViolationType> violationTypes = violationTypeService.getAllViolationTypes();
	    RestListDto<ViolationType> restViolationTypesDto = new RestListDto<>(violationTypes);
    	return ResponseEntity.ok(restViolationTypesDto);
    }

}
