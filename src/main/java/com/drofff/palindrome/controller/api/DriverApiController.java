package com.drofff.palindrome.controller.api;

import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.dto.RestDriverDto;
import com.drofff.palindrome.dto.RestListDto;
import com.drofff.palindrome.dto.RestResponseDto;
import com.drofff.palindrome.mapper.RestDriverDtoMapper;
import com.drofff.palindrome.service.DriverService;
import com.drofff.palindrome.service.PhotoService;
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
    private final RestDriverDtoMapper restDriverDtoMapper;

    @Value("${application.url}")
    private String applicationUrl;

    @Autowired
    public DriverApiController(DriverService driverService, PhotoService photoService,
                               RestDriverDtoMapper restDriverDtoMapper) {
        this.driverService = driverService;
        this.photoService = photoService;
        this.restDriverDtoMapper = restDriverDtoMapper;
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

}
