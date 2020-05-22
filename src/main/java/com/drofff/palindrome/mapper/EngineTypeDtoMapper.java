package com.drofff.palindrome.mapper;

import com.drofff.palindrome.document.EngineType;
import com.drofff.palindrome.dto.EngineTypeDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EngineTypeDtoMapper extends Mapper<EngineType, EngineTypeDto> {

    @Autowired
    public EngineTypeDtoMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

}
