package com.drofff.palindrome.controller;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.Violation;
import com.drofff.palindrome.dto.*;
import com.drofff.palindrome.exception.PalindromeException;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.grep.pattern.DriverPattern;
import com.drofff.palindrome.mapper.RestDriverDtoMapper;
import com.drofff.palindrome.mapper.RestFindDriverDtoMapper;
import com.drofff.palindrome.service.CarService;
import com.drofff.palindrome.service.DriverService;
import com.drofff.palindrome.service.PhotoService;
import com.drofff.palindrome.service.ViolationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.drofff.palindrome.constants.EndpointConstants.DRIVER_API_BASE_ENDPOINT;
import static com.drofff.palindrome.grep.Filter.grepByPattern;
import static com.drofff.palindrome.utils.ListUtils.applyToEachListElement;
import static com.drofff.palindrome.utils.ListUtils.isNotSingletonList;
import static com.drofff.palindrome.utils.ViolationUtils.getLatestViolationDateTimeIfPresent;

@RestController
@RequestMapping(DRIVER_API_BASE_ENDPOINT)
public class DriverApiController {

    private final DriverService driverService;
    private final PhotoService photoService;
    private final ViolationService violationService;
    private final CarService carService;
    private final RestDriverDtoMapper restDriverDtoMapper;
    private final RestFindDriverDtoMapper restFindDriverDtoMapper;

    @Autowired
    public DriverApiController(DriverService driverService, PhotoService photoService,
                               ViolationService violationService, CarService carService,
                               RestDriverDtoMapper restDriverDtoMapper, RestFindDriverDtoMapper restFindDriverDtoMapper) {
        this.driverService = driverService;
        this.photoService = photoService;
        this.violationService = violationService;
        this.carService = carService;
        this.restDriverDtoMapper = restDriverDtoMapper;
        this.restFindDriverDtoMapper = restFindDriverDtoMapper;
    }

    @GetMapping
    public ResponseEntity<RestResponseDto> getDriverByPattern(@RequestBody DriverPattern driverPattern) {
        try {
            validateDriverPattern(driverPattern);
            List<Driver> drivers = driverService.getAllDrivers();
            List<RestFindDriverDto> findDriverDtos = applyToEachListElement(this::toRestFindDriverDtoWithNoPhoto, drivers);
            List<RestFindDriverDto> driversByPattern = grepByPattern(findDriverDtos, driverPattern);
            validateIsUnique(driversByPattern);
            driversByPattern.forEach(this::loadDriverPhotoIntoDto);
            RestListDto<RestFindDriverDto> driverListResponse = new RestListDto<>(driversByPattern);
            return ResponseEntity.ok(driverListResponse);
        } catch (ValidationException e) {
            RestValidationDto restValidationDto = RestValidationDto.fromValidationException(e);
            return ResponseEntity.badRequest()
                    .body(restValidationDto);
        }
    }

    private void validateDriverPattern(DriverPattern driverPattern) {
        if(driverPattern.isEmpty()) {
            throw new ValidationException("At least one field value should be provided");
        }
    }

    private RestFindDriverDto toRestFindDriverDtoWithNoPhoto(Driver driver) {
        RestFindDriverDto findDriverDto = restFindDriverDtoMapper.toDto(driver);
        Set<String> ownedCarNumbers = loadCarNumbersByIds(driver.getOwnedCarIds());
        findDriverDto.setOwnedCarNumbers(ownedCarNumbers);
        return findDriverDto;
    }

    private Set<String> loadCarNumbersByIds(Set<String> carIds) {
        return carIds.stream()
                .map(carService::getCarById)
                .map(Car::getNumber)
                .collect(Collectors.toSet());
    }

    private void validateIsUnique(List<?> list) {
        if(isNotSingletonList(list)) {
            throw new PalindromeException("Inconsistent data state. Non unique result has been returned");
        }
    }

    private void loadDriverPhotoIntoDto(RestFindDriverDto restFindDriverDto) {
        Driver driver = driverService.getDriverById(restFindDriverDto.getId());
        byte[] photo = photoService.loadPhotoByUri(driver.getPhotoUri());
        restFindDriverDto.setPhoto(photo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponseDto> getDriverById(@PathVariable String id) {
        Driver driver = driverService.getDriverById(id);
        RestDriverDto restDriverDto = toRestDriverDto(driver);
        return ResponseEntity.ok(restDriverDto);
    }

    private RestDriverDto toRestDriverDto(Driver driver) {
        RestDriverDto restDriverDto = restDriverDtoMapper.toDto(driver);
        byte[] photo = photoService.loadPhotoByUri(driver.getPhotoUri());
        restDriverDto.setPhoto(photo);
        List<Violation> driverViolations = violationService.getDriverViolations(driver);
        restDriverDto.setViolationsCount(driverViolations.size());
        LocalDateTime latestViolationDateTime = getLatestViolationDateTimeIfPresent(driverViolations)
                .orElse(null);
        restDriverDto.setLastViolationDateTime(latestViolationDateTime);
        return restDriverDto;
    }

}
