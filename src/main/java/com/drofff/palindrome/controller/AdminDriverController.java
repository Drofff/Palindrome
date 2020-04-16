package com.drofff.palindrome.controller;

import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.dto.DriverDto;
import com.drofff.palindrome.enums.DriverIdType;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.mapper.DriverDtoMapper;
import com.drofff.palindrome.service.AuthenticationService;
import com.drofff.palindrome.service.DriverService;
import com.drofff.palindrome.service.PhotoService;
import com.drofff.palindrome.service.UserBlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.drofff.palindrome.constants.EndpointConstants.ADMIN_USERS_ENDPOINT;
import static com.drofff.palindrome.constants.ParameterConstants.*;
import static com.drofff.palindrome.enums.DriverIdType.DRIVER_ID;
import static com.drofff.palindrome.utils.ModelUtils.putValidationExceptionIntoModel;
import static com.drofff.palindrome.utils.ModelUtils.redirectToWithMessage;

@Controller
@RequestMapping("/admin/users/driver")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminDriverController {

    private static final String UPDATE_DRIVER_VIEW = "adminUpdateDriverPage";

    private static final DriverIdType DEFAULT_DRIVER_PAGE_ID_TYPE = DRIVER_ID;

    private final AuthenticationService authenticationService;
    private final PhotoService photoService;
    private final UserBlockService userBlockService;
    private final DriverService driverService;
    private final DriverDtoMapper driverDtoMapper;

    @Autowired
    public AdminDriverController(AuthenticationService authenticationService, PhotoService photoService,
                                 UserBlockService userBlockService, DriverService driverService,
                                 DriverDtoMapper driverDtoMapper) {
        this.authenticationService = authenticationService;
        this.photoService = photoService;
        this.userBlockService = userBlockService;
        this.driverService = driverService;
        this.driverDtoMapper = driverDtoMapper;
    }

    @GetMapping("/{id}")
    public String viewDriverProfile(@PathVariable String id, @RequestParam(required = false) DriverIdType type, Model model) {
        Driver driver = getDriverByIdOfType(id, type);
        User user = authenticationService.getUserById(driver.getUserId());
        model.addAttribute(DRIVER_PARAM, driver);
        model.addAttribute(EMAIL_PARAM, user.getUsername());
        model.addAttribute(USER_ID_PARAM, user.getId());
        String encodedPhoto = photoService.loadEncodedPhotoByUri(driver.getPhotoUri());
        model.addAttribute(PHOTO_PARAM, encodedPhoto);
        model.addAttribute(BLOCKED_PARAM, userBlockService.isUserBlocked(user));
        return "viewDriverProfilePage";
    }

    private Driver getDriverByIdOfType(String id, DriverIdType type) {
        return Optional.ofNullable(type)
                .orElse(DEFAULT_DRIVER_PAGE_ID_TYPE)
                .getFindDriverFunctionFromService(driverService)
                .apply(id);
    }

    @GetMapping("/{id}/update")
    public String getUpdateDriverPage(@PathVariable String id, Model model) {
        Driver driver = driverService.getDriverByUserId(id);
        model.addAttribute(DRIVER_PARAM, driver);
        return UPDATE_DRIVER_VIEW;
    }

    @PostMapping("/{id}/update")
    public String updateDriver(@PathVariable String id, DriverDto driverDto, Model model) {
        Driver driver = driverDtoMapper.toEntity(driverDto);
        driver.setId(id);
        try {
            driverService.updateDriverProfile(driver);
            return redirectToWithMessage(ADMIN_USERS_ENDPOINT, "Successfully updated driver data");
        } catch(ValidationException e) {
            model.addAttribute(DRIVER_PARAM, driver);
            putValidationExceptionIntoModel(e, model);
            return UPDATE_DRIVER_VIEW;
        }
    }

}
