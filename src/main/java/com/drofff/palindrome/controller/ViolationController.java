package com.drofff.palindrome.controller;

import static com.drofff.palindrome.utils.ModelUtils.putPageIntoModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.Violation;
import com.drofff.palindrome.service.DriverService;
import com.drofff.palindrome.service.ViolationService;

@Controller
@RequestMapping("/violation")
public class ViolationController {

	private final ViolationService violationService;
	private final DriverService driverService;

	@Autowired
	public ViolationController(ViolationService violationService, DriverService driverService) {
		this.violationService = violationService;
		this.driverService = driverService;
	}

	@GetMapping
	@PreAuthorize("hasAuthority('DRIVER')")
	public String getMyViolationsPage(Pageable pageRequest, Model model) {
		Driver driver = driverService.getCurrentDriver();
		Page<Violation> violations = violationService.getPageOfDriverViolations(driver, pageRequest);
		putPageIntoModel(violations, model);
		return "myViolationsPage";
	}

}
