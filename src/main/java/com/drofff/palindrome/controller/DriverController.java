package com.drofff.palindrome.controller;

import static com.drofff.palindrome.constants.EndpointConstants.HOME_ENDPOINT;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.ModelUtils.putValidationExceptionIntoModel;
import static com.drofff.palindrome.utils.ModelUtils.redirectToWithMessage;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.dto.DriverDto;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.mapper.DriverDtoMapper;
import com.drofff.palindrome.service.DriverService;
import com.drofff.palindrome.service.PhotoService;

@Controller
@RequestMapping("/driver")
@PreAuthorize("hasAuthority('DRIVER')")
public class DriverController {

	private static final String DRIVER_CREATED_MESSAGE = "You have successfully created your driver profile! Welcome!";

	private static final String CREATE_DRIVER_VIEW = "createDriverPage";
	private static final String UPDATE_DRIVER_VIEW = "updateDriverPage";
	private static final String UPDATE_DRIVER_PHOTO_VIEW = "updateDriverPhotoPage";

	private static final String DRIVER_PROFILE_PAGE_ENDPOINT = "/driver";

	private static final String DRIVER_PARAM = "driver";
	private static final String PHOTO_PARAM = "photo";

	private final DriverService driverService;
	private final PhotoService photoService;
	private final DriverDtoMapper driverDtoMapper;

	public DriverController(DriverService driverService, PhotoService photoService,
	                        DriverDtoMapper driverDtoMapper) {
		this.driverService = driverService;
		this.photoService = photoService;
		this.driverDtoMapper = driverDtoMapper;
	}

	@GetMapping
	public String getDriverProfilePage(Model model) {
		Driver driver = getCurrentDriverProfile();
		model.addAttribute(DRIVER_PARAM, driver);
		String photo = photoService.loadEncodedPhotoByUri(driver.getPhotoUri());
		model.addAttribute(PHOTO_PARAM, photo);
		return "driverPage";
	}

	@GetMapping("/create")
	public String getCreateDriverProfilePage() {
		return CREATE_DRIVER_VIEW;
	}

	@PostMapping("/create")
	public String createDriver(DriverDto driverDto, MultipartFile photo, Model model) {
		Driver driver = driverDtoMapper.toEntity(driverDto);
		try {
			driverService.createDriverProfileWithPhoto(driver, photo);
			return redirectToWithMessage(HOME_ENDPOINT, DRIVER_CREATED_MESSAGE);
		} catch(ValidationException e) {
			model.addAttribute(DRIVER_PARAM, driver);
			putValidationExceptionIntoModel(e, model);
			return CREATE_DRIVER_VIEW;
		}
	}

	@GetMapping("/update")
	public String getUpdateDriverProfilePage(Model model) {
		Driver driverProfile = getCurrentDriverProfile();
		model.addAttribute(DRIVER_PARAM, driverProfile);
		return UPDATE_DRIVER_VIEW;
	}

	@PostMapping("/update")
	public String updateDriver(DriverDto driverDto, Model model) {
		Driver driver = driverDtoMapper.toEntity(driverDto);
		try {
			driverService.updateDriverProfile(driver);
			return redirectToWithMessage(DRIVER_PROFILE_PAGE_ENDPOINT, "Profile data has been successfully updated");
		} catch(ValidationException e) {
			putValidationExceptionIntoModel(e, model);
			model.addAttribute(DRIVER_PARAM, driver);
			return UPDATE_DRIVER_VIEW;
		}
	}

	@GetMapping("/update/photo")
	public String getUpdateDriverPhotoPage(Model model) {
		model.addAttribute(PHOTO_PARAM, getCurrentDriverPhoto());
		return UPDATE_DRIVER_PHOTO_VIEW;
	}

	@PostMapping("/update/photo")
	public String updateDriverPhoto(MultipartFile photo, Model model) {
		try {
			driverService.updateDriverPhoto(photo);
			return redirectToWithMessage(DRIVER_PROFILE_PAGE_ENDPOINT, "Photo has been changed successfully");
		} catch(ValidationException e) {
			putValidationExceptionIntoModel(e, model);
			model.addAttribute(PHOTO_PARAM, getCurrentDriverPhoto());
			return UPDATE_DRIVER_PHOTO_VIEW;
		}
	}

	private String getCurrentDriverPhoto() {
		Driver driver = getCurrentDriverProfile();
		return photoService.loadEncodedPhotoByUri(driver.getPhotoUri());
	}

	private Driver getCurrentDriverProfile() {
		User currentUser = getCurrentUser();
		return driverService.getDriverByUserId(currentUser.getId());
	}

}
