package com.drofff.palindrome.controller.api;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.Violation;
import com.drofff.palindrome.dto.RestDriverDto;
import com.drofff.palindrome.dto.RestListDto;
import com.drofff.palindrome.dto.RestResponseDto;
import com.drofff.palindrome.exception.PalindromeException;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.grep.pattern.DriverPattern;
import com.drofff.palindrome.mapper.RestDriverDtoMapper;
import com.drofff.palindrome.service.CarService;
import com.drofff.palindrome.service.DriverService;
import com.drofff.palindrome.service.PhotoService;
import com.drofff.palindrome.service.ViolationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.drofff.palindrome.grep.Filter.grepByPattern;
import static com.drofff.palindrome.utils.ListUtils.applyToEachListElement;
import static com.drofff.palindrome.utils.ListUtils.isNotSingletonList;
import static com.drofff.palindrome.utils.ViolationUtils.getLatestViolationDateTimeIfPresent;

@RestController
@RequestMapping("/api/drivers")
public class DriverApiController {

    private final DriverService driverService;
    private final PhotoService photoService;
    private final ViolationService violationService;
    private final CarService carService;
    private final RestDriverDtoMapper restDriverDtoMapper;

    @Value("${application.url}")
    private String applicationUrl;

    @Autowired
    public DriverApiController(DriverService driverService, PhotoService photoService,
                               ViolationService violationService, CarService carService,
                               RestDriverDtoMapper restDriverDtoMapper) {
        this.driverService = driverService;
        this.photoService = photoService;
        this.violationService = violationService;
        this.carService = carService;
        this.restDriverDtoMapper = restDriverDtoMapper;
    }

    @GetMapping
    public ResponseEntity<RestResponseDto> getDriverByPattern(@RequestBody DriverPattern driverPattern) {
        validateDriverPattern(driverPattern);
        List<Driver> drivers = driverService.getAllDrivers();
        List<RestDriverDto> driverDtos = applyToEachListElement(this::toRestDriverDto, drivers);
        List<RestDriverDto> driversByPattern = grepByPattern(driverDtos, driverPattern);
        validateIsUnique(driversByPattern);
        RestListDto<RestDriverDto> driverListResponse = new RestListDto<>(driversByPattern);
        return ResponseEntity.ok(driverListResponse);
    }

    private void validateDriverPattern(DriverPattern driverPattern) {
        if(driverPattern.isEmpty()) {
            throw new ValidationException("At least one field value should be provided");
        }
    }

    private RestDriverDto toRestDriverDto(Driver driver) {
        RestDriverDto restDriverDto = restDriverDtoMapper.toDto(driver);
        String photoUrl = generatePhotoUrlForDriver(driver);
        restDriverDto.setPhotoUrl(photoUrl);
        Set<String> ownedCarNumbers = loadCarNumbersByIds(driver.getOwnedCarIds());
        restDriverDto.setOwnedCarNumbers(ownedCarNumbers);
	    List<Violation> driverViolations = violationService.getDriverViolations(driver);
	    restDriverDto.setViolationsCount(driverViolations.size());
	    LocalDateTime latestViolationDateTime = getLatestViolationDateTimeIfPresent(driverViolations)
			    .orElse(null);
	    restDriverDto.setLastViolationDateTime(latestViolationDateTime);
	    return restDriverDto;
    }

    private String generatePhotoUrlForDriver(Driver driver) {
    	return applicationUrl + "/api/drivers/" + driver.getId() + "/photo";
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

    @GetMapping("/{id}/photo")
    public ResponseEntity<byte[]> getPhotoOfDriverWithId(@PathVariable String id) {
        Driver driver = driverService.getDriverById(id);
        String driverPhotoUri = driver.getPhotoUri();
        byte[] photo = photoService.loadPhotoByUri(driverPhotoUri);
        return ResponseEntity.ok(photo);
    }

}
