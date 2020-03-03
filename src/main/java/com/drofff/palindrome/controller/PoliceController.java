package com.drofff.palindrome.controller;

import static com.drofff.palindrome.constants.EndpointConstants.HOME_ENDPOINT;
import static com.drofff.palindrome.constants.ParameterConstants.POLICE_PARAM;
import static com.drofff.palindrome.utils.ModelUtils.putValidationExceptionIntoModel;
import static com.drofff.palindrome.utils.ModelUtils.redirectToWithMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.drofff.palindrome.document.Police;
import com.drofff.palindrome.dto.PoliceDto;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.mapper.PoliceDtoMapper;
import com.drofff.palindrome.service.DepartmentService;
import com.drofff.palindrome.service.PoliceService;

@Controller
@RequestMapping("/police")
public class PoliceController {

	private static final String CREATE_POLICE_VIEW = "createPolicePage";

	private static final String DEPARTMENTS_PARAM = "departments";

	private final PoliceService policeService;
	private final DepartmentService departmentService;
	private final PoliceDtoMapper policeDtoMapper;

	@Autowired
	public PoliceController(PoliceService policeService, DepartmentService departmentService,
	                        PoliceDtoMapper policeDtoMapper) {
		this.policeService = policeService;
		this.departmentService = departmentService;
		this.policeDtoMapper = policeDtoMapper;
	}

	@GetMapping("/create")
	public String getCreatePolicePage(Model model) {
		model.addAttribute(DEPARTMENTS_PARAM, departmentService.getAll());
		return CREATE_POLICE_VIEW;
	}

	@PostMapping("/create")
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

}
