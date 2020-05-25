package com.drofff.palindrome.controller.mvc;

import com.drofff.palindrome.document.Department;
import com.drofff.palindrome.dto.DepartmentDto;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.mapper.DepartmentDtoMapper;
import com.drofff.palindrome.service.DepartmentService;
import com.drofff.palindrome.service.PoliceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.drofff.palindrome.constants.ParameterConstants.DEPARTMENTS_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.MESSAGE_PARAM;
import static com.drofff.palindrome.utils.ModelUtils.putValidationExceptionIntoModel;
import static com.drofff.palindrome.utils.ModelUtils.redirectToWithMessage;

@Controller
@RequestMapping("/admin/department")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminDepartmentController {

    private static final String ALL_DEPARTMENTS_ENDPOINT = "/admin/department";

    private static final String CREATE_DEPARTMENT_VIEW = "createDepartmentPage";
    private static final String UPDATE_DEPARTMENT_VIEW = "updateDepartmentPage";

    private static final String DEPARTMENT_PARAM = "department";

    private final DepartmentService departmentService;
    private final PoliceService policeService;
    private final DepartmentDtoMapper departmentDtoMapper;

    @Autowired
    public AdminDepartmentController(DepartmentService departmentService, PoliceService policeService,
                                     DepartmentDtoMapper departmentDtoMapper) {
        this.departmentService = departmentService;
        this.policeService = policeService;
        this.departmentDtoMapper = departmentDtoMapper;
    }

    @GetMapping
    public String getAllDepartmentsPage(@RequestParam(required = false, name = MESSAGE_PARAM) String message, Model model) {
        List<Department> departments = departmentService.getAll();
        model.addAttribute(DEPARTMENTS_PARAM, departments);
        model.addAttribute(MESSAGE_PARAM, message);
        return "adminDepartmentsPage";
    }

    @GetMapping("/create")
    public String getCreateDepartmentPage() {
        return CREATE_DEPARTMENT_VIEW;
    }

    @PostMapping("/create")
    public String createDepartment(DepartmentDto departmentDto, Model model) {
        try {
            Department department = departmentDtoMapper.toEntity(departmentDto);
            departmentService.create(department);
            return redirectToWithMessage(ALL_DEPARTMENTS_ENDPOINT, "Successfully created department");
        } catch(ValidationException e) {
            putValidationExceptionIntoModel(e, model);
            model.addAttribute(DEPARTMENT_PARAM, departmentDto);
            return CREATE_DEPARTMENT_VIEW;
        }
    }

    @GetMapping("/update/{id}")
    public String getUpdateDepartmentPage(@PathVariable String id, Model model) {
        Department department = departmentService.getById(id);
        model.addAttribute(DEPARTMENT_PARAM, department);
        return UPDATE_DEPARTMENT_VIEW;
    }

    @PostMapping("/update/{id}")
    public String updateDepartment(@PathVariable String id, DepartmentDto departmentDto, Model model) {
        Department department = departmentDtoMapper.toEntity(departmentDto);
        department.setId(id);
        try {
            departmentService.update(department);
            return redirectToWithMessage(ALL_DEPARTMENTS_ENDPOINT, "Successfully updated department " + department.getName());
        } catch(ValidationException e) {
            putValidationExceptionIntoModel(e, model);
            model.addAttribute(DEPARTMENT_PARAM, department);
            return UPDATE_DEPARTMENT_VIEW;
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteDepartmentWithId(@PathVariable String id) {
        Department department = departmentService.getById(id);
        if(isNotInUse(department)) {
            departmentService.delete(department);
            return redirectToWithMessage(ALL_DEPARTMENTS_ENDPOINT, "Successfully deleted department");
        } else {
            return redirectToWithMessage(ALL_DEPARTMENTS_ENDPOINT, "Department in use can not be deleted");
        }
    }

    private boolean isNotInUse(Department department) {
        return !policeService.hasAnyPoliceFromDepartment(department);
    }

}
