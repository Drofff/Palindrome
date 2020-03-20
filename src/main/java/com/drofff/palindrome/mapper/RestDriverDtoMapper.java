package com.drofff.palindrome.mapper;

import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.dto.RestDriverDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestDriverDtoMapper extends Mapper<Driver, RestDriverDto> {

    @Autowired
    public RestDriverDtoMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

}
