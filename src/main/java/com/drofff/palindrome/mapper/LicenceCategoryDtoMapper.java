package com.drofff.palindrome.mapper;

import com.drofff.palindrome.document.LicenceCategory;
import com.drofff.palindrome.dto.LicenceCategoryDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LicenceCategoryDtoMapper extends Mapper<LicenceCategory, LicenceCategoryDto> {

    @Autowired
    public LicenceCategoryDtoMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

}
