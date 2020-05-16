package com.drofff.palindrome.mapper;

import com.drofff.palindrome.document.ChangeRequest;
import com.drofff.palindrome.dto.SentChangeRequestDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class SentChangeRequestDtoMapper extends Mapper<ChangeRequest, SentChangeRequestDto> {

    public SentChangeRequestDtoMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

}
