package com.drofff.palindrome.controller.mvc;

import com.drofff.palindrome.document.LicenceCategory;
import com.drofff.palindrome.dto.LicenceCategoryDto;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.mapper.LicenceCategoryDtoMapper;
import com.drofff.palindrome.service.CarService;
import com.drofff.palindrome.service.LicenceCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.drofff.palindrome.constants.ParameterConstants.LICENCE_CATEGORIES_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.MESSAGE_PARAM;
import static com.drofff.palindrome.utils.ModelUtils.putValidationExceptionIntoModel;
import static com.drofff.palindrome.utils.ModelUtils.redirectToWithMessage;

@Controller
@RequestMapping("/admin/licence-category")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminLicenceCategoryController {

    private static final String ALL_LICENCE_CATEGORIES_ENDPOINT = "/admin/licence-category";

    private static final String CREATE_LICENCE_CATEGORY_VIEW = "createLicenceCategoryPage";
    private static final String UPDATE_LICENCE_CATEGORY_VIEW = "updateLicenceCategoryPage";

    private static final String LICENCE_CATEGORY_PARAM = "licenceCategory";

    private final LicenceCategoryService licenceCategoryService;
    private final CarService carService;
    private final LicenceCategoryDtoMapper licenceCategoryDtoMapper;

    @Autowired
    public AdminLicenceCategoryController(LicenceCategoryService licenceCategoryService, CarService carService,
                                          LicenceCategoryDtoMapper licenceCategoryDtoMapper) {
        this.licenceCategoryService = licenceCategoryService;
        this.carService = carService;
        this.licenceCategoryDtoMapper = licenceCategoryDtoMapper;
    }

    @GetMapping
    public String getAllLicenceCategoriesPage(@RequestParam(required = false, name = MESSAGE_PARAM) String message, Model model) {
        List<LicenceCategory> licenceCategories = licenceCategoryService.getAll();
        model.addAttribute(LICENCE_CATEGORIES_PARAM, licenceCategories);
        model.addAttribute(MESSAGE_PARAM, message);
        return "adminLicenceCategoriesPage";
    }

    @GetMapping("/create")
    public String getCreateLicenceCategoryPage() {
        return CREATE_LICENCE_CATEGORY_VIEW;
    }

    @PostMapping("/create")
    public String createLicenceCategory(LicenceCategoryDto licenceCategoryDto, Model model) {
        try {
            LicenceCategory licenceCategory = licenceCategoryDtoMapper.toEntity(licenceCategoryDto);
            licenceCategoryService.create(licenceCategory);
            return redirectToWithMessage(ALL_LICENCE_CATEGORIES_ENDPOINT, "Successfully created licence category");
        } catch(ValidationException e) {
            putValidationExceptionIntoModel(e, model);
            model.addAttribute(LICENCE_CATEGORY_PARAM, licenceCategoryDto);
            return CREATE_LICENCE_CATEGORY_VIEW;
        }
    }

    @GetMapping("/update/{id}")
    public String getUpdateLicenceCategoryPage(@PathVariable String id, Model model) {
        LicenceCategory licenceCategory = licenceCategoryService.getById(id);
        model.addAttribute(LICENCE_CATEGORY_PARAM, licenceCategory);
        return UPDATE_LICENCE_CATEGORY_VIEW;
    }

    @PostMapping("/update/{id}")
    public String updateLicenceCategory(@PathVariable String id, LicenceCategoryDto licenceCategoryDto, Model model) {
        LicenceCategory licenceCategory = licenceCategoryDtoMapper.toEntity(licenceCategoryDto);
        licenceCategory.setId(id);
        try {
            licenceCategoryService.update(licenceCategory);
            return redirectToWithMessage(ALL_LICENCE_CATEGORIES_ENDPOINT, "Successfully updated licence category " +
                    licenceCategoryDto.getName());
        } catch(ValidationException e) {
            putValidationExceptionIntoModel(e, model);
            model.addAttribute(LICENCE_CATEGORY_PARAM, licenceCategory);
            return UPDATE_LICENCE_CATEGORY_VIEW;
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteLicenceCategoryWithId(@PathVariable String id) {
        LicenceCategory licenceCategory = licenceCategoryService.getById(id);
        if(isNotInUse(licenceCategory)) {
            licenceCategoryService.delete(licenceCategory);
            return redirectToWithMessage(ALL_LICENCE_CATEGORIES_ENDPOINT, "Successfully deleted licence category");
        } else {
            return redirectToWithMessage(ALL_LICENCE_CATEGORIES_ENDPOINT, "Licence category in use can not be deleted");
        }
    }

    private boolean isNotInUse(LicenceCategory licenceCategory) {
        return !carService.hasAnyCarWithLicenceCategory(licenceCategory);
    }

}
