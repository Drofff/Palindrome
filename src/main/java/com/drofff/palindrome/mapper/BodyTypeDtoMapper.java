package com.drofff.palindrome.mapper;

import com.drofff.palindrome.document.BodyType;
import com.drofff.palindrome.dto.BodyTypeDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BodyTypeDtoMapper extends Mapper<BodyType, BodyTypeDto> {

    @Autowired
    public BodyTypeDtoMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

}
