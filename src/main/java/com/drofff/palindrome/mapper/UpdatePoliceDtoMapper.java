package com.drofff.palindrome.mapper;

import com.drofff.palindrome.document.Police;
import com.drofff.palindrome.dto.UpdatePoliceDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdatePoliceDtoMapper extends Mapper<Police, UpdatePoliceDto> {

    @Autowired
    public UpdatePoliceDtoMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

}
