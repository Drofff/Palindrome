package com.drofff.palindrome.mapper;

import com.drofff.palindrome.document.Violation;
import com.drofff.palindrome.dto.PoliceViolationDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PoliceViolationDtoMapper extends Mapper<Violation, PoliceViolationDto> {

    public PoliceViolationDtoMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

}
