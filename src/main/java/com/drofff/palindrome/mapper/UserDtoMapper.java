package com.drofff.palindrome.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.dto.UserDto;

@Component
public class UserDtoMapper extends Mapper<User, UserDto> {

	@Autowired
	public UserDtoMapper(ModelMapper modelMapper) {
		super(modelMapper);
	}

}
