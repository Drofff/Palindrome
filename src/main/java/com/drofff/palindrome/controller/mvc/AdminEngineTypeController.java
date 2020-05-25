package com.drofff.palindrome.controller.mvc;

import com.drofff.palindrome.document.EngineType;
import com.drofff.palindrome.dto.EngineTypeDto;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.mapper.EngineTypeDtoMapper;
import com.drofff.palindrome.service.CarService;
import com.drofff.palindrome.service.EngineTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.drofff.palindrome.constants.ParameterConstants.ENGINE_TYPES_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.MESSAGE_PARAM;
import static com.drofff.palindrome.utils.ModelUtils.putValidationExceptionIntoModel;
import static com.drofff.palindrome.utils.ModelUtils.redirectToWithMessage;

@Controller
@RequestMapping("/admin/engine-type")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminEngineTypeController {

    private static final String ALL_ENGINE_TYPES_ENDPOINT = "/admin/engine-type";

    private static final String CREATE_ENGINE_TYPE_VIEW = "createEngineTypePage";
    private static final String UPDATE_ENGINE_TYPE_VIEW = "updateEngineTypePage";

    private static final String ENGINE_TYPE_PARAM = "engineType";

    private final EngineTypeService engineTypeService;
    private final CarService carService;
    private final EngineTypeDtoMapper engineTypeDtoMapper;

    @Autowired
    public AdminEngineTypeController(EngineTypeService engineTypeService, CarService carService,
                                     EngineTypeDtoMapper engineTypeDtoMapper) {
        this.engineTypeService = engineTypeService;
        this.carService = carService;
        this.engineTypeDtoMapper = engineTypeDtoMapper;
    }

    @GetMapping
    public String getAllEngineTypesPage(@RequestParam(required = false, name = MESSAGE_PARAM) String message, Model model) {
        List<EngineType> engineTypes = engineTypeService.getAll();
        model.addAttribute(ENGINE_TYPES_PARAM, engineTypes);
        model.addAttribute(MESSAGE_PARAM, message);
        return "adminEngineTypesPage";
    }

    @GetMapping("/create")
    public String getCreateEngineTypePage() {
        return CREATE_ENGINE_TYPE_VIEW;
    }

    @PostMapping("/create")
    public String createEngineType(EngineTypeDto engineTypeDto, Model model) {
        try {
            EngineType engineType = engineTypeDtoMapper.toEntity(engineTypeDto);
            engineTypeService.create(engineType);
            return redirectToWithMessage(ALL_ENGINE_TYPES_ENDPOINT, "Engine type has been successfully created");
        } catch(ValidationException e) {
            putValidationExceptionIntoModel(e, model);
            model.addAttribute(ENGINE_TYPE_PARAM, engineTypeDto);
            return CREATE_ENGINE_TYPE_VIEW;
        }
    }

    @GetMapping("/update/{id}")
    public String getUpdateEngineTypePage(@PathVariable String id, Model model) {
        EngineType engineType = engineTypeService.getById(id);
        model.addAttribute(ENGINE_TYPE_PARAM, engineType);
        return UPDATE_ENGINE_TYPE_VIEW;
    }

    @PostMapping("/update/{id}")
    public String updateEngineType(@PathVariable String id, EngineTypeDto engineTypeDto, Model model) {
        EngineType engineType = engineTypeDtoMapper.toEntity(engineTypeDto);
        engineType.setId(id);
        try {
            engineTypeService.update(engineType);
            return redirectToWithMessage(ALL_ENGINE_TYPES_ENDPOINT, "Successfully updated engine type " + engineType.getName());
        } catch(ValidationException e) {
            putValidationExceptionIntoModel(e, model);
            model.addAttribute(ENGINE_TYPE_PARAM, engineType);
            return UPDATE_ENGINE_TYPE_VIEW;
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteEngineTypeWithId(@PathVariable String id) {
        EngineType engineType = engineTypeService.getById(id);
        if(isNotInUse(engineType)) {
            engineTypeService.delete(engineType);
            return redirectToWithMessage(ALL_ENGINE_TYPES_ENDPOINT, "Successfully deleted engine type");
        } else {
            return redirectToWithMessage(ALL_ENGINE_TYPES_ENDPOINT, "Engine type in use can not be deleted");
        }
    }

    private boolean isNotInUse(EngineType engineType) {
        return !carService.hasAnyCarWithEngineType(engineType);
    }

}
