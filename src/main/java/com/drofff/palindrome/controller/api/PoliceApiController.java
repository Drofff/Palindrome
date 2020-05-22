package com.drofff.palindrome.controller.api;

import com.drofff.palindrome.document.Department;
import com.drofff.palindrome.document.Police;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.dto.RestMessageDto;
import com.drofff.palindrome.dto.RestPoliceDto;
import com.drofff.palindrome.dto.RestResponseDto;
import com.drofff.palindrome.mapper.RestPoliceDtoMapper;
import com.drofff.palindrome.service.DepartmentService;
import com.drofff.palindrome.service.PhotoService;
import com.drofff.palindrome.service.PoliceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;

@Controller
@RequestMapping("/api/polices")
public class PoliceApiController {

	private final PoliceService policeService;
	private final DepartmentService departmentService;
	private final PhotoService photoService;
	private final RestPoliceDtoMapper restPoliceDtoMapper;

	@Value("${application.url}")
	private String applicationUrl;

	@Autowired
	public PoliceApiController(PoliceService policeService, DepartmentService departmentService,
	                           PhotoService photoService, RestPoliceDtoMapper restPoliceDtoMapper) {
		this.policeService = policeService;
		this.departmentService = departmentService;
		this.photoService = photoService;
		this.restPoliceDtoMapper = restPoliceDtoMapper;
	}

	@GetMapping("/info")
	public ResponseEntity<RestResponseDto> getCurrentPolice() {
		User user = getCurrentUser();
		Police police = policeService.getPoliceByUserId(user.getId());
		RestPoliceDto restPoliceDto = toRestPoliceDto(police);
		return ResponseEntity.ok(restPoliceDto);
	}

	private RestPoliceDto toRestPoliceDto(Police police) {
		RestPoliceDto restPoliceDto = restPoliceDtoMapper.toDto(police);
		String department = getNameOfDepartmentWithId(police.getDepartmentId());
		restPoliceDto.setDepartment(department);
		String photoUrl = generatePhotoUrlForPolice(police);
		restPoliceDto.setPhotoUrl(photoUrl);
		return restPoliceDto;
	}

	private String getNameOfDepartmentWithId(String id) {
		Department department = departmentService.getById(id);
		return department.getName();
	}

	private String generatePhotoUrlForPolice(Police police) {
		return applicationUrl + "/api/polices/" + police.getId() + "/photo";
	}

	@GetMapping("/{id}/photo")
	public ResponseEntity<byte[]> getPhotoOfPoliceWithId(@PathVariable String id) {
		Police police = policeService.getPoliceById(id);
		String policePhotoUri = police.getPhotoUri();
		byte[] photo = photoService.loadPhotoByUri(policePhotoUri);
		return ResponseEntity.ok(photo);
	}

	@PostMapping("/two-step-auth/enable")
	public ResponseEntity<RestResponseDto> enableTwoStepAuthForPolice() {
		policeService.enableTwoStepAuth();
		RestMessageDto restMessageDto = new RestMessageDto("Two step authentication has been successfully enabled");
		return ResponseEntity.ok(restMessageDto);
	}

	@PostMapping("/two-step-auth/disable")
	public ResponseEntity<RestResponseDto> disableTwoStepAuthForPolice() {
		policeService.disableTwoStepAuth();
		RestMessageDto restMessageDto = new RestMessageDto("Two step authentication has been successfully disabled");
		return ResponseEntity.ok(restMessageDto);
	}

}
