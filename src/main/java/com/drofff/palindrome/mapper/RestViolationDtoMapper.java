package com.drofff.palindrome.mapper;

import com.drofff.palindrome.document.Violation;
import com.drofff.palindrome.dto.RestViolationDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestViolationDtoMapper extends Mapper<Violation, RestViolationDto> {

    @Autowired
    public RestViolationDtoMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

}
