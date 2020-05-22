package com.drofff.palindrome.controller.mvc;

import com.drofff.palindrome.document.Brand;
import com.drofff.palindrome.dto.BrandDto;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.mapper.BrandDtoMapper;
import com.drofff.palindrome.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.drofff.palindrome.constants.ParameterConstants.BRANDS_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.MESSAGE_PARAM;
import static com.drofff.palindrome.utils.ModelUtils.putValidationExceptionIntoModel;
import static com.drofff.palindrome.utils.ModelUtils.redirectToWithMessage;

@Controller("/admin/brand")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminBrandController {

    private static final String ALL_BRANDS_ENDPOINT = "/admin/brand";

    private static final String CREATE_BRAND_VIEW = "createBrandPage";
    private static final String UPDATE_BRAND_VIEW = "updateBrandPage";

    private static final String BRAND_PARAM = "brand";

    private final BrandService brandService;
    private final BrandDtoMapper brandDtoMapper;

    @Autowired
    public AdminBrandController(BrandService brandService, BrandDtoMapper brandDtoMapper) {
        this.brandService = brandService;
        this.brandDtoMapper = brandDtoMapper;
    }

    @GetMapping
    public String getAllBrandsPage(@RequestParam(required = false, name = MESSAGE_PARAM) String message, Model model) {
        List<Brand> brands = brandService.getAll();
        model.addAttribute(BRANDS_PARAM, brands);
        model.addAttribute(MESSAGE_PARAM, message);
        return "adminBrandsPage";
    }

    @GetMapping("/create")
    public String getCreateBrandPage() {
        return CREATE_BRAND_VIEW;
    }

    @PostMapping("/create")
    public String createBrand(BrandDto brandDto, Model model) {
        try {
            Brand brand = brandDtoMapper.toEntity(brandDto);
            brandService.create(brand);
            return redirectToWithMessage(ALL_BRANDS_ENDPOINT, "Successfully created brand " + brand.getName());
        } catch(ValidationException e) {
            putValidationExceptionIntoModel(e, model);
            model.addAttribute(BRAND_PARAM, brandDto);
            return CREATE_BRAND_VIEW;
        }
    }

    @GetMapping("/update/{id}")
    public String getUpdateBrandPage(@PathVariable String id, Model model) {
        Brand brand = brandService.getById(id);
        model.addAttribute(BRAND_PARAM, brand);
        return UPDATE_BRAND_VIEW;
    }

    @PostMapping("/update/{id}")
    public String updateBrand(@PathVariable String id, BrandDto brandDto, Model model) {
        try {
            Brand brand = brandDtoMapper.toEntity(brandDto);
            brand.setId(id);
            brandService.update(brand);
            return redirectToWithMessage(ALL_BRANDS_ENDPOINT, "Successfully updated brand " + brand.getName());
        } catch(ValidationException e) {
            putValidationExceptionIntoModel(e, model);
            model.addAttribute(BRAND_PARAM, brandDto);
            return UPDATE_BRAND_VIEW;
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteBrandWithId(@PathVariable String id) {
        Brand brand = brandService.getById(id);
        brandService.delete(brand);
        return redirectToWithMessage(ALL_BRANDS_ENDPOINT, "Brand has been successfully deleted");
    }

}
