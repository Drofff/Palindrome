package com.drofff.palindrome.controller.mvc;

import com.drofff.palindrome.document.BodyType;
import com.drofff.palindrome.dto.BodyTypeDto;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.mapper.BodyTypeDtoMapper;
import com.drofff.palindrome.service.BodyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.drofff.palindrome.constants.ParameterConstants.BODY_TYPES_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.MESSAGE_PARAM;
import static com.drofff.palindrome.utils.ModelUtils.putValidationExceptionIntoModel;
import static com.drofff.palindrome.utils.ModelUtils.redirectToWithMessage;

@Controller
@RequestMapping("/admin/body-type")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminBodyTypeController {

    private static final String ALL_BODY_TYPES_ENDPOINT = "/admin/body-type";

    private static final String CREATE_BODY_TYPE_VIEW = "createBodyTypePage";
    private static final String UPDATE_BODY_TYPE_VIEW = "updateBodyTypePage";

    private static final String BODY_TYPE_PARAM = "bodyType";

    private final BodyTypeService bodyTypeService;
    private final BodyTypeDtoMapper bodyTypeDtoMapper;

    @Autowired
    public AdminBodyTypeController(BodyTypeService bodyTypeService, BodyTypeDtoMapper bodyTypeDtoMapper) {
        this.bodyTypeService = bodyTypeService;
        this.bodyTypeDtoMapper = bodyTypeDtoMapper;
    }

    @GetMapping
    public String getAllBodyTypesPage(@RequestParam(required = false, name = MESSAGE_PARAM) String message, Model model) {
        List<BodyType> bodyTypes = bodyTypeService.getAll();
        model.addAttribute(BODY_TYPES_PARAM, bodyTypes);
        model.addAttribute(MESSAGE_PARAM, message);
        return "adminBodyTypesPage";
    }

    @GetMapping("/create")
    public String getCreateBodyTypePage() {
        return CREATE_BODY_TYPE_VIEW;
    }

    @PostMapping("/create")
    public String createBodyType(BodyTypeDto bodyTypeDto, Model model) {
        try {
            BodyType bodyType = bodyTypeDtoMapper.toEntity(bodyTypeDto);
            bodyTypeService.create(bodyType);
            return redirectToWithMessage(ALL_BODY_TYPES_ENDPOINT, "Successfully created new body type");
        } catch(ValidationException e) {
            putValidationExceptionIntoModel(e, model);
            model.addAttribute(BODY_TYPE_PARAM, bodyTypeDto);
            return CREATE_BODY_TYPE_VIEW;
        }
    }

    @GetMapping("/update/{id}")
    public String getUpdateBodyTypePage(@PathVariable String id, Model model) {
        BodyType bodyType = bodyTypeService.getById(id);
        model.addAttribute(BODY_TYPE_PARAM, bodyType);
        return UPDATE_BODY_TYPE_VIEW;
    }

    @PostMapping("/update/{id}")
    public String updateBodyType(@PathVariable String id, BodyTypeDto bodyTypeDto, Model model) {
        try {
            BodyType bodyType = bodyTypeDtoMapper.toEntity(bodyTypeDto);
            bodyType.setId(id);
            bodyTypeService.update(bodyType);
            return redirectToWithMessage(ALL_BODY_TYPES_ENDPOINT, "Successfully updated body type " + bodyType.getName());
        } catch(ValidationException e) {
            putValidationExceptionIntoModel(e, model);
            model.addAttribute(BODY_TYPE_PARAM, bodyTypeDto);
            return UPDATE_BODY_TYPE_VIEW;
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteBodyTypeWithId(@PathVariable String id) {
        BodyType bodyType = bodyTypeService.getById(id);
        bodyTypeService.delete(bodyType);
        return redirectToWithMessage(ALL_BODY_TYPES_ENDPOINT, "Successfully deleted body type");
    }

}
