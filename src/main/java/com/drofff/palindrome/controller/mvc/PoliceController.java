package com.drofff.palindrome.controller.mvc;

import com.drofff.palindrome.document.Department;
import com.drofff.palindrome.document.Police;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.dto.PoliceDto;
import com.drofff.palindrome.dto.PoliceFatDto;
import com.drofff.palindrome.dto.UpdatePoliceDto;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.mapper.PoliceDtoMapper;
import com.drofff.palindrome.mapper.PoliceFatDtoMapper;
import com.drofff.palindrome.mapper.UpdatePoliceDtoMapper;
import com.drofff.palindrome.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Map;

import static com.drofff.palindrome.constants.EndpointConstants.HOME_ENDPOINT;
import static com.drofff.palindrome.constants.EndpointConstants.RELATIVE_HOME_ENDPOINT;
import static com.drofff.palindrome.constants.ParameterConstants.*;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.ModelUtils.putValidationExceptionIntoModel;
import static com.drofff.palindrome.utils.ModelUtils.redirectToWithMessage;

@Controller
@RequestMapping("/police")
public class PoliceController {

	private static final int STATISTIC_DAYS_COUNT = 7;

	private static final String CREATE_POLICE_VIEW = "createPolicePage";
	private static final String UPDATE_POLICE_VIEW = "updatePolicePage";
	private static final String UPDATE_POLICE_PHOTO_VIEW = "updatePolicePhotoPage";

	private final PoliceService policeService;
	private final DepartmentService departmentService;
	private final PhotoService photoService;
	private final ViolationService violationService;
	private final ChangeRequestService changeRequestService;
	private final PoliceDtoMapper policeDtoMapper;
	private final PoliceFatDtoMapper policeFatDtoMapper;
	private final UpdatePoliceDtoMapper updatePoliceDtoMapper;
	private final MappingsResolver mappingsResolver;

	@Autowired
	public PoliceController(PoliceService policeService, DepartmentService departmentService,
							PhotoService photoService, ViolationService violationService,
							ChangeRequestService changeRequestService, PoliceDtoMapper policeDtoMapper,
							PoliceFatDtoMapper policeFatDtoMapper, UpdatePoliceDtoMapper updatePoliceDtoMapper,
							MappingsResolver mappingsResolver) {
		this.policeService = policeService;
		this.departmentService = departmentService;
		this.photoService = photoService;
		this.violationService = violationService;
		this.changeRequestService = changeRequestService;
		this.policeDtoMapper = policeDtoMapper;
		this.policeFatDtoMapper = policeFatDtoMapper;
		this.updatePoliceDtoMapper = updatePoliceDtoMapper;
		this.mappingsResolver = mappingsResolver;
	}

	@GetMapping
	public String getPoliceProfilePage(@RequestParam(required = false, name = MESSAGE_PARAM) String message,
									   Model model) {
		User currentUser = getCurrentUser();
		model.addAttribute(EMAIL_PARAM, currentUser.getUsername());
		Police police = policeService.getPoliceByUserId(currentUser.getId());
		String encodedPhoto = photoService.loadEncodedPhotoByUri(police.getPhotoUri());
		police.setPhotoUri(encodedPhoto);
		model.addAttribute(POLICE_PARAM, police);
		Department department = departmentService.getById(police.getDepartmentId());
		model.addAttribute("department", department);
		model.addAttribute(MESSAGE_PARAM, message);
		return "policeProfile";
	}

	@GetMapping(RELATIVE_HOME_ENDPOINT)
	@PreAuthorize("hasAuthority('POLICE')")
	public String getPoliceHomePage(@RequestParam(required = false, name = MESSAGE_PARAM) String message,
									Model model) {
		model.addAttribute(MESSAGE_PARAM, message);
		Map<LocalDate, Integer> violationsPerDay = violationService.countViolationsPerLastDays(STATISTIC_DAYS_COUNT);
		model.addAttribute(VIOLATIONS_PARAM, violationsPerDay);
		Map<LocalDate, Integer> approvedChangeRequestsPerDay = changeRequestService
				.countApprovedChangeRequestsPerLastDays(STATISTIC_DAYS_COUNT);
		model.addAttribute(REQUESTS_PARAM, approvedChangeRequestsPerDay);
		return "policeHomePage";
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('DRIVER')")
	public String getPoliceWithIdPage(@PathVariable String id, Model model) {
		Police police = policeService.getPoliceById(id);
		model.addAttribute(POLICE_PARAM, toPoliceFatDto(police));
		String encodedPhoto = photoService.loadEncodedPhotoByUri(police.getPhotoUri());
		model.addAttribute(PHOTO_PARAM, encodedPhoto);
		return "policePage";
	}

	private PoliceFatDto toPoliceFatDto(Police police) {
		PoliceFatDto policeFatDto = policeFatDtoMapper.toDto(police);
		return mappingsResolver.resolveMappings(police, policeFatDto);
	}

	@GetMapping("/create")
	@PreAuthorize("hasAuthority('POLICE')")
	public String getCreatePolicePage(Model model) {
		model.addAttribute(DEPARTMENTS_PARAM, departmentService.getAll());
		return CREATE_POLICE_VIEW;
	}

	@PostMapping("/create")
	@PreAuthorize("hasAuthority('POLICE')")
	public String createPolice(PoliceDto policeDto, MultipartFile photo, Model model) {
		Police police = policeDtoMapper.toEntity(policeDto);
		try {
			policeService.createPoliceProfileWithPhoto(police, photo);
			return redirectToWithMessage(HOME_ENDPOINT, "Profile has been successfully created");
		} catch(ValidationException e) {
			model.addAttribute(POLICE_PARAM, police);
			model.addAttribute(DEPARTMENTS_PARAM, departmentService.getAll());
			putValidationExceptionIntoModel(e, model);
			return CREATE_POLICE_VIEW;
		}
	}

	@GetMapping("/update")
	@PreAuthorize("hasAuthority('POLICE')")
	public String getUpdatePolicePage(Model model) {
		User currentUser = getCurrentUser();
		Police police = policeService.getPoliceByUserId(currentUser.getId());
		model.addAttribute(POLICE_PARAM, police);
		model.addAttribute(DEPARTMENTS_PARAM, departmentService.getAll());
		return UPDATE_POLICE_VIEW;
	}

	@PostMapping("/update")
	@PreAuthorize("hasAuthority('POLICE')")
	public String updatePolice(UpdatePoliceDto updatePoliceDto, Model model) {
		Police police = updatePoliceDtoMapper.toEntity(updatePoliceDto);
		try {
			policeService.updatePoliceProfile(police);
			return redirectToWithMessage("/police", "Successfully updated profile data");
		} catch(ValidationException e) {
			model.addAttribute(POLICE_PARAM, police);
			model.addAttribute(DEPARTMENTS_PARAM, departmentService.getAll());
			putValidationExceptionIntoModel(e, model);
			return UPDATE_POLICE_VIEW;
		}
	}

	@GetMapping("/update/photo")
	public String getUpdatePolicePhotoPage() {
		return UPDATE_POLICE_PHOTO_VIEW;
	}

	@PostMapping("/update/photo")
	public String updatePolicePhoto(MultipartFile photo, Model model) {
		try {
			policeService.updatePolicePhoto(photo);
			model.addAttribute(SUCCESS_PARAM, true);
		} catch(ValidationException e) {
			putValidationExceptionIntoModel(e, model);
		}
		return UPDATE_POLICE_PHOTO_VIEW;
	}

}
