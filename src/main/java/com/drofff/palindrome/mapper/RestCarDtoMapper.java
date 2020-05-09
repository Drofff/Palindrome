package com.drofff.palindrome.mapper;

import com.drofff.palindrome.document.Car;
import com.drofff.palindrome.dto.RestCarDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestCarDtoMapper extends Mapper<Car, RestCarDto> {

    @Autowired
    public RestCarDtoMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

}
