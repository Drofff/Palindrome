package com.drofff.palindrome.mapper;

import com.drofff.palindrome.document.ViolationType;
import com.drofff.palindrome.dto.ViolationTypeDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ViolationTypeDtoMapper extends Mapper<ViolationType, ViolationTypeDto> {

    @Autowired
    public ViolationTypeDtoMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

}
