package com.drofff.palindrome.mapper;

import com.drofff.palindrome.document.Brand;
import com.drofff.palindrome.dto.BrandDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BrandDtoMapper extends Mapper<Brand, BrandDto> {

    @Autowired
    public BrandDtoMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

}
