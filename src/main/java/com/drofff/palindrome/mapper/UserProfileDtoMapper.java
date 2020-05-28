package com.drofff.palindrome.mapper;

import com.drofff.palindrome.document.UserProfile;
import com.drofff.palindrome.dto.UserProfileDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserProfileDtoMapper extends Mapper<UserProfile, UserProfileDto> {

    @Autowired
    public UserProfileDtoMapper(ModelMapper modelMapper) {
        super(modelMapper);
    }

}
