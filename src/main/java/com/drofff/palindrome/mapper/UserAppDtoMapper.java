package com.drofff.palindrome.mapper;

import com.drofff.palindrome.document.UserApp;
import com.drofff.palindrome.dto.UserAppDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserAppDtoMapper extends Mapper<UserApp, UserAppDto> {

    @Autowired
    public UserAppDtoMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

}
