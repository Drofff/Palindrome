package com.drofff.palindrome.controller.api;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.Violation;
import com.drofff.palindrome.dto.*;
import com.drofff.palindrome.mapper.RestCarDtoMapper;
import com.drofff.palindrome.mapper.RestDriverDtoMapper;
import com.drofff.palindrome.mapper.RestViolationDtoMapper;
import com.drofff.palindrome.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.drofff.palindrome.constants.EndpointConstants.DRIVERS_API_ENDPOINT;
import static com.drofff.palindrome.utils.FormattingUtils.concatPathSegments;
import static com.drofff.palindrome.utils.ListUtils.applyToEachListElement;

@RestController
@RequestMapping(DRIVERS_API_ENDPOINT)
public class DriverApiController {

    private final DriverService driverService;
    private final PhotoService photoService;
    private final CarService carService;
    private final ViolationService violationService;
    private final RestDriverDtoMapper restDriverDtoMapper;
    private final RestCarDtoMapper restCarDtoMapper;
    private final RestViolationDtoMapper restViolationDtoMapper;
    private final MappingsResolver mappingsResolver;

    @Value("${application.url}")
    private String applicationUrl;

    @Autowired
    public DriverApiController(DriverService driverService, PhotoService photoService,
                               CarService carService, ViolationService violationService,
                               RestDriverDtoMapper restDriverDtoMapper, RestCarDtoMapper restCarDtoMapper,
                               RestViolationDtoMapper restViolationDtoMapper, MappingsResolver mappingsResolver) {
        this.driverService = driverService;
        this.photoService = photoService;
        this.carService = carService;
        this.violationService = violationService;
        this.restDriverDtoMapper = restDriverDtoMapper;
        this.restCarDtoMapper = restCarDtoMapper;
        this.restViolationDtoMapper = restViolationDtoMapper;
        this.mappingsResolver = mappingsResolver;
    }

    @GetMapping
    public ResponseEntity<RestResponseDto> getDriversByName(String name) {
        List<Driver> drivers = driverService.getDriversByName(name);
        List<RestDriverDto> restDriverDtos = applyToEachListElement(this::toRestDriverDto, drivers);
        RestListDto<RestDriverDto> driverRestListDto = new RestListDto<>(restDriverDtos);
        return ResponseEntity.ok(driverRestListDto);
    }

    private RestDriverDto toRestDriverDto(Driver driver) {
        RestDriverDto restDriverDto = restDriverDtoMapper.toDto(driver);
        String photoUrl = getUrlToPhotoOfDriver(driver);
        restDriverDto.setPhotoUrl(photoUrl);
        return restDriverDto;
    }

    private String getUrlToPhotoOfDriver(Driver driver) {
        String driverEndpointsUrl = applicationUrl + DRIVERS_API_ENDPOINT;
        String getDriverPhotoUri = driver.getId() + "/photo";
        return concatPathSegments(driverEndpointsUrl, getDriverPhotoUri);
    }

    @GetMapping("/{id}/photo")
    public ResponseEntity<byte[]> getPhotoOfDriverWithId(@PathVariable String id) {
        Driver driver = driverService.getDriverById(id);
        String driverPhotoUri = driver.getPhotoUri();
        byte[] photo = photoService.loadPhotoByUri(driverPhotoUri);
        return ResponseEntity.ok(photo);
    }

    @GetMapping("/{id}/cars")
    public ResponseEntity<RestResponseDto> getCarsOfDriverWithId(@PathVariable String id) {
        Driver driver = driverService.getDriverById(id);
        List<Car> cars = carService.getCarsOfDriver(driver);
        List<RestCarDto> restCarDtos = applyToEachListElement(this::toRestCarDto, cars);
        RestListDto<RestCarDto> restListDto = new RestListDto<>(restCarDtos);
        return ResponseEntity.ok(restListDto);
    }

    private RestCarDto toRestCarDto(Car car) {
        RestCarDto restCarDto = restCarDtoMapper.toDto(car);
        return mappingsResolver.resolveMappings(car, restCarDto);
    }

    @GetMapping("/{id}/violations")
    public ResponseEntity<RestResponseDto> getViolationsOfDriverWithId(@PathVariable String id) {
        Driver driver = driverService.getDriverById(id);
        List<Violation> violations = violationService.getDriverViolations(driver);
        List<RestViolationDto> restViolationDtos = applyToEachListElement(this::toRestViolationDto, violations);
        RestListDto<RestViolationDto> restListDto = new RestListDto<>(restViolationDtos);
        return ResponseEntity.ok(restListDto);
    }

    private RestViolationDto toRestViolationDto(Violation violation) {
        RestViolationDto restViolationDto = restViolationDtoMapper.toDto(violation);
        return mappingsResolver.resolveMappings(violation, restViolationDto);
    }

}