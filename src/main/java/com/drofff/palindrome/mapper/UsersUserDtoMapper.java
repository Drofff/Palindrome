package com.drofff.palindrome.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.dto.UsersUserDto;

@Component
public class UsersUserDtoMapper extends Mapper<User, UsersUserDto> {

	@Autowired
	public UsersUserDtoMapper(ModelMapper modelMapper) {
		super(modelMapper);
	}

}
