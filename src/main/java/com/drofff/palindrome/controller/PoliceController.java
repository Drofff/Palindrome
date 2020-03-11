package com.drofff.palindrome.controller;

import static com.drofff.palindrome.constants.EndpointConstants.HOME_ENDPOINT;
import static com.drofff.palindrome.constants.ParameterConstants.PHOTO_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.POLICE_PARAM;
import static com.drofff.palindrome.utils.ModelUtils.putValidationExceptionIntoModel;
import static com.drofff.palindrome.utils.ModelUtils.redirectToWithMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.drofff.palindrome.document.Police;
import com.drofff.palindrome.dto.PoliceDto;
import com.drofff.palindrome.dto.PoliceFatDto;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.mapper.PoliceDtoMapper;
import com.drofff.palindrome.mapper.PoliceFatDtoMapper;
import com.drofff.palindrome.service.DepartmentService;
import com.drofff.palindrome.service.MappingsResolver;
import com.drofff.palindrome.service.PhotoService;
import com.drofff.palindrome.service.PoliceService;

@Controller
@RequestMapping("/police")
public class PoliceController {

	private static final String CREATE_POLICE_VIEW = "createPolicePage";

	private static final String DEPARTMENTS_PARAM = "departments";

	private final PoliceService policeService;
	private final DepartmentService departmentService;
	private final PhotoService photoService;
	private final PoliceDtoMapper policeDtoMapper;
	private final PoliceFatDtoMapper policeFatDtoMapper;
	private final MappingsResolver mappingsResolver;

	@Autowired
	public PoliceController(PoliceService policeService, DepartmentService departmentService,
	                        PhotoService photoService, PoliceDtoMapper policeDtoMapper,
	                        PoliceFatDtoMapper policeFatDtoMapper, MappingsResolver mappingsResolver) {
		this.policeService = policeService;
		this.departmentService = departmentService;
		this.photoService = photoService;
		this.policeDtoMapper = policeDtoMapper;
		this.policeFatDtoMapper = policeFatDtoMapper;
		this.mappingsResolver = mappingsResolver;
	}

	@GetMapping("/{id}")
	public String getPoliceProfilePage(@PathVariable String id, Model model) {
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
