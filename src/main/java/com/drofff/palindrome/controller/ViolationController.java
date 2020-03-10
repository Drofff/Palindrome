package com.drofff.palindrome.controller;

import static com.drofff.palindrome.constants.ParameterConstants.VIOLATIONS_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.VIOLATION_PARAM;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;
import static com.drofff.palindrome.utils.ListUtils.applyToEachListElement;
import static com.drofff.palindrome.utils.ModelUtils.putPageIntoModel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.Police;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.document.Violation;
import com.drofff.palindrome.dto.ViolationFatDto;
import com.drofff.palindrome.dto.ViolationsViolationDto;
import com.drofff.palindrome.mapper.ViolationFatDtoMapper;
import com.drofff.palindrome.mapper.ViolationsViolationDtoMapper;
import com.drofff.palindrome.service.DriverService;
import com.drofff.palindrome.service.MappingsResolver;
import com.drofff.palindrome.service.PoliceService;
import com.drofff.palindrome.service.ViolationService;

@Controller
@RequestMapping("/violation")
public class ViolationController {

	private final ViolationService violationService;
	private final DriverService driverService;
	private final PoliceService policeService;
	private final ViolationsViolationDtoMapper violationsViolationDtoMapper;
	private final ViolationFatDtoMapper violationFatDtoMapper;
	private final MappingsResolver mappingsResolver;

	@Autowired
	public ViolationController(ViolationService violationService, DriverService driverService,
	                           PoliceService policeService, ViolationsViolationDtoMapper violationsViolationDtoMapper,
	                           ViolationFatDtoMapper violationFatDtoMapper, MappingsResolver mappingsResolver) {
		this.violationService = violationService;
		this.driverService = driverService;
		this.policeService = policeService;
		this.violationsViolationDtoMapper = violationsViolationDtoMapper;
		this.violationFatDtoMapper = violationFatDtoMapper;
		this.mappingsResolver = mappingsResolver;
	}

	@GetMapping
	@PreAuthorize("hasAuthority('DRIVER')")
	public String getMyViolationsPage(Pageable pageRequest, Model model) {
		Driver driver = driverService.getCurrentDriver();
		Page<Violation> violations = violationService.getPageOfDriverViolations(driver, pageRequest);
		List<ViolationsViolationDto> violationsViolationDtoList = applyToEachListElement(this::toViolationsViolationDto, violations.getContent());
		model.addAttribute(VIOLATIONS_PARAM, violationsViolationDtoList);
		putPageIntoModel(violations, model);
		return "myViolationsPage";
	}

	private ViolationsViolationDto toViolationsViolationDto(Violation violation) {
		ViolationsViolationDto dto = violationsViolationDtoMapper.toDto(violation);
		return mappingsResolver.resolveMappings(violation, dto);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('DRIVER')")
	public String getViolationPage(@PathVariable String id, Model model) {
		User currentUser = getCurrentUser();
		Violation violation = violationService.getViolationOfUserById(currentUser, id);
		ViolationFatDto violationFatDto = toViolationFatDto(violation);
		model.addAttribute(VIOLATION_PARAM, violationFatDto);
		return "violationPage";
	}

	private ViolationFatDto toViolationFatDto(Violation violation) {
		ViolationFatDto violationFatDto = violationFatDtoMapper.toDto(violation);
		Police officer = policeService.getPoliceById(violation.getOfficerId());
		violationFatDto.setOfficer(officer);
		return mappingsResolver.resolveMappings(violation, violationFatDto);
	}

}
