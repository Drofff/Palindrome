package com.drofff.palindrome.mapper;

import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.dto.RestFindDriverDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestFindDriverDtoMapper extends Mapper<Driver, RestFindDriverDto> {

    @Autowired
    public RestFindDriverDtoMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

}
