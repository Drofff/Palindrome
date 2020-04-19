package com.drofff.palindrome.controller;

import com.drofff.palindrome.document.Police;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.dto.PoliceFatDto;
import com.drofff.palindrome.dto.UpdatePoliceDto;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.mapper.PoliceFatDtoMapper;
import com.drofff.palindrome.mapper.UpdatePoliceDtoMapper;
import com.drofff.palindrome.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.drofff.palindrome.constants.EndpointConstants.ADMIN_USERS_ENDPOINT;
import static com.drofff.palindrome.constants.ParameterConstants.*;
import static com.drofff.palindrome.utils.ModelUtils.putValidationExceptionIntoModel;
import static com.drofff.palindrome.utils.ModelUtils.redirectToWithMessage;

@Controller
@RequestMapping("/admin/users/police")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminPoliceController {

    private static final String UPDATE_POLICE_VIEW = "adminUpdatePolicePage";

    private final PoliceService policeService;
    private final PhotoService photoService;
    private final UserService userService;
    private final UserBlockService userBlockService;
    private final DepartmentService departmentService;
    private final PoliceFatDtoMapper policeFatDtoMapper;
    private final UpdatePoliceDtoMapper updatePoliceDtoMapper;
    private final MappingsResolver mappingsResolver;

    @Autowired
    public AdminPoliceController(PoliceService policeService, PhotoService photoService,
                                 UserService userService, UserBlockService userBlockService,
                                 DepartmentService departmentService, PoliceFatDtoMapper policeFatDtoMapper,
                                 UpdatePoliceDtoMapper updatePoliceDtoMapper, MappingsResolver mappingsResolver) {
        this.policeService = policeService;
        this.photoService = photoService;
        this.userService = userService;
        this.userBlockService = userBlockService;
        this.departmentService = departmentService;
        this.policeFatDtoMapper = policeFatDtoMapper;
        this.updatePoliceDtoMapper = updatePoliceDtoMapper;
        this.mappingsResolver = mappingsResolver;
    }

    @GetMapping("/{id}")
    public String viewPoliceProfile(@PathVariable String id, Model model) {
        Police police = policeService.getPoliceByUserId(id);
        model.addAttribute(POLICE_PARAM, toPoliceFatDto(police));
        String encodedPhoto = photoService.loadEncodedPhotoByUri(police.getPhotoUri());
        model.addAttribute(PHOTO_PARAM, encodedPhoto);
        User user = userService.getUserById(id);
        boolean blocked = userBlockService.isUserBlocked(user);
        model.addAttribute(BLOCKED_PARAM, blocked);
        return "viewPoliceProfilePage";
    }

    private PoliceFatDto toPoliceFatDto(Police police) {
        PoliceFatDto policeFatDto = policeFatDtoMapper.toDto(police);
        return mappingsResolver.resolveMappings(police, policeFatDto);
    }

    @GetMapping("/{id}/update")
    public String getUpdatePolicePage(@PathVariable String id, Model model) {
        Police police = policeService.getPoliceByUserId(id);
        model.addAttribute(POLICE_PARAM, police);
        model.addAttribute(DEPARTMENTS_PARAM, departmentService.getAll());
        return UPDATE_POLICE_VIEW;
    }

    @PostMapping("/{id}/update")
    public String updatePolice(@PathVariable String id, UpdatePoliceDto updatePoliceDto, Model model) {
        Police police = updatePoliceDtoMapper.toEntity(updatePoliceDto);
        police.setId(id);
        try {
            policeService.updatePoliceProfile(police);
            return redirectToWithMessage(ADMIN_USERS_ENDPOINT, "Successfully updated police data");
        } catch(ValidationException e) {
            model.addAttribute(POLICE_PARAM, police);
            model.addAttribute(DEPARTMENTS_PARAM, departmentService.getAll());
            putValidationExceptionIntoModel(e, model);
            return UPDATE_POLICE_VIEW;
        }
    }

}
