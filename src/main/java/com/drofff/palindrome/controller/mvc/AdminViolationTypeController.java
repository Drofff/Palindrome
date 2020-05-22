package com.drofff.palindrome.controller.mvc;

import com.drofff.palindrome.document.ViolationType;
import com.drofff.palindrome.dto.ViolationTypeDto;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.mapper.ViolationTypeDtoMapper;
import com.drofff.palindrome.service.ViolationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.drofff.palindrome.constants.ParameterConstants.MESSAGE_PARAM;
import static com.drofff.palindrome.enums.Currency.getAvailableCurrencies;
import static com.drofff.palindrome.utils.ModelUtils.putValidationExceptionIntoModel;
import static com.drofff.palindrome.utils.ModelUtils.redirectToWithMessage;

@Controller
@RequestMapping("/admin/violation-type")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminViolationTypeController {

    private static final String ALL_VIOLATION_TYPES_ENDPOINT = "/admin/violation-type";

    private static final String CREATE_VIOLATION_TYPE_VIEW = "createViolationTypePage";
    private static final String UPDATE_VIOLATION_TYPE_VIEW = "updateViolationTypePage";

    private static final String VIOLATION_TYPE_PARAM = "violationType";
    private static final String AVAILABLE_CURRENCIES_PARAM = "availableCurrencies";

    private final ViolationTypeService violationTypeService;
    private final ViolationTypeDtoMapper violationTypeDtoMapper;

    @Autowired
    public AdminViolationTypeController(ViolationTypeService violationTypeService, ViolationTypeDtoMapper violationTypeDtoMapper) {
        this.violationTypeService = violationTypeService;
        this.violationTypeDtoMapper = violationTypeDtoMapper;
    }

    @GetMapping
    public String getAllViolationTypesPage(@RequestParam(required = false, name = MESSAGE_PARAM) String message, Model model) {
        List<ViolationType> violationTypes = violationTypeService.getAll();
        model.addAttribute("violationTypes", violationTypes);
        model.addAttribute(MESSAGE_PARAM, message);
        return "adminViolationTypesPage";
    }

    @GetMapping("/create")
    public String getCreateViolationTypePage(Model model) {
        model.addAttribute(AVAILABLE_CURRENCIES_PARAM, getAvailableCurrencies());
        return CREATE_VIOLATION_TYPE_VIEW;
    }

    @PostMapping("/create")
    public String createViolationType(ViolationTypeDto violationTypeDto, Model model) {
        try {
            ViolationType violationType = violationTypeDtoMapper.toEntity(violationTypeDto);
            violationTypeService.create(violationType);
            return redirectToWithMessage(ALL_VIOLATION_TYPES_ENDPOINT, "Successfully created violation type");
        } catch(ValidationException e) {
            putValidationExceptionIntoModel(e, model);
            model.addAttribute(VIOLATION_TYPE_PARAM, violationTypeDto);
            model.addAttribute(AVAILABLE_CURRENCIES_PARAM, getAvailableCurrencies());
            return CREATE_VIOLATION_TYPE_VIEW;
        }
    }

    @GetMapping("/update/{id}")
    public String getUpdateViolationTypePage(@PathVariable String id, Model model) {
        ViolationType violationType = violationTypeService.getById(id);
        model.addAttribute(VIOLATION_TYPE_PARAM, violationType);
        model.addAttribute(AVAILABLE_CURRENCIES_PARAM, getAvailableCurrencies());
        return UPDATE_VIOLATION_TYPE_VIEW;
    }

    @PostMapping("/update/{id}")
    public String updateViolationType(@PathVariable String id, ViolationTypeDto violationTypeDto, Model model) {
        try {
            ViolationType violationType = violationTypeDtoMapper.toEntity(violationTypeDto);
            violationType.setId(id);
            violationTypeService.update(violationType);
            return redirectToWithMessage(ALL_VIOLATION_TYPES_ENDPOINT, "Successfully updated violation type " +
                    violationType.getName());
        } catch(ValidationException e) {
            putValidationExceptionIntoModel(e, model);
            model.addAttribute(VIOLATION_TYPE_PARAM, violationTypeDto);
            model.addAttribute(AVAILABLE_CURRENCIES_PARAM, getAvailableCurrencies());
            return UPDATE_VIOLATION_TYPE_VIEW;
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteViolationTypeWithId(@PathVariable String id) {
        ViolationType violationType = violationTypeService.getById(id);
        violationTypeService.delete(violationType);
        return redirectToWithMessage(ALL_VIOLATION_TYPES_ENDPOINT, "Successfully deleted violation type");
    }

}
